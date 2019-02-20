package net.euphalys.hub.gui.main;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.server.Server;
import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.player.IGroup;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dinnerwolph
 */

public class GuiSwitchHub extends AbstractGui {

    private final int page;

    GuiSwitchHub(Hub hub, int page) {
        super(hub);
        this.page = page;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Changez de hub (Page " + this.page + ")");
        this.update(player);
        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player) {
        List<Server> servers = new ArrayList();
        BukkitOTL.getInstance().serverList.forEach((servername, server) -> {
            if (servername.contains("Hub"))
                servers.add(server);
        });
        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int line = 0, slot = 0, i = 0;
        this.inventory.clear();
        this.setBackSlot();
        boolean more = false;
        for (Server server : servers) {
            if (i < (7 * (this.page - 1))) {
                i++;
                continue;
            } else if (i > ((7 * (this.page - 1) + (baseSlots.length * 3)))) {
                more = true;
                break;
            }

            this.setSlotData(this.getHubItem(server), baseSlots[slot] + (9 * line), server.getServerName());
            slot++;

            if (slot == baseSlots.length) {
                slot = 0;
                line++;
            }
            i++;
        }

        if (this.page > 1)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page - 1), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if (more)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 1) + " »", Material.PAPER, this.inventory.getSize() - 1, null, "page_next");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.startsWith("Hub")) {
            if (action.equals(EuphalysApi.getInstance().getSProperty("name"))) {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas aller sur votre hub actuel !");
                return;
            }
            if (BukkitOTL.getInstance().serverList.get(action).getOnlineAmount() >= BukkitOTL.getInstance().serverList.get(action).getMaxAmount()) {
                player.sendMessage(ChatColor.RED + "Ce Hub est plein, vous ne pouvez pas y aller.");
                return;
            }
            EuphalysApi.getInstance().getPlayer(player.getUniqueId()).sendToServer(action);
        } else if (action.equals("page_back"))
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, (this.page - 1)));
        else if (action.equals("page_next"))
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, (this.page + 1)));
        else if (action.equals("back"))
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
    }

    private ItemStack getHubItem(Server hub) {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS, 1);
        ItemMeta meta = glass.getItemMeta();
        String baseName = "Hub " + hub.getServerName().replace("Hub", "") + " (" + hub.getOnlineAmount() + " joueur" + (hub.getOnlineAmount() > 1 ? "s" : "") + ")";
        if (hub.getOnlineAmount() <= 15) {
            glass.setDurability(DyeColor.GREEN.getWoolData());
            meta.setDisplayName(ChatColor.GREEN + baseName);
        } else if (hub.getOnlineAmount() <= 30) {
            glass.setDurability(DyeColor.YELLOW.getWoolData());
            meta.setDisplayName(ChatColor.YELLOW + baseName);
        } else if (hub.getOnlineAmount() <= 40) {
            glass.setDurability(DyeColor.RED.getWoolData());
            meta.setDisplayName(ChatColor.RED + baseName);
        } else {
            glass.setDurability(DyeColor.YELLOW.getWoolData());
            meta.setDisplayName(ChatColor.YELLOW + baseName);
        }

        List<String> lore = new ArrayList();
        Map<String, String> groups = BukkitOTL.getInstance().hubgroup.get(hub.getServerName());
        try {
            for (String s : groups.keySet()) {
                IGroup group = EuphalysApi.getInstance().getGroup(Integer.parseInt(s));
                lore.add(group.getName() + " §r: " + groups.get(s));
            }
        } catch (Exception e) {

        }

        lore.add(ChatColor.DARK_GRAY + "▶ Cliquez pour être téléporté");
        meta.setLore(lore);
        glass.setItemMeta(meta);
        return glass;
    }
}
