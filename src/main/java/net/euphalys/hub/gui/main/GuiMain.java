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

    public GuiMain(final Hub hub) {
        super(hub);
    }

    @Override
    public void display(final Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 9 * 6, "Menu Principal");
        this.setSlotData("Build", Material.GRASS, 12, new String[]{}, "build");
        this.setSlotData(ChatColor.DARK_GREEN + "Accès au serveurs de tests (Build)", Material.ENDER_PEARL, 13, new String[]{}, "buildtest");
        this.setSlotData(ChatColor.DARK_GREEN + "Projet Hub", net.euphalys.api.utils.Material.GREEN_TERRACOTTA.getBukkitMaterial(), 14, makeButtonLore(new String[]{"§cSous Whitelist. Réservé à l'équipe du Projet Hub."}, true, false), "buildhub");
        this.setSlotData(ChatColor.GOLD + "Changer de hub", Material.ENDER_CHEST, 35, makeButtonLore(new String[]{"Cliquez pour ouvrir l'interface"}, true, false), "switch_hub");
        if (player.hasPermission("euphalys.dev.alpha"))
            this.setSlotData(ChatColor.BLUE + "Shattered Space", Material.DIAMOND_SWORD, 10, makeButtonLore(new String[]{"", "", "Développeur: Dinnerwoplph", "GameDesign : FlameOfChange", "Version Minecraft : §b1.8 - 1.14 ", "§cJeu en test interne."}, true, false), "shattered");
        this.setBackSlot();
        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(final Player player, final ItemStack stack, final String action, final ClickType clickType) {
        if (action.equals("switch_hub"))
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, 1));
        else if (action.equals("build"))
            EuphalysApi.getInstance().getPlayer(player.getUniqueId()).sendToServer("buildcore");
        else if (action.equals("buildtest"))
            EuphalysApi.getInstance().getPlayer(player.getUniqueId()).sendToServer("buildtest");
        else if (action.equals("buildhub"))
            EuphalysApi.getInstance().getPlayer(player.getUniqueId()).sendToServer("buildhub");
        else if (action.equals("back"))
            player.closeInventory();
        else if (action.equals("shattered"))
            this.hub.getGuiManager().openGui(player, new GuiShatteredSpace(this.hub, 1));

    }
}
