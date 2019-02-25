package net.euphalys.hub.listener;

import net.euphalys.hub.Hub;
import net.euphalys.hub.listener.player.Join;
import net.euphalys.hub.listener.servers.Update;
import org.bukkit.plugin.PluginManager;

/**
 * @author Dinnerwolph
 */

public class ListenerManager {

    Hub hub = Hub.getInstance();

    public ListenerManager() {
        init();
    }

    private void init() {
        PluginManager pm = hub.getServer().getPluginManager();
        pm.registerEvents(new Join(), hub);
        pm.registerEvents(new Update(), hub);
        pm.registerEvents(new Gui(hub), hub);
        pm.registerEvents(new Server(), hub);
    }
}
