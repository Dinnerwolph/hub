package net.euphalys.hub.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinnerwolph
 */

public class EventBus implements EntryPoints {

    private List<AbstractManager> managers = new ArrayList();

     public void registerManager(AbstractManager manager) {
        this.managers.add(manager);
    }

    @Override
    public void onDisable() {
        this.managers.forEach(EntryPoints::onDisable);
    }

    @Override
    public void onLogin(Player player) {
        this.managers.forEach(manager -> {
            manager.onLogin(player);
        });
    }

    @Override
    public void onLogout(Player player) {
        this.managers.forEach(manager -> {
            manager.onLogout(player);
        });
    }
}
