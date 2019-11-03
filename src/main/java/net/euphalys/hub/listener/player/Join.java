package net.euphalys.hub.listener.player;

import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import fr.dinnerwolph.otl.bukkit.server.Server;
import net.euphalys.api.player.IEuphalysPlayer;
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
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IEuphalysPlayer euphaPlayer = EuphalysApi.getInstance().getPlayer(player.getUniqueId());
        if (!player.hasPermission("euphalys.joinmessage") || euphaPlayer.isVanished() || euphaPlayer.hasNickName())
            event.setJoinMessage("");
        else
            event.setJoinMessage(player.getDisplayName() + " §6§oa rejoint le serveur !");

        IScoreboardSign sign = EuphalysApi.getInstance().newScoreboardSign(player, "§2EUPHALYS.NET");
        sign.create();
        sign.setLine(8, "§7Bienvenue, §b" + euphaPlayer.getName());
        sign.setLine(9, "§1");
        sign.setLine(10, "§7⋙ Serveur : §c" + EuphalysApi.getInstance().getSProperty("name"));
        sign.setLine(11, "§7⋙ Connectés : §c" + NumberOfPlayer());
        sign.setLine(12, "§7⋙ Grade : " + euphaPlayer.getRealGroup().getScore());
        sign.setLine(13, "§7⋙ Niveau : Niveau 1";
        sign.setLine(14, "§3");

        Hub.getInstance().scoreboardSignMap.put(player.getUniqueId(), sign);
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
    public void onEntityDamage(final EntityDamageEvent event) {
        Entity entity = event.getEntity();
        event.setCancelled(true);
        if (entity.getType() == EntityType.PLAYER) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                entity.teleport(new Location(Bukkit.getWorlds().get(0), 0.5, 33.5, 0.5));
            }

        }
    }


    @EventHandler
    public void onEntityDamageByOther(final EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack currentItem = event.getItem();
        if (currentItem != null) {
            Hub.getInstance().getPlayerManager().getStaticInventory().doInteraction(player, currentItem);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(final PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), new Runnable() {
            @Override
            public void run() {
                Hub.getInstance().sendInformation();
            }
        }, 20 * 2);
        Hub.getInstance().scoreboardSignMap.get(event.getPlayer().getUniqueId()).destroy();
        Hub.getInstance().scoreboardSignMap.remove(event.getPlayer().getUniqueId());
    }
}
