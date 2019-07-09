package net.euphalys.hub.managers;

import net.euphalys.hub.Hub;
import net.euphalys.hub.gui.AbstractGui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Dinnerwolph
 */

public class GuiManager extends AbstractManager {
    private final ConcurrentMap<UUID, AbstractGui> playersGui = new ConcurrentHashMap();

    public GuiManager(final Hub hub) {
        super(hub);
    }

    public void onDisable() {
        this.hub.getServer().getOnlinePlayers().forEach(this::onLogout);
    }

    public void onLogin(final Player player) {
    }

    public void onLogout(final Player player) {
        if (this.playersGui.containsKey(player.getUniqueId())) {
            ((AbstractGui) this.playersGui.get(player.getUniqueId())).onClose(player);
            this.playersGui.remove(player.getUniqueId());
        }

    }

    public void openGui(final Player player, final AbstractGui gui) {
        if (this.playersGui.containsKey(player.getUniqueId()))
            closeGui(player);

        this.playersGui.put(player.getUniqueId(), gui);
        gui.display(player);
    }

    public void closeGui(final Player player) {
        player.closeInventory();
        this.playersGui.remove(player.getUniqueId());
    }

    public void removeClosedGui(final Player player) {
        if (this.playersGui.containsKey(player.getUniqueId())) {
            this.getPlayerGui(player).onClose(player);
            this.playersGui.remove(player.getUniqueId());
        }

    }

    public AbstractGui getPlayerGui(final HumanEntity humanEntity) {
        return this.getPlayerGui(humanEntity.getUniqueId());
    }

    public AbstractGui getPlayerGui(final UUID uuid) {
        if (playersGui.containsKey(uuid))
            return this.playersGui.get(uuid);
        return null;
    }

    public ConcurrentHashMap<UUID, AbstractGui> getPlayersGui() {
        return (ConcurrentHashMap) this.playersGui;
    }
}
