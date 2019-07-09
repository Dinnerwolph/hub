package net.euphalys.hub.gui;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.server.Server;
import net.euphalys.api.player.IGroup;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.main.GuiMain;
import net.euphalys.hub.gui.main.game.GuiShatteredSpace;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.myles.ViaVersion.util.ConcurrentList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dinnerwolph
 */
public abstract class PageGui extends AbstractGui {

    protected final int page;
    private final String template;

    public PageGui(final Hub hub, final int page, final String template) {
        super(hub);
        this.page = page;
        this.template = template;
    }

    @Override
    public void update(final Player player) {
        final List<Server> servers = new ConcurrentList<>();

        BukkitOTL.getInstance().serverList.forEach((servername, server) -> {
            if (servername.startsWith(template))
                if (server.getStatus().equals("WAITING"))
                    servers.add(server);
        });
        final int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int line = 0, slot = 0, i = 0;
        this.inventory.clear();
        this.setBackSlot();
        boolean more = false;
        for (final Server server : servers) {
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
    public void onClick(final Player player, final ItemStack stack, final String action, final ClickType clickType) {
        if (action.startsWith(template)) {
            if (BukkitOTL.getInstance().serverList.get(action).getOnlineAmount() >= BukkitOTL.getInstance().serverList.get(action).getMaxAmount()) {
                player.sendMessage(ChatColor.RED + "Ce Hub est plein, vous ne pouvez pas y aller.");
                return;
            }
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(action);
            player.sendPluginMessage(this.hub, "BungeeCord", out.toByteArray());
        } else if (action.equals("page_back"))
            this.hub.getGuiManager().openGui(player, new GuiShatteredSpace(this.hub, (this.page - 1)));
        else if (action.equals("page_next"))
            this.hub.getGuiManager().openGui(player, new GuiShatteredSpace(this.hub, (this.page + 1)));
        else if (action.equals("back"))
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
    }

    private ItemStack getHubItem(final Server hub) {
        ItemStack glass;
        final String baseName = "ShatteredSpace " + hub.getServerName().replace(template, "") + " (" + hub.getOnlineAmount() + " joueur" + (hub.getOnlineAmount() > 1 ? "s" : "") + ")";
        if (hub.getOnlineAmount() <= 15)
            glass = getGlass(net.euphalys.api.utils.Material.LIME_STAINED_GLASS.getBukkitMaterial(), DyeColor.GREEN, "§a" + baseName);
        else if (hub.getOnlineAmount() <= 30)
            glass = getGlass(net.euphalys.api.utils.Material.ORANGE_STAINED_GLASS.getBukkitMaterial(), DyeColor.ORANGE, "§6" + baseName);
        else if (hub.getOnlineAmount() <= 40)
            glass = getGlass(net.euphalys.api.utils.Material.RED_STAINED_GLASS.getBukkitMaterial(), DyeColor.RED, "§c" + baseName);
        else
            glass = getGlass(net.euphalys.api.utils.Material.ORANGE_STAINED_GLASS.getBukkitMaterial(), DyeColor.ORANGE, "§6" + baseName);
        final ItemMeta meta = glass.getItemMeta();
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

    private ItemStack getGlass(final Material material, final DyeColor color, final String name) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setDurability(color.getWoolData());
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
