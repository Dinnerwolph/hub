package net.euphalys.hub.commands;

import com.mojang.authlib.GameProfile;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.test.CustomPlayer;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */
public class Test implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (EuphalysApi.getInstance().is1_14())
            spawnNpc();
        return false;
    }

    public void spawnNpc() {
        final WorldServer s = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        final CustomPlayer player = new CustomPlayer(MinecraftServer.getServer(), s, new GameProfile(UUID.fromString("e7c79742-f3f5-4b33-aa86-9df4922ff0b6"), "NickiMinaj"), new PlayerInteractManager(s));
        final Location loc = new Location(s.getWorld(), -15.5, 12.5, 88.5);
        player.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        final PacketPlayOutPlayerInfo pi = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, player);
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(player);
        for (final Player p : Bukkit.getOnlinePlayers()) {
            final PlayerConnection co = ((CraftPlayer) p).getHandle().playerConnection;
            co.sendPacket(pi);
            co.sendPacket(spawn);
        }
    }
}
