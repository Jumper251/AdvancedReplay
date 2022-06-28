package me.jumper251.replay.dev.mrflyn.extended;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class WorldHandler {
    public static HashMap<UUID, String> UUID_HASHCODE = new HashMap<>();
    public static HashMap<String, Integer> WORLD_WATCHER = new HashMap<>();
    public static void onReplayStart(Replayer replayer, SpawnData spawnData){
        if (!ConfigManager.USE_DATABASE){
            replayer.setPaused(true);
            replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation()));
            replayer.setPaused(false);
            return;
        }

        ReplaySystem.getInstance().worldManger.onReplayStart(replayer, spawnData);


    }
    public static void worldWatcherIncrement(String name, int number){
        if (WORLD_WATCHER.containsKey(name))WORLD_WATCHER.put(name, WORLD_WATCHER.get(name)+number);
        else WORLD_WATCHER.put(name, number);
    }

}
