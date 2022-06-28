package me.jumper251.replay.dev.mrflyn.extended.worldmanagers;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.database.DatabaseRegistry;
import me.jumper251.replay.dev.mrflyn.extended.VanillaListeners;
import me.jumper251.replay.dev.mrflyn.extended.VoidChunkGenerator;
import me.jumper251.replay.dev.mrflyn.extended.WorldHandler;
import me.jumper251.replay.dev.mrflyn.extended.WorldUtils;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.utils.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import static me.jumper251.replay.dev.mrflyn.extended.WorldHandler.worldWatcherIncrement;

public class VanillaWorldManager implements IWorldManger {

    private final VanillaListeners listener;

    public VanillaWorldManager(){
        listener = new VanillaListeners();
    }

    @Override
    public void onWorldLoad(World world) {
        UUID uid = world.getUID();
        if(!ConfigManager.UPLOAD_WORLDS)return;
        if (ConfigManager.BLACKLISTED_UPLOAD_WORDLS.contains(world.getName()))return;
        Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ()->{
            String hashcode = ReplaySystem.getInstance().worldManger.uploadWorld(world.getName());
            if(hashcode==null)return;
            WorldHandler.UUID_HASHCODE.put(uid, hashcode);
        });
    }

    @Override
    public String uploadWorld(String uid) {
        try {
            File folder = Bukkit.getWorld(uid).getWorldFolder();
            String hashcode = WorldUtils.hashDirectory(folder);
            if (hashcode == null) return null;
            if (DatabaseRegistry.getDatabase().getService().hasWorld(hashcode)) return hashcode;
            ByteArrayOutputStream os = WorldUtils.createTarGzip(folder);
            //TODO: store
            DatabaseRegistry.getDatabase().getService().setWorld(hashcode, Bukkit.getWorld(uid).getName(), os.toByteArray(), "vanilla");
            return hashcode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File downloadWorld(String hashcode) {
        RawWorld worldData = DatabaseRegistry.getDatabase().getService().getWorld(hashcode);
        if (worldData==null){
            LogUtils.log("(downloadWorld) Unable to load world from database. (Doesn't exist)");
            return null;
        }
        if (worldData.data==null||worldData.name==null||worldData.hashcode==null){
            LogUtils.log("(downloadWorld) Unable to load world from database. (Missing data");
            return null;
        }
        String destination = ReplaySystem.getInstance().getDataFolder()+"/downloadedWorlds" + "/"+worldData.name+"_"+worldData.hashcode;
        WorldUtils.extractTarGZ(new ByteArrayInputStream(worldData.data), destination);
        return new File(destination);
    }

    @Override
    public File downloadWorldFromName(String name) {
        RawWorld worldData = DatabaseRegistry.getDatabase().getService().getWorldFromName(name);
        if (worldData==null){
            LogUtils.log("(downloadWorldFromName) Unable to load world from database. (Doesn't exist)");
            return null;
        }
        if (worldData.data==null||worldData.name==null||worldData.hashcode==null){
            LogUtils.log("(downloadWorldFromName) Unable to load world from database. (Missing data");
            return null;
        }
        String destination = ReplaySystem.getInstance().getDataFolder()+"/downloadedWorlds" + "/"+worldData.name+"_"+worldData.hashcode;
        WorldUtils.extractTarGZ(new ByteArrayInputStream(worldData.data), destination);
        return new File(destination);
    }

    @Override
    public void loadWorld(File folder) {
        WorldCreator wc = new WorldCreator(folder.getPath());
        wc.generateStructures(false);
        wc.generator(new VoidChunkGenerator());
        World w = Bukkit.createWorld(wc);
        if (w == null) {
            throw new IllegalStateException("World should be null");
        }
        w.setKeepSpawnInMemory(true);
        w.setAutoSave(false);
    }

    @Override
    public void unloadWorld(String name) {
        if (Bukkit.getWorld(name)==null)return;
        Bukkit.unloadWorld(name, false);
    }



    @Override
    public Listener getListener(){
        return this.listener;
    }

    @Override
    public void onReplayStart(Replayer replayer, SpawnData spawnData) {
        String destination = ReplaySystem.getInstance().getDataFolder()+"/downloadedWorlds";
        String replayWorld = spawnData.getLocation().getWorld()+"_"+spawnData.getWorldHashCode();
        //check if world is already loaded
        replayer.setPaused(true);
        if (Bukkit.getWorld(replayWorld)!=null){
            //TODO: add number to watching replays in this world.
            worldWatcherIncrement(replayWorld, 1);
            replayer.setSpawnWorld(replayWorld);
            replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
            replayer.setPaused(false);
            return;
        }
        //check if world is already downloaded
        if(WorldUtils.doesFolderExists(replayWorld, destination)){
            //TODO: loadWorld
            ReplaySystem.getInstance().worldManger
                    .loadWorld(new File(destination+"/"+replayWorld));
            worldWatcherIncrement(replayWorld, 1);
            replayer.setSpawnWorld(replayWorld);
            replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
            replayer.setPaused(false);
            return;
        }
        //download world from database async
        Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ()->{
            File file = ReplaySystem.getInstance().worldManger.downloadWorld(spawnData.getWorldHashCode());
            if (file==null){
                //TODO: attempt unsafe world loading
                Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), ()->{
                    replayer.setSpawnWorld(spawnData.getLocation().getWorld());
                    replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation()));
                });
                return;
            }
            Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), ()->{
                ReplaySystem.getInstance().worldManger.loadWorld(file);
                worldWatcherIncrement(replayWorld, 1);
                replayer.setSpawnWorld(replayWorld);
                replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
                replayer.setPaused(false);
            });
        });
    }
}
