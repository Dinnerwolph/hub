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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("euphalys.joinmessage"))
            event.setJoinMessage(player.getDisplayName() + " §7§oa rejoint le serveur !");
        else
            event.setJoinMessage("");
        IScoreboardSign sign = EuphalysApi.getInstance().newScoreboardSign(player, "§6Project EpyCube");
        sign.create();
        sign.setLine(10, "§c ★ Work In Progress ★");
        sign.setLine(11, "§6Bienvenue, §5" + player.getName());
        sign.setLine(12, "§6Serveur : §5" + EuphalysApi.getInstance().getSProperty("name"));
        sign.setLine(13, "§6Connectés :§5" + NumberOfPlayer());
        sign.setLine(14, "§6Grade :" + EuphalysApi.getInstance().getPlayer(player.getUniqueId()).getGroup().getName());
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

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(Hub.getInstance(), new Runnable() {
            @Override
            public void run() {
                Hub.getInstance().sendInformation();
            }
        }, 20 * 5);
    }
}
