package net.euphalys.hub.gui.main.game;

import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.PageGui;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class GuiShatteredSpace extends PageGui {

    public GuiShatteredSpace(final Hub hub, final int page) {
        super(hub, page, "shsp_preview");
    }

    @Override
    public void display(final Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Choisir un serveur (Page " + this.page + ")");
        this.update(player);
        player.openInventory(this.inventory);
    }
}
