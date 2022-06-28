package me.jumper251.replay.dev.mrflyn.extended.worldmanagers;

import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.UUID;

public interface IWorldManger {

    void onWorldLoad(World world);

    //call this when a world is loaded WorldLoadEvent. Uploads the world to the db if not exists.
    //call async to save performance
    String uploadWorld(String name);

    //call this to download a world from the db, using world's hashcode.
    //call async to save performance
    File downloadWorld(String hashcode);

    //call this to download a world from the db, using world's name. (unsafe)
    //call async to save performance
    File downloadWorldFromName(String name);

    //call this to load the world from a directory must be called in sync.
    void loadWorld(File folder);

    void unloadWorld(String name);

    Listener getListener();

    void onReplayStart(Replayer replayer, SpawnData spawnData);


}
