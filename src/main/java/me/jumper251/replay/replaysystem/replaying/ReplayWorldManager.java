package me.jumper251.replay.replaysystem.replaying;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import me.jumper251.replay.ReplaySystem;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class ReplayWorldManager {

  private final SlimePlugin slimePlugin;
  private SlimeLoader sqlLoader;

  public ReplayWorldManager() {
    this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    this.sqlLoader = slimePlugin.getLoader("mysql");
  }

  public void cloneReplayWorld(Replayer replayer, String worldName, String playerName) {
    try {
      SlimeWorld world = slimePlugin.loadWorld(sqlLoader, worldName, true, getProperties());
      String clonedWorldName = worldName + "_" + playerName + "_replay";
      SlimeWorld clonedWorld = world.clone(clonedWorldName);
      slimePlugin.generateWorld(clonedWorld);

      new BukkitRunnable() {
        @Override
        public void run() {
          if (Bukkit.getWorld(clonedWorldName) != null) {
            Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), () -> {
              replayer.start(Bukkit.getWorld(clonedWorldName));
            });
            cancel();
          }
        }
      }.runTaskTimerAsynchronously(ReplaySystem.getInstance(), 0, 10);
    } catch (CorruptedWorldException | NewerFormatException | WorldInUseException | UnknownWorldException |
             IOException e) {
      e.printStackTrace();
    }
  }

  private SlimePropertyMap getProperties() {
    SlimePropertyMap properties = new SlimePropertyMap();

    properties.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
    properties.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
    properties.setString(SlimeProperties.DIFFICULTY, "normal");

    return properties;
  }
}
