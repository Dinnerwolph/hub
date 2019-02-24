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

/**
 * @author Dinnerwolph
 */

public class StaticInventory {
    private final Hub hub;
    private final HashMap<Integer, ItemStack> items;
    private final HashMap<Player, Boolean> hide;

    public StaticInventory(Hub hub) {
        this.hub = hub;
        this.items = new HashMap();
        this.hide = new HashMap();
    }

    public boolean doInteraction(Player player, ItemStack stack) {
        if (stack.getType() == Material.COMPASS) {
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
            return true;
        } else if (stack.getType() == Material.SULPHUR) {
            this.hideOrShowPlayers(player);
            return true;
        } else if (stack.getType() == Material.NETHER_STAR) {
            this.hub.getGuiManager().openGui(player, new GuiCosmetiques(this.hub));
            return true;
        } else if (stack.getType() == Material.ENCHANTMENT_TABLE) {
            this.hub.getGuiManager().openGui(player, new GuiFriends(this.hub, 1));
            return true;
        }
        return false;
    }

    private static ItemStack buildItemStack(Material material, int quantity, int data, String name, String[] lores) {
        ItemStack stack = new ItemStack(material, quantity, (short) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if (lores != null)
            meta.setLore(Arrays.asList(lores));

        stack.setItemMeta(meta);
        return stack;
    }

    private static ItemStack buildHead(String owner, int quantity, String name, String[] lores) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, quantity, (short) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setDisplayName(name);
        if (lores != null)
            meta.setLore(Arrays.asList(lores));
        meta.setOwner(owner);
        stack.setItemMeta(meta);
        return stack;
    }

    private void loadItems(Player player) {
        this.items.put(2, buildItemStack(Material.SULPHUR, 1, 0, "§1Poudre magique", new String[]{"§7\u25B6 Vous permet de masquer les autres joueurs."}));
        this.items.put(4, buildItemStack(Material.COMPASS, 1, 0, "§1Menu principal", new String[]{"§7\u25B6 Affiche les modes de jeux et autres hubs."}));
        this.items.put(6, buildItemStack(Material.NETHER_STAR, 1, 0, "§1Cosmétiques", new String[]{"§7\u25B6 Utilisez une monture ou des particules !"}));
        this.items.put(19, buildHead(player.getName(), 1, "§1Profil : §7" + player.getName(), new String[]{"§6Temps de jeu : §9" + getTimePlayed(player.getUniqueId()), "§6Grade : " + EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getName()}));
        this.items.put(25, buildItemStack(Material.ENCHANTMENT_TABLE, 1, 0, "§9Amis", new String[]{"§7\u25B6 Gestion des amis."}));

    }

    public void setInventoryToPlayer(Player player) {
        loadItems(player);
        Iterator var2 = this.items.keySet().iterator();

        while (var2.hasNext()) {
            int slot = (Integer) var2.next();
            player.getInventory().setItem(slot, this.items.get(slot));
        }

    }

    private void hideOrShowPlayers(Player target) {
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

        target.playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
    }

    private boolean isHinding(Player player) {
        return this.hide.containsKey(player);
    }

    private String getTimePlayed(UUID uuid) {
        float time = EuphalysApi.getInstance().getPlayer(uuid).getTimePlayed() + 0L;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df;
        if(time >= 86400000)
            df = new SimpleDateFormat("DD HH:mm:ss");
        else
            df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(timeZone);
        return df.format((EuphalysApi.getInstance().getPlayer(uuid).getTimePlayed() + 0L)/1000);
    }
}
