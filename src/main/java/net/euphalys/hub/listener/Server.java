package net.euphalys.hub.listener;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author Dinnerwolph
 */

public class Server implements Listener {

    @EventHandler
    public void onWeatherChanger(final WeatherChangeEvent event) {
        World world = event.getWorld();
        if (world.hasStorm()) {
            world.setWeatherDuration(0);
        }

    }
}
