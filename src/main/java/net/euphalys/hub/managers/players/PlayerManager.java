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
    private boolean canBuild;

    public PlayerManager(Hub hub) {
        super(hub);
        this.staticInventory = new StaticInventory(hub);
        this.canBuild = false;
    }

    public void onDisable() {
    }

    public void onLogin(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.setWalkSpeed(0.2F);
        player.setAllowFlight(false);
        player.setFoodLevel(20);
        player.setHealth(20.0D);
        player.getInventory().clear();
        player.teleport(getSpawn());
        player.sendTitle("§eBon jeu sur EpyCube !", "§f§l" + player.getName());
        this.staticInventory.setInventoryToPlayer(player);
    }

    public void onLogout(Player player) {
    }

    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    public Location getSpawn() {
        return new Location(Bukkit.getWorlds().get(0), -15.5, 33.5, 88.5);
    }

    public StaticInventory getStaticInventory() {
        return this.staticInventory;
    }

    public boolean canBuild() {
        return this.canBuild;
    }
}
