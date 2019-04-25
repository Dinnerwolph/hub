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
        ItemStack playerHead;
        Material confirm = net.euphalys.api.utils.Material.LIME_TERRACOTTA.getBukkitMaterial();
        Material cancel = net.euphalys.api.utils.Material.RED_TERRACOTTA.getBukkitMaterial();
        this.inventory = this.hub.getServer().createInventory(null, 9 * 3, "Menu des Amis - retirer " + playerName);
        if (EuphalysApi.getInstance().is1_14()) {
            playerHead = new ItemStack(net.euphalys.api.utils.Material.PLAYER_HEAD.getBukkitMaterial());
        } else {
            playerHead = new ItemStack(net.euphalys.api.utils.Material.PLAYER_HEAD.getBukkitMaterial(), 1, (short) 3);
        }
        this.setHead(inventory, playerName, "§7" + playerName, playerHead, 13, new String[]{"Confirmer le retrait"}, "");
        this.setSlotData(ChatColor.GREEN + "Confirmer ✔", confirm, 11, null, "CONFIRM");
        this.setSlotData(ChatColor.RED + "Annuler ✖", cancel, 15, null, "CANCEL");
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
