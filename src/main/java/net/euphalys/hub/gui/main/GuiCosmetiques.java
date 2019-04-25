package net.euphalys.hub.gui.main;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinnerwolph
 */

public class GuiCosmetiques extends AbstractGui {
    public GuiCosmetiques(Hub hub) {
        super(hub);
    }

    public void display(Player player) {
        Material exp_bottle = net.euphalys.api.utils.Material.EXPERIENCE_BOTTLE.getBukkitMaterial();
        ItemStack skeletonSkull;
        if (EuphalysApi.getInstance().is1_14()) {
            skeletonSkull = new ItemStack(net.euphalys.api.utils.Material.SKELETON_SKULL.getBukkitMaterial());
        } else {
            skeletonSkull = new ItemStack(net.euphalys.api.utils.Material.SKELETON_SKULL.getBukkitMaterial(), 1, (short) SkullType.SKELETON.ordinal());
        }
        this.inventory = this.hub.getServer().createInventory(null, 54, "Menu des cosmétiques");
        this.setSlotData("§6Chapeaux", Material.LEATHER_HELMET, 11, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Torses", Material.LEATHER_CHESTPLATE, 20, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Pantalons", Material.LEATHER_LEGGINGS, 29, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Chaussures", Material.LEATHER_BOOTS, 38, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Mouvements", exp_bottle, 15, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Animations", Material.BLAZE_POWDER, 24, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Montures", Material.SADDLE, 33, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§6Métamorphes", skeletonSkull, 42, makeButtonLore(new String[]{""}, true), "");
        this.setSlotData("§cFermer", Material.ARROW, 49, makeButtonLore(new String[]{""}, true), "close");
        player.openInventory(this.inventory);
    }

    private static String[] makeButtonLore(String[] description, boolean clickOpen) {
        List<String> lore = new ArrayList();
        String[] loreArray = new String[0];
        if (description != null) {
            String[] var4 = description;
            int var5 = description.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String strings = var4[var6];
                lore.add("§f" + strings);
            }
        }

        if (clickOpen) {
            lore.add("§e▶ Clique pour ouvrir le menu !");
        }

        return (String[]) lore.toArray(loreArray);
    }
}
