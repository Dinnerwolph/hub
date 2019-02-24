package net.euphalys.hub.gui.main;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import net.euphalys.hub.gui.main.game.GuiShatteredSpace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Dinnerwolph
 */

public class GuiMain extends AbstractGui {

    public GuiMain(Hub hub) {
        super(hub);
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 9 * 6, "Menu Principal");
        this.setSlotData("Build", Material.GRASS, 12, new String[]{}, "build");
        this.setSlotData(ChatColor.GOLD + "Changer de hub", Material.ENDER_CHEST, 35, makeButtonLore(new String[]{"Cliquez pour ouvrir l'interface"}, true, false), "switch_hub");
        if (EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getGroupId() >= 50)
            this.setSlotData(ChatColor.BLUE + "Shattered Space", Material.DIAMOND_SWORD, 10, makeButtonLore(new String[]{"", "", "Développeur: Dinnerwolph, trollgun", "GameDesign : FlameOfChange", "Version Minecraft : §b1.9 - 1.13 ", "§cJeu en test interne."}, true, false), "shattered");
        this.setBackSlot();
        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.equals("switch_hub"))
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, 1));
        else if (action.equals("build"))
            EuphalysApi.getInstance().getPlayer(player.getUniqueId()).sendToServer("buildcore");
        else if (action.equals("back"))
            player.closeInventory();
        else if (action.equals("shattered"))
            this.hub.getGuiManager().openGui(player, new GuiShatteredSpace(this.hub, 1));

    }
}
