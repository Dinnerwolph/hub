package net.euphalys.hub.listener;

import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author Dinnerwolph
 */

public class Gui implements Listener {

    private Hub hub;

    public Gui(Hub hub) {
        this.hub = hub;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            AbstractGui currentGui = this.hub.getGuiManager().getPlayerGui(player);
            if (currentGui != null) {
                if (event.getClickedInventory() instanceof PlayerInventory)
                    this.hub.getPlayerManager().getStaticInventory().doInteraction(player, event.getCurrentItem());

                String action = currentGui.getAction(event.getSlot(), event.getClick());
                if (action != null)
                    currentGui.onClick(player, event.getCurrentItem(), action, event.getClick());
                event.setCancelled(true);
            } else if (event.getClickedInventory() instanceof PlayerInventory)
                if (this.hub.getPlayerManager().getStaticInventory().doInteraction(player, event.getCurrentItem()))
                    event.setCancelled(true);

        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (this.hub.getGuiManager().getPlayerGui(event.getPlayer()) != null) {
            this.hub.getGuiManager().removeClosedGui((Player) event.getPlayer());
        }

    }
}
