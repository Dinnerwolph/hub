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
import us.myles.ViaVersion.util.ConcurrentList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */

public abstract class AbstractGui {
    protected Map<Pair<ClickType, Integer>, String> actions = new ConcurrentHashMap<>();
    protected Inventory inventory;
    protected final Hub hub;

    public AbstractGui(final Hub hub) {
        this.hub = hub;
    }

    public abstract void display(Player player);

    public void update(final Player player) {
    }

    public void onClose(final Player player) {
    }

    public void onClick(final Player player, final ItemStack stack, final String action, final ClickType clickType) {
    }

    public void setSlotData(final Inventory inv, final String name, final Material material, final int slot, final String[] description, final String action) {
        this.setSlotData(inv, name, new ItemStack(material, 1), slot, description, action);
    }

    public void setSlotData(final String name, final Material material, final int slot, final String[] description, final String action) {
        this.setSlotData(this.inventory, name, new ItemStack(material, 1), slot, description, action);
    }

    public void setSlotData(final String name, final ItemStack item, final int slot, final String[] description, final String action) {
        this.setSlotData(this.inventory, name, item, slot, description, action);
    }

    public void setSlotData(final Inventory inv, final String name, final ItemStack item, final int slot, final String[] description, final String action) {
        setSlotData(inv, name, item, slot, description, action, action, action);
    }

    public void setSlotData(final Inventory inv, final String name, final ItemStack item, final int slot, final String[] description, final String leftClick, final String middleClick, final String rightClick) {
        this.actions.put(new Pair<>(ClickType.LEFT, slot), leftClick);
        this.actions.put(new Pair<>(ClickType.MIDDLE, slot), middleClick);
        this.actions.put(new Pair<>(ClickType.RIGHT, slot), rightClick);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (description != null)
            meta.setLore(Arrays.asList(description));

        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    protected void setHead(final Inventory inv, final String owner, final String name, final ItemStack item, final int slot, final String[] description, final String action) {
        setHead(inv, owner, name, item, slot, description, action, action, action);
    }

    protected void setHead(final Inventory inv, final String owner, final String name, ItemStack item, final int slot, final String[] description, final String leftClick, final String middleClick, final String rightClikc) {
        this.actions.put(new Pair<>(ClickType.LEFT, slot), leftClick);
        this.actions.put(new Pair<>(ClickType.MIDDLE, slot), middleClick);
        this.actions.put(new Pair<>(ClickType.RIGHT, slot), rightClikc);
        SkullMeta meta;
        if (hub.skullCache.containsKey(owner)) {
            item = hub.skullCache.get(owner);
            meta = (SkullMeta) item.getItemMeta();
        } else {
            meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(owner);
            item.setItemMeta(meta);
            hub.skullCache.put(owner, item);
        }
        meta.setDisplayName(name);
        if (description != null)
            meta.setLore(Arrays.asList(description));
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    public void setSlotData(final Inventory inv, final ItemStack item, final int slot, final String action) {
        this.actions.put(new Pair<>(ClickType.LEFT, slot), action);
        inv.setItem(slot, item);
    }

    public void setSlotData(final ItemStack item, final int slot, final String action) {
        this.setSlotData(this.inventory, item, slot, action);
    }

    public void setBackSlot() {
        this.setSlotData(this.inventory, getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    public String getAction(final int slot, final ClickType type) {
        String action = "";
        for (final Pair<ClickType, Integer> pair : actions.keySet())
            if (pair.getKey() == type && pair.getValue() == slot)
                action = this.actions.get(pair);
        return action;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    protected static ItemStack getBackIcon() {
        final ItemStack stack = new ItemStack(Material.EMERALD, 1);
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("« Retour");
        stack.setItemMeta(meta);
        return stack;
    }

    protected static String[] makeButtonLore(final String[] description, final boolean clickOpen, final boolean clickTeleport) {
        final List<String> lore = new ConcurrentList<>();
        final String[] loreArray = new String[]{};

        if (description != null) {
            for (final String string : description)
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
