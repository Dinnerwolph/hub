package net.euphalys.hub.listener.servers;

import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.event.ServerUpdateEvent;
import fr.dinnerwolph.otl.bukkit.server.Server;
import net.euphalys.api.utils.IScoreboardSign;
import net.euphalys.core.api.EuphalysApi;
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
    public void onServerUpdate(ServerUpdateEvent event) {
        IScoreboardSign sign;
        System.out.println(Hub.getInstance().scoreboardSignMap.keySet().toString());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sign = Hub.getInstance().scoreboardSignMap.get(onlinePlayer);
            if (sign == null) {
                System.out.println("new scoreboardsing " + onlinePlayer);
            } else
                sign.setLine(11, "§7⋙ §9Connectés : §b" + NumberOfPlayer());
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
