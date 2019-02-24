package net.euphalys.hub.gui.main;

import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.server.Server;
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
import us.myles.ViaVersion.api.Via;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */

public class GuiSwitchHub extends AbstractGui {

    private final int page;
    private final int version[] = {47, 110, 340, 404};
    private final String server[] = {"Hub1-8", "Hub1-9", "Hub1-12", "Hub1-13"};

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
        int playerVer = Via.getAPI().getPlayerVersion(player);
        int count = -1;
        for (int i : version)
            if (playerVer >= i)
                count++;
        List<Server> servers = new ArrayList();
        for (String s : BukkitOTL.getInstance().serverList.keySet())
            for (int i = 0; i < count + 1; i++)
                if (BukkitOTL.getInstance().serverList.get(s).getServerName().startsWith(server[i]))
                    servers.add(BukkitOTL.getInstance().serverList.get(s));


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
        Map<Integer, IGroup> groupMap = new ConcurrentHashMap<>();
        try {
            groups.keySet().forEach((server) -> {
                IGroup group = EuphalysApi.getInstance().getGroup(Integer.parseInt(server));
                groupMap.put(group.getGroupId(), group);
            });
            for (int i = 100; i > 0; i--)
                if (groupMap.get(i) != null)
                    lore.add(groupMap.get(i).getName() + " §r: " + groups.get(String.valueOf(i)));
        } catch (NullPointerException e) {

        }

        lore.add(ChatColor.DARK_GRAY + "▶ Cliquez pour être téléporté");
        meta.setLore(lore);
        glass.setItemMeta(meta);
        return glass;
    }
}
