package net.euphalys.hub.gui.main;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import net.euphalys.hub.gui.main.friends.GuiFriendsRemove;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiFriends extends AbstractGui {

    private final int page;

    public GuiFriends(Hub hub, int page) {
        super(hub);
        this.page = page;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 9 * 3, "Menu des Amis (Page " + this.page + ")");
        update(player);
        player.openInventory(inventory);
    }

    @Override
    public void update(Player player) {
        this.inventory.clear();
        this.setBackSlot();
        int line = 0, slot = 0, i = 0;
        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        boolean more = false;
        IEuphalysPlayer euphaPlayer = EuphalysApi.getInstance().getPlayer(player.getUniqueId());
        for (int number = 0; number < euphaPlayer.getFriends().size(); number++) {
            if (i < (7 * (this.page - 1))) {
                i++;
                continue;
            } else if (i > ((7 * (this.page * 1) + (baseSlots.length * 3)))) {
                more = true;
                break;
            }
            String targetName = euphaPlayer.getFriends().get(number);
            IEuphalysPlayer target = EuphalysApi.getInstance().getPlayer(targetName);
            this.setHead(inventory, targetName, "§7" + targetName, new ItemStack(Material.SKULL_ITEM, 1, (short) 3),
                    baseSlots[slot] + (9 * line), new String[]{"§6Statut : §7" +
                            (target.isOnline() ? "§aConnecté ✔" : "§cDéconnecté ✖"), "§6Grade : §7" + target.getGroup().getName(),
                            "Clic-droit pour retirer des amis.", (target.isOnline() ? "Clic-gauche pour rejoindre votre amis." : "")}, (target.isOnline() ? "JOIN_" + euphaPlayer.getFriends().get(number) : ""),
                    "", "REMOVE_" + euphaPlayer.getFriends().get(number));
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
        if (action.equals("page_back"))
            this.hub.getGuiManager().openGui(player, new GuiFriends(this.hub, (this.page - 1)));
        else if (action.equals("page_next"))
            this.hub.getGuiManager().openGui(player, new GuiFriends(this.hub, (this.page + 1)));
        else if (action.equals("back"))
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
        else if (action.startsWith("REMOVE_") && clickType == ClickType.RIGHT) {
            action = action.replace("REMOVE_", "");
            this.hub.getGuiManager().openGui(player, new GuiFriendsRemove(this.hub, action));
        } else if (action.startsWith("JOIN_"))
            EuphalysApi.getInstance().getPlayer(player.getUniqueId()).sendToServer(EuphalysApi.getInstance().getPlayer(action.replace("JOIN_", "")).getServer());
    }
}
