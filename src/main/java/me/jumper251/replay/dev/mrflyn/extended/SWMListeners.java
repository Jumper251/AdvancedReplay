package me.jumper251.replay.dev.mrflyn.extended;

import com.grinderwolf.swm.api.SlimePlugin;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplaySessionFinishEvent;
import me.jumper251.replay.filesystem.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.UUID;

public class SWMListeners implements Listener {

    SlimePlugin plugin;
    public SWMListeners(SlimePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e){
        UUID uid = e.getWorld().getUID();
        if(!ConfigManager.UPLOAD_WORLDS)return;
        if (ConfigManager.BLACKLISTED_UPLOAD_WORDLS.contains(e.getWorld().getName()))return;
        Bukkit.getScheduler().runTaskAsynchronously(ReplaySystem.getInstance(), ()->{
           String hashcode = ReplaySystem.getInstance().worldManger.uploadWorld(e.getWorld().getName());
           if(hashcode==null)return;
           WorldHandler.UUID_HASHCODE.put(uid, hashcode);
        });
//        WorldHandler.WORLD_WATCHER.put(uid, 0);
    }




}
