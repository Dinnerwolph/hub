package net.euphalys.hub.gui.main;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinnerwolph
 */

public class GuiMenuPrincipale extends AbstractGui {
    public GuiMenuPrincipale(Hub hub) {
        super(hub);
    }

    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory((InventoryHolder) null, 54, "Menu Principal");
        this.setSlotData(" §c§l* POPULAIRE * §6Factions", Material.DIAMOND_CHESTPLATE, 13, makeButtonLore(new String[]{""}, false, true), "faction");
        this.setSlotData("§6Minage", Material.DIAMOND_PICKAXE, 21, makeButtonLore(new String[]{""}, false, true), "minage1");
        this.setSlotData("§6PvPBox", Material.DIAMOND_SWORD, 23, makeButtonLore(new String[]{""}, false, true), "pvpbox");
        this.setSlotData("§6Votre profile", this.getPlayerHead(player), 18, makeButtonLore(new String[]{""}, true, false), "parkour");
        this.setSlotData("§6Vos amis", Material.FEATHER, 27, makeButtonLore(new String[]{""}, true, false), "parkour");
        this.setSlotData("§6Spawn", Material.BED, 26, makeButtonLore(new String[]{""}, false, true), "spawn");
        this.setSlotData("§6Jump", Material.FEATHER, 35, makeButtonLore(new String[]{"", "En espérant que vous atteignez le", "paradis...", ""}, false, true), "parkour");
        this.setSlotData("§6Fermer", Material.ARROW, 49, makeButtonLore((String[]) null, false, false), "parkour");
        this.setSlotData("Changer de hub", Material.ENDER_CHEST, 36, makeButtonLore(new String[]{"Cliquez pour ouvrir l'interface"}, true, false), "switch_hub");
        player.openInventory(this.inventory);
    }

    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.equals("spawn")) {
            player.teleport(new Location(Bukkit.getWorld("world"), 0.5D, 63.0D, 0.5D));
        } else {
            if (action.equals("switch_hub")) {
                this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, 1));
            }
            ByteArrayDataOutput out;
            if (action.equals("faction")) {
                out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(action);
                player.sendPluginMessage(this.hub, "BungeeCord", out.toByteArray());
            } else if (action.equals("minage1")) {
                out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(action);
                player.sendPluginMessage(this.hub, "BungeeCord", out.toByteArray());
            } else if (action.equals("pvpbox")) {
                out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(action);
                player.sendPluginMessage(this.hub, "BungeeCord", out.toByteArray());
            }
        }

    }

    protected static String[] makeButtonLore(String[] description, boolean clickOpen, boolean clickTeleport) {
        List<String> lore = new ArrayList();
        String[] loreArray = new String[0];
        if (description != null) {
            String[] var5 = description;
            int var6 = description.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String strings = var5[var7];
                lore.add("§f" + strings);
            }
        }

        if (clickOpen) {
            lore.add("§e▶ Clique pour ouvrir le menu !");
        } else if (clickTeleport) {
            lore.add("§e▶ Clique pour être téléporté !");
        }

        return (String[]) lore.toArray(loreArray);
    }

    private ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        skull.setItemMeta(meta);
        return skull;
    }
}
