package net.euphalys.hub.listener.player;

import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.server.Server;
import net.euphalys.api.utils.IScoreboardSign;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Dinnerwolph
 */

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("euphalys.joinmessage"))
            event.setJoinMessage(player.getDisplayName() +" §7§oa rejoint le serveur !");
        else
            event.setJoinMessage("");
        IScoreboardSign sign = EuphalysApi.getInstance().newScoreboardSign(player, "§2Euphalys");
        sign.create();
        sign.setLine(8, "§9Bienvenue, §b" + player.getName());
        sign.setLine(9, "§1");
        sign.setLine(10, "§7⋙ §9Serveur : §b" + EuphalysApi.getInstance().getSProperty("name"));
        sign.setLine(11, "§7⋙ §9Connectés : §b" + NumberOfPlayer());
        sign.setLine(12, "§7⋙ §9Grade : " + EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getScore());
        sign.setLine(13, "§3");
        Hub.getInstance().scoreboardSignMap.put(player, sign);
        Hub.getInstance().getEventBus().onLogin(player);
        Hub.getInstance().sendInformation();
    }

    private int NumberOfPlayer() {
        int count = 0;
        for (Server serv : BukkitOTL.getInstance().serverList.values()) {
            count += serv.getOnlineAmount();
        }
        return count;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        event.setCancelled(true);
        if (entity.getType() == EntityType.PLAYER) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                entity.teleport(new Location(Bukkit.getWorlds().get(0), 0.5, 33.5, 0.5));
            }

        }
    }


    @EventHandler
    public void onEntityDamageByOther(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack currentItem = event.getItem();
        if (currentItem != null) {
            Hub.getInstance().getPlayerManager().getStaticInventory().doInteraction(player, currentItem);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), new Runnable() {
            @Override
            public void run() {
                Hub.getInstance().sendInformation();
            }
        }, 20 * 2);
        Hub.getInstance().scoreboardSignMap.get(event.getPlayer()).destroy();
        Hub.getInstance().scoreboardSignMap.remove(event.getPlayer());
    }
}
