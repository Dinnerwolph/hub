package net.euphalys.hub.managers.players;

import net.euphalys.hub.Hub;
import net.euphalys.hub.managers.AbstractManager;
import net.euphalys.hub.managers.StaticInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class PlayerManager extends AbstractManager {

    private final StaticInventory staticInventory;
    private final Location spawn;

    public PlayerManager(final Hub hub) {
        super(hub);
        this.staticInventory = new StaticInventory(hub);
        this.spawn = new Location(Bukkit.getWorlds().get(0), 72.5, 69.5, 59.5);
    }

    public void onDisable() {
    }

    public void onLogin(final Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setWalkSpeed(0.2F);
        player.setAllowFlight(false);
        player.setFoodLevel(20);
        player.setHealth(20.0D);
        player.getInventory().clear();
        player.teleport(getSpawn());
        player.sendTitle("§eBon jeu sur Euphalys !", "§f§l" + player.getName());
        this.staticInventory.setInventoryToPlayer(player);
    }

    public void onLogout(final Player player) {
    }

    private Location getSpawn() {
        return spawn;
    }

    public StaticInventory getStaticInventory() {
        return this.staticInventory;
    }
}
