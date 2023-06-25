package me.jumper251.replay;


import java.util.HashMap;


import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.plugin.java.JavaPlugin;



import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.saving.DatabaseReplaySaver;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.utils.ReplayCleanup;
import me.jumper251.replay.utils.Metrics;
import me.jumper251.replay.utils.ReplayManager;
import me.jumper251.replay.utils.Updater;


public class ReplaySystem extends JavaPlugin {

	
	public static ReplaySystem instance;
	
	public static Updater updater;
	public static Metrics metrics;
	
	public final static String PREFIX = "§8[§3Replay§8] §r§7";

	public static boolean isPluginDisabling = false;

	
	@Override
	public void onDisable() {
		isPluginDisabling = true;
		for (Replay replay : new HashMap<>(ReplayManager.activeReplays).values()) {
		    if (replay.isRecording() && replay.getRecorder().getData().getActions().size() > 0) {
				replay.getRecorder().stop(ConfigManager.SAVE_STOP);
			}
		}
		for(Replayer replay : ReplayHelper.replaySessions.values()){
			System.out.println("Stopping Replay");
			replay.stop();
		}

	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		Long start = System.currentTimeMillis();

		getLogger().info("Loading Replay v" + getDescription().getVersion() + " by " + getDescription().getAuthors().get(0));
		
		ConfigManager.loadConfigs();
		ReplayManager.register();
		
		ReplaySaver.register(ConfigManager.USE_DATABASE ? new DatabaseReplaySaver() : new DefaultReplaySaver());
		
		updater = new Updater();
		metrics = new Metrics(this, 2188);
		
		if (ConfigManager.CLEANUP_REPLAYS > 0) {
			ReplayCleanup.cleanupReplays();
		}
		
		getLogger().info("Finished (" + (System.currentTimeMillis() - start) + "ms)");

	}
	
	
	public static ReplaySystem getInstance() {
		return instance;
	}
}
