package net.euphalys.hub;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.utils.IScoreboardSign;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.hub.listener.ListenerManager;
import net.euphalys.hub.managers.EventBus;
import net.euphalys.hub.managers.GuiManager;
import net.euphalys.hub.managers.players.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */

public class Hub extends JavaPlugin {

    private static Hub instance;
    private World world;
    private GuiManager guiManager;
    private EventBus eventBus;
    private PlayerManager playerManager;
    public Map<Player, IScoreboardSign> scoreboardSignMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        this.world = getServer().getWorlds().get(0);
        this.world.setGameRuleValue("randomTickSpeed", "0");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setTime(6000L);
        this.guiManager = new GuiManager(this);
        this.playerManager = new PlayerManager(this);
        this.eventBus = new EventBus();
        this.eventBus.registerManager(new PlayerManager(this));
        new ListenerManager();
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public static Hub getInstance() {
        return instance;
    }

    public void sendInformation() {
        Map<Integer, Integer> info = new ConcurrentHashMap<>();
        JSONObject object = new JSONObject();
        JSONObject map = new JSONObject();
        for (Player player : Bukkit.getOnlinePlayers()) {
            IEuphalysPlayer euphalysPlayer = EuphalysApi.getInstance().getPlayer(player.getUniqueId());
            int groupid = euphalysPlayer.getGroup().getGroupId();
            if (info.get(groupid) == null)
                info.put(groupid, 1);
            else
                info.put(groupid, info.get(groupid) + 1);
            object.clear();
            map.clear();
            map.putAll(info);
            object.put("server", EuphalysApi.getInstance().getSProperty("name"));
            object.put("type", "HUB_GROUP");
            object.put("data", map.toJSONString());
            EuphalysApi.getInstance().getContext().writeAndFlush(object.toJSONString());
        }
        if (Bukkit.getOnlinePlayers().size() == 0) {
            object.clear();
            object.put("server", EuphalysApi.getInstance().getSProperty("name"));
            object.put("type", "HUB_GROUP");
            object.put("data", null);
            EuphalysApi.getInstance().getContext().writeAndFlush(object.toJSONString());
        }
    }
}
