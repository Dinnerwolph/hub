package net.euphalys.hub.managers;

import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public interface EntryPoints {

    void onDisable();

    void onLogin(Player player);

    void onLogout(Player player);
}
