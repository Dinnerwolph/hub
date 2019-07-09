package net.euphalys.hub.managers;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.main.GuiCosmetiques;
import net.euphalys.hub.gui.main.GuiFriends;
import net.euphalys.hub.gui.main.GuiMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */

public class StaticInventory {
    private final Hub hub;
    private final Map<Integer, ItemStack> items;
    private final Map<Player, Boolean> hide;

    public StaticInventory(final Hub hub) {
        this.hub = hub;
        this.items = new ConcurrentHashMap<>();
        this.hide = new ConcurrentHashMap<>();
    }

    public boolean doInteraction(final Player player, final ItemStack stack) {
        Material gunpowder = net.euphalys.api.utils.Material.GUNPOWDER.getBukkitMaterial();
        Material enchanting_table = net.euphalys.api.utils.Material.ENCHANTING_TABLE.getBukkitMaterial();
        if (stack.getType() == Material.COMPASS) {
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
            return true;
        } else if (stack.getType() == gunpowder) {
            this.hideOrShowPlayers(player);
            return true;
        } else if (stack.getType() == Material.NETHER_STAR) {
            this.hub.getGuiManager().openGui(player, new GuiCosmetiques(this.hub));
            return true;
        } else if (stack.getType() == enchanting_table) {
            this.hub.getGuiManager().openGui(player, new GuiFriends(this.hub, 1));
            return true;
        }
        return false;
    }

    private static ItemStack buildItemStack(final Material material, final int quantity, final int data, final String name, final String[] lores) {
        ItemStack stack = new ItemStack(material, quantity, (short) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if (lores != null)
            meta.setLore(Arrays.asList(lores));

        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack buildHead(final String owner, final int quantity, final String name, final String[] lores) {
        ItemStack stack;
        if (EuphalysApi.getInstance().is1_14())
            stack = new ItemStack(net.euphalys.api.utils.Material.PLAYER_HEAD.getBukkitMaterial(), quantity);
        else
            stack = new ItemStack(net.euphalys.api.utils.Material.PLAYER_HEAD.getBukkitMaterial(), quantity, (short) 3);
        SkullMeta meta;
        if (hub.skullCache.containsKey(owner)) {
            stack = hub.skullCache.get(owner);
            meta = (SkullMeta) stack.getItemMeta();
        } else {
            meta = (SkullMeta) stack.getItemMeta();
            meta.setOwner(owner);
            stack.setItemMeta(meta);
            hub.skullCache.put(owner, stack);
        }
        meta.setDisplayName(name);
        if (lores != null)
            meta.setLore(Arrays.asList(lores));
        meta.setOwner(owner);
        stack.setItemMeta(meta);
        return stack;
    }

    private void loadItems(final Player player) {
        Material gunpowder = net.euphalys.api.utils.Material.GUNPOWDER.getBukkitMaterial();
        Material enchanting_table = net.euphalys.api.utils.Material.ENCHANTING_TABLE.getBukkitMaterial();
        this.items.put(2, buildItemStack(gunpowder, 1, 0, "§1Poudre magique", new String[]{"§7\u25B6 Vous permet de masquer les autres joueurs."}));
        this.items.put(4, buildItemStack(Material.COMPASS, 1, 0, "§1Menu principal", new String[]{"§7\u25B6 Affiche les modes de jeux et autres hubs."}));
        this.items.put(6, buildItemStack(Material.NETHER_STAR, 1, 0, "§1Cosmétiques", new String[]{"§7\u25B6 Utilisez une monture ou des particules !"}));
        this.items.put(19, buildHead(player.getName(), 1, "§1Profil : §7" + player.getName(), new String[]{"§6Temps de jeu : §9" + getTimePlayed(player.getUniqueId()), "§6Grade : " + EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getScore()}));
        this.items.put(25, buildItemStack(enchanting_table, 1, 0, "§9Amis", new String[]{"§7\u25B6 Gestion des amis."}));

    }

    public void setInventoryToPlayer(final Player player) {
        loadItems(player);
        Iterator var2 = this.items.keySet().iterator();

        while (var2.hasNext()) {
            int slot = (Integer) var2.next();
            player.getInventory().setItem(slot, this.items.get(slot));
        }

    }

    private void hideOrShowPlayers(final Player target) {
        Iterator var2;
        Player players;
        if (!this.isHinding(target)) {
            this.hide.put(target, true);
            var2 = Bukkit.getOnlinePlayers().iterator();

            while (var2.hasNext()) {
                players = (Player) var2.next();
                target.hidePlayer(players);
            }

            target.sendMessage("§7Pouf ! La magie vient de faire son appartion...");
        } else {
            this.hide.remove(target);
            var2 = Bukkit.getOnlinePlayers().iterator();

            while (var2.hasNext()) {
                players = (Player) var2.next();
                target.showPlayer(players);
            }

            target.sendMessage("§7Pouf, mais où est passé la magie ?");
        }

        target.playSound(target.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
    }

    private boolean isHinding(final Player player) {
        return this.hide.containsKey(player);
    }

    private String getTimePlayed(final UUID uuid) {
        float time = EuphalysApi.getInstance().getPlayer(uuid).getTimePlayed() + 0L;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df;
        if (time >= 86_400_000)
            df = new SimpleDateFormat("DD HH:mm:ss");
        else
            df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(timeZone);
        return df.format((EuphalysApi.getInstance().getPlayer(uuid).getTimePlayed() + 0L) / 1000);
    }
}
