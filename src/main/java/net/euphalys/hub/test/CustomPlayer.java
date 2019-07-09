package net.euphalys.hub.test;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_14_R1.*;

/**
 * @author Dinnerwolph
 */
public class CustomPlayer extends EntityPlayer {

    public CustomPlayer(final MinecraftServer minecraftserver, final WorldServer worldserver, final GameProfile gameprofile, final PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
        this.playerInteractManager.breakBlock(new BlockPosition(-16, 11, 88));
    }
}
