package net.euphalys.hub.gui.main.friends;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import net.euphalys.hub.gui.main.GuiFriends;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiFriendsRemove extends AbstractGui {

    private final String playerName;

    public GuiFriendsRemove(Hub hub, String playerName) {
        super(hub);
        this.playerName = playerName;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 9 * 3, "Menu des Amis - retirer " + playerName);
        this.setHead(inventory, playerName, "§7" + playerName, new ItemStack(Material.SKULL_ITEM, 1, (short) 3), 13, new String[]{"Confirmer le retrait"}, "");
        this.setSlotData(ChatColor.GREEN + "Confirmer ✔", Material.STAINED_CLAY, 11, null, "CONFIRM");
        this.setSlotData(ChatColor.RED + "Annuler ✖", Material.STAINED_CLAY, 15, null, "CANCEL");
        player.openInventory(inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.equals("CANCEL"))
            this.hub.getGuiManager().openGui(player, new GuiFriends(this.hub, 1));
        else if (action.equals("CONFIRM")) {
            IEuphalysPlayer euphaPlayer = EuphalysApi.getInstance().getPlayer(playerName);
            EuphalysApi.getInstance().getFriendsManager().removeFriend(EuphalysApi.getInstance().getPlayer(player.getUniqueId()), euphaPlayer);
            this.hub.getGuiManager().openGui(player, new GuiFriends(this.hub, 1));
        }
    }
}
