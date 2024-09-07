package me.jumper251.replay.utils;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.commands.replay.ReplayCommand;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.listener.ReplayListener;
import me.jumper251.replay.replaysystem.Replay;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class ReplayManager {

    public static HashMap<String, Replay> activeReplays = new HashMap<>();

    public static void register() {
        registerEvents();
        registerCommands();

        if (ConfigManager.RECORD_STARTUP) {
            ReplayAPI.getInstance().recordReplay(null, Bukkit.getConsoleSender());
        }

        Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ReplayManager::delayedInit);
    }

    private static void registerEvents() {
        new ReplayListener().register();
    }

    private static void registerCommands() {
        ReplaySystem.getInstance().getCommand("replay").setExecutor(new ReplayCommand());
    }

    private static void delayedInit() {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_21)) {
            ProtocolLibUtil.prepare();
        }
    }

}
