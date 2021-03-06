package net.euphalys.hub.listener.servers;

import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.event.ServerUpdateEvent;
import fr.dinnerwolph.otl.bukkit.server.Server;
import net.euphalys.api.utils.IScoreboardSign;
import net.euphalys.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Dinnerwolph
 */
public class Update implements Listener {

    @EventHandler
    public void onServerUpdate(final ServerUpdateEvent event) {
        IScoreboardSign sign;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sign = Hub.getInstance().scoreboardSignMap.get(onlinePlayer.getUniqueId());
            if (sign == null) {
                System.out.println("new scoreboardsing " + onlinePlayer);
            } else
                sign.setLine(10, "§7⋙ Connectés : §c" + NumberOfPlayer());
        }
    }

    private int NumberOfPlayer() {
        int count = 0;
        for (Server serv : BukkitOTL.getInstance().serverList.values()) {
            count += serv.getOnlineAmount();
        }
        return count;
    }
}
