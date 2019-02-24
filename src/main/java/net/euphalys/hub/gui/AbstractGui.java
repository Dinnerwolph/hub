package net.euphalys.hub.gui;

import net.euphalys.core.api.utils.Pair;
import net.euphalys.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */

public abstract class AbstractGui {
    protected Map<Pair<ClickType, Integer>, String> actions = new ConcurrentHashMap<>();
    protected Inventory inventory;
    protected final Hub hub;

    public AbstractGui(Hub hub) {
    this.hub = hub;
    }

    public abstract void display(Player player);

    public void update(Player player) {
    }

    public void onClose(Player player) {
    }

    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
    }

    public void setSlotData(Inventory inv, String name, Material material, int slot, String[] description, String action) {
        this.setSlotData(inv, name, new ItemStack(material, 1), slot, description, action);
    }

    public void setSlotData(String name, Material material, int slot, String[] description, String action) {
        this.setSlotData(this.inventory, name, new ItemStack(material, 1), slot, description, action);
    }

    public void setSlotData(String name, ItemStack item, int slot, String[] description, String action) {
        this.setSlotData(this.inventory, name, item, slot, description, action);
    }

    public void setSlotData(Inventory inv, String name, ItemStack item, int slot, String[] description, String action) {
        setSlotData(inv, name, item, slot, description, action, action, action);
    }

    public void setSlotData(Inventory inv, String name, ItemStack item, int slot, String[] description, String leftClick, String middleClick, String rightClick) {
        this.actions.put(new Pair<>(ClickType.LEFT, slot), leftClick);
        this.actions.put(new Pair<>(ClickType.MIDDLE, slot), middleClick);
        this.actions.put(new Pair<>(ClickType.RIGHT, slot), rightClick);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (description != null)
            meta.setLore(Arrays.asList(description));

        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    protected void setHead(Inventory inv, String owner, String name, ItemStack item, int slot, String[] description, String action) {
        setHead(inv, owner, name, item, slot, description, action, action, action);
    }

    protected void setHead(Inventory inv, String owner, String name, ItemStack item, int slot, String[] description, String leftClick, String middleClick, String rightClikc) {
        this.actions.put(new Pair<>(ClickType.LEFT, slot), leftClick);
        this.actions.put(new Pair<>(ClickType.MIDDLE, slot), middleClick);
        this.actions.put(new Pair<>(ClickType.RIGHT, slot), rightClikc);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(name);
        if (description != null)
            meta.setLore(Arrays.asList(description));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    public void setSlotData(Inventory inv, ItemStack item, int slot, String action) {
        this.actions.put(new Pair<>(ClickType.LEFT, slot), action);
        inv.setItem(slot, item);
    }

    public void setSlotData(ItemStack item, int slot, String action) {
        this.setSlotData(this.inventory, item, slot, action);
    }

    public void setBackSlot() {
        this.setSlotData(this.inventory, getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    public String getAction(int slot, ClickType type) {
        String action = "";
        for (Pair<ClickType, Integer> pair : actions.keySet())
            if (pair.getKey() == type && pair.getValue() == slot)
                action = this.actions.get(pair);
        return action;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    protected static ItemStack getBackIcon() {
        ItemStack stack = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("« Retour");
        stack.setItemMeta(meta);
        return stack;
    }

    protected static String[] makeButtonLore(String[] description, boolean clickOpen, boolean clickTeleport) {
        List<String> lore = new ArrayList<>();
        String[] loreArray = new String[]{};

        if (description != null) {
            for (String string : description)
                lore.add(ChatColor.GRAY + string);

            if (clickOpen || clickTeleport)
                lore.add("");
        }

        if (clickOpen)
            lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour ouvrir le menu");

        if (clickTeleport)
            lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour être téléporté");

        return lore.toArray(loreArray);
    }
}
