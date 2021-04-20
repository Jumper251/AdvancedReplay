package me.jumper251.replay.filesystem;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.database.DatabaseRegistry;
import me.jumper251.replay.database.MySQLDatabase;
import me.jumper251.replay.replaysystem.recording.optimization.ReplayQuality;
import me.jumper251.replay.utils.LogUtils;

public class ConfigManager {

	public static File sqlFile = new File(ReplaySystem.getInstance().getDataFolder(), "mysql.yml");
	public static FileConfiguration sqlCfg = YamlConfiguration.loadConfiguration(sqlFile);
	
	public static File file = new File(ReplaySystem.getInstance().getDataFolder(), "config.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static int MAX_LENGTH, CLEANUP_REPLAYS;
	
	public static boolean RECORD_BLOCKS, REAL_CHANGES;
	public static boolean RECORD_ITEMS, RECORD_ENTITIES;
	public static boolean RECORD_CHAT, RECORD_PLUGIN_MESSAGES;
	public static boolean SAVE_STOP, USE_OFFLINE_SKINS, HIDE_PLAYERS, UPDATE_NOTIFY, USE_DATABASE, ADD_PLAYERS;
	public static boolean WORLD_RESET;
	
	public static ReplayQuality QUALITY = ReplayQuality.HIGH;
	
	public static void loadConfigs() {
		if(!sqlFile.exists()){
			sqlCfg.set("host", "localhost");
			sqlCfg.set("username", "username");
			sqlCfg.set("database", "database");
			sqlCfg.set("password", "password");
			try {
				sqlCfg.save(sqlFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!file.exists()) {
			LogUtils.log("Creating Config files...");
			
			cfg.set("general.max_length", 3600);
			cfg.set("general.save_on_stop", false);
			cfg.set("general.use_mysql", false);
			cfg.set("general.use_offline_skins", true);
			cfg.set("general.quality", "high");
			cfg.set("general.cleanup_replays", -1);
			cfg.set("general.hide_players", false);
			cfg.set("general.add_new_players", false);	
			cfg.set("general.update_notifications", true);

			cfg.set("replaying.world.reset_changes", false);
			
			cfg.set("recording.blocks.enabled", true);
			cfg.set("recording.blocks.real_changes", true);
			cfg.set("recording.entities.enabled", false);
			cfg.set("recording.entities.items.enabled", true);
			cfg.set("recording.chat.enabled", false);
			cfg.set("recording.chat.plugin_messages", false);


			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		ItemConfig.loadConfig();
		
		loadData(true);
		
	}
	
	public static void loadData(boolean initial) {
		MAX_LENGTH = cfg.getInt("general.max_length");
		SAVE_STOP = cfg.getBoolean("general.save_on_stop");
		USE_OFFLINE_SKINS = cfg.getBoolean("general.use_offline_skins");
		QUALITY = ReplayQuality.valueOf(cfg.getString("general.quality", "high").toUpperCase());
		HIDE_PLAYERS = cfg.getBoolean("general.hide_players");
		CLEANUP_REPLAYS = cfg.getInt("general.cleanup_replays", -1);
		ADD_PLAYERS = cfg.getBoolean("general.add_new_players");
		UPDATE_NOTIFY = cfg.getBoolean("general.update_notifications");
		if (initial ) USE_DATABASE = cfg.getBoolean("general.use_mysql");
		RECORD_BLOCKS = cfg.getBoolean("recording.blocks.enabled");
		REAL_CHANGES = cfg.getBoolean("recording.blocks.real_changes");
		RECORD_ITEMS = cfg.getBoolean("recording.entities.items.enabled");
		RECORD_ENTITIES = cfg.getBoolean("recording.entities.enabled");
		RECORD_CHAT = cfg.getBoolean("recording.chat.enabled");
		RECORD_PLUGIN_MESSAGES = cfg.getBoolean("recording.chat.plugin_messages");

		if (USE_DATABASE) {
			
			String host = sqlCfg.getString("host");
			String username = sqlCfg.getString("username");
			String database = sqlCfg.getString("database");
			String password = sqlCfg.getString("password");
			
			MySQLDatabase mysql = new MySQLDatabase(host, database, username, password);
			DatabaseRegistry.registerDatabase(mysql);
			DatabaseRegistry.getDatabase().getService().createReplayTable();
			
		}


		ItemConfig.loadData();
	}
	
	public static void reloadConfig() {
		try {
			cfg.load(file);
			ItemConfig.cfg.load(ItemConfig.file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		loadData(false);
	}
}
