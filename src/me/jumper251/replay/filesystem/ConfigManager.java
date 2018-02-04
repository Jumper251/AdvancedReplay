package me.jumper251.replay.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.utils.LogUtils;

public class ConfigManager {

	public static File file = new File(ReplaySystem.getInstance().getDataFolder(), "config.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static int MAX_LENGTH;
	
	public static boolean RECORD_BLOCKS, REAL_CHANGES;
	public static boolean RECORD_ITEMS;
	public static boolean SAVE_STOP, USE_OFFLINE_SKINS;
	
	public static String DEATH_MESSAGE, LEAVE_MESSAGE;
	
	public static void loadConfigs() {
		if (!file.exists()) {
			LogUtils.log("Creating Config files...");
			
			cfg.set("general.max_length", 3600);
			cfg.set("general.save_on_stop", false);
			cfg.set("general.use_offline_skins", true);
			cfg.set("general.death_message", "&6{name} &7died.");
			cfg.set("general.quit_message", "&6{name} &7left the game.");

			cfg.set("recording.blocks.enabled", true);
			cfg.set("recording.blocks.real_changes", true);
			cfg.set("recording.entities.items.enabled", true);

			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		loadData();
	}
	
	public static void loadData() {
		MAX_LENGTH = cfg.getInt("general.max_length");
		SAVE_STOP = cfg.getBoolean("general.save_on_stop");
		USE_OFFLINE_SKINS = cfg.getBoolean("general.use_offline_skins");
		DEATH_MESSAGE = cfg.getString("general.death_message");
		LEAVE_MESSAGE = cfg.getString("general.quit_message");

		RECORD_BLOCKS = cfg.getBoolean("recording.blocks.enabled");
		REAL_CHANGES = cfg.getBoolean("recording.blocks.real_changes");
		RECORD_ITEMS = cfg.getBoolean("recording.entities.items.enabled");

	}
	
	public static void reloadConfig() {
		try {
			cfg.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		loadConfigs();
	}
}
