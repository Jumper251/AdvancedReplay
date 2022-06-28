package me.jumper251.replay.dev.mrflyn.extended.worldmanagers;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.plugin.loaders.LoaderUtils;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.database.DatabaseRegistry;
import me.jumper251.replay.dev.mrflyn.extended.*;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static me.jumper251.replay.dev.mrflyn.extended.WorldHandler.worldWatcherIncrement;

public class SWMWorldManager implements IWorldManger {

    SlimePlugin slime;
    int query = 0;
    private final SWMListeners listener;

    public SWMWorldManager(){
        slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        listener = new SWMListeners(slime);
    }

    @Override
    public void onWorldLoad(World world) {
//        if (query==2){
//            query = 0;
//        }
        UUID uid = world.getUID();
        if(!ConfigManager.UPLOAD_WORLDS)return;
        if (ConfigManager.BLACKLISTED_UPLOAD_WORDLS.contains(world.getName()))return;
//        Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ()->{
            String hashcode = ReplaySystem.getInstance().worldManger.uploadWorld(world.getName());
            if(hashcode==null)return;
            WorldHandler.UUID_HASHCODE.put(uid, hashcode);
//        try {
//            ReplaySystem.getInstance().getLogger().info("Sleeping");
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        });
//        query++;
    }

    @Override
    public String uploadWorld(String name) {
        try {
            String hashcode = null;
            byte[] data = null;
            if (slime.getLoader("file").worldExists(name)){
                data = slime.getLoader("file").loadWorld(name, true);
                hashcode = WorldUtils.hash(data);
            } else if (slime.getLoader("mysql").worldExists(name)) {
                data = slime.getLoader("mysql").loadWorld(name, true);
                hashcode = WorldUtils.hash(data);
            } else if (slime.getLoader("mongodb").worldExists(name)) {
                data = slime.getLoader("mongodb").loadWorld(name, true);
                hashcode = WorldUtils.hash(data);
            }else {
                return ReplaySystem.getInstance().vanillaWorldManager.uploadWorld(name);
            }
            if (hashcode == null) return null;
            if (data == null)return null;
            if (DatabaseRegistry.getDatabase().getService().hasWorld(hashcode)) return hashcode;
            DatabaseRegistry.getDatabase().getService().setWorld(hashcode, name, data, "slime");
            return hashcode;
        }catch (Exception e){
            if (!(e instanceof NullPointerException))
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File downloadWorld(String hashcode) {
        try {
            RawWorld worldData = DatabaseRegistry.getDatabase().getService().getWorld(hashcode);
            if (worldData == null) {
                LogUtils.log("(downloadWorld) Unable to load world from database. (Doesn't exist)");
                return null;
            }
            if (worldData.data == null || worldData.name == null || worldData.hashcode == null || worldData.type == null) {
                LogUtils.log("(downloadWorld) Unable to load world from database. (Missing data");
                return null;
            }
            if (worldData.type.equals("vanilla")){
                return ReplaySystem.getInstance().vanillaWorldManager.downloadWorld(hashcode);
            }
            String destination = ReplaySystem.getInstance().getDataFolder() + "/downloadedWorlds" + "/" + worldData.name + "_" + worldData.hashcode;
            File file = new File(Paths.get(destination + ".slime").toUri());
            if (!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Files.write(Paths.get(destination + ".slime"), worldData.data);
            return file;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File downloadWorldFromName(String name) {
        try {
            RawWorld worldData = DatabaseRegistry.getDatabase().getService().getWorldFromName(name);
            if (worldData == null) {
                LogUtils.log("(downloadWorldFromName) Unable to load world from database. (Doesn't exist)");
                return null;
            }
            if (worldData.data == null || worldData.name == null || worldData.hashcode == null || worldData.type == null) {
                LogUtils.log("(downloadWorldFromName) Unable to load world from database. (Missing data");
                return null;
            }
            if (worldData.type.equals("vanilla")){
                return ReplaySystem.getInstance().vanillaWorldManager.downloadWorldFromName(name);
            }
            String destination = ReplaySystem.getInstance().getDataFolder() + "/downloadedWorlds" + "/" + worldData.name + "_" + worldData.hashcode;
            return new File(Files.write(Paths.get(destination + ".slime"), worldData.data).toUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void loadWorld(File file) {
        try {
            if (!file.getName().endsWith(".slime")) {
                ReplaySystem.getInstance().vanillaWorldManager.loadWorld(file);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SlimeWorld loadWorldSlime(File file) {
        try {
            byte[] data = Files.readAllBytes(file.toPath());
            SlimePropertyMap spm = new SlimePropertyMap();
            spm.setString(SlimeProperties.WORLD_TYPE, "flat");
            spm.setInt(SlimeProperties.SPAWN_X, 0);
            spm.setInt(SlimeProperties.SPAWN_Y, 64);
            spm.setInt(SlimeProperties.SPAWN_Z, 0);
            spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
            spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
            spm.setString(SlimeProperties.DIFFICULTY, "easy");
            spm.setBoolean(SlimeProperties.PVP, true);
            String name = file.getName().replace(".slime", "");
            return LoaderUtils.deserializeWorld(slime.getLoader("file"), name, data, spm, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public void unloadWorld(String name) {
        ReplaySystem.getInstance().getLogger().info("Swm UnloadWorld: called");
        if (Bukkit.getWorld(name)==null)return;
        ReplaySystem.getInstance().getLogger().info("Swm UnloadWorld: true "+name);
//        Bukkit.unloadWorld(name, true);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "swm unload "+name);
    }

    public Listener getListener(){
        return this.listener;
    }

    @Override
    public void onReplayStart(Replayer replayer, SpawnData spawnData) {
        String destination = ReplaySystem.getInstance().getDataFolder()+"/downloadedWorlds";
        String replayWorld = spawnData.getLocation().getWorld()+"_"+spawnData.getWorldHashCode();
        ReplaySystem.getInstance().getLogger().info("onReplayStart");
        //check if world is already loaded
        replayer.setPaused(true);
        if (Bukkit.getWorld(replayWorld)!=null){
            //TODO: add number to watching replays in this world.
            worldWatcherIncrement(replayWorld, 1);
            ReplaySystem.getInstance().getLogger().info("onReplayStart: worldLoadedAlready");
            replayer.setSpawnWorld(replayWorld);
            replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
            replayer.setPaused(false);
            return;
        }
        //check if world is already downloaded
        if(WorldUtils.doesFolderExists(replayWorld+".slime", destination)){
            //TODO: loadWorld
            ReplaySystem.getInstance().getLogger().info("onReplayStart: loadWorldSlime");
            Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ()->{
                SlimeWorld world = loadWorldSlime(new File(destination,replayWorld+".slime"));
                Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), ()->{
                    slime.generateWorld(world);
                    replayer.setSpawnWorld(replayWorld);
                    replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
                    worldWatcherIncrement(replayWorld, 1);
                    replayer.setPaused(false);
                });
            });
            return;
        }
        else if (WorldUtils.doesFolderExists(replayWorld, destination)) {
            //TODO: loadWorld
            ReplaySystem.getInstance().getLogger().info("onReplayStart: loadWorldVanilla");
            ReplaySystem.getInstance().worldManger
                    .loadWorld(new File(destination + "/" + replayWorld));
            worldWatcherIncrement(replayWorld, 1);
            replayer.setSpawnWorld(replayWorld);
            replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
            replayer.setPaused(false);
            return;
        }
        //download world from database async
        Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ()->{
            ReplaySystem.getInstance().getLogger().info("onReplayStart: downloadWorld");
            File file = ReplaySystem.getInstance().worldManger.downloadWorld(spawnData.getWorldHashCode());
            ReplaySystem.getInstance().getLogger().info("onReplayStart: downloadWorld done");
            if (file==null){
                //TODO: attempt unsafe world loading
                Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), ()->{
                    replayer.setSpawnWorld(spawnData.getLocation().getWorld());
                    replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation()));
                    replayer.setPaused(false);
                });
                return;
            }
            if (!file.getName().endsWith(".slime")) {
                Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), () -> {
                    ReplaySystem.getInstance().getLogger().info("onReplayStart: downloadWorld done vanilla");
                    ReplaySystem.getInstance().worldManger.loadWorld(file);
                    worldWatcherIncrement(replayWorld, 1);
                    replayer.setSpawnWorld(replayWorld);
                    replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
                    replayer.setPaused(false);
                });
                return;
            }
            ReplaySystem.getInstance().getLogger().info("onReplayStart: downloadWorld done slime");
            SlimeWorld w = loadWorldSlime(file);
            ReplaySystem.getInstance().getLogger().info("onReplayStart: downloadWorld done slime done");
            Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), () -> {
                ReplaySystem.getInstance().getLogger().info("onReplayStart: loadWorld slime");
                slime.generateWorld(w);
                ReplaySystem.getInstance().getLogger().info("onReplayStart: loadWorld slime done");
                worldWatcherIncrement(replayWorld, 1);
                replayer.setSpawnWorld(replayWorld);
                replayer.getWatchingPlayer().teleport(LocationData.toLocation(spawnData.getLocation(), replayWorld));
                replayer.setPaused(false);
            });
        });
    }

}
