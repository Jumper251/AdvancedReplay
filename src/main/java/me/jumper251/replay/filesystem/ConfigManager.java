package me.jumper251.replay.filesystem;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	public static boolean RECORD_CHAT;
	public static boolean SAVE_STOP, RECORD_STARTUP, USE_OFFLINE_SKINS, HIDE_PLAYERS, UPDATE_NOTIFY, USE_DATABASE, ADD_PLAYERS;
	public static boolean WORLD_RESET;
	public static boolean UPLOAD_WORLDS;
	public static List<String> BLACKLISTED_UPLOAD_WORDLS = new ArrayList<>();

	public static ReplayQuality QUALITY = ReplayQuality.HIGH;
	
	public static String DEATH_MESSAGE, LEAVE_MESSAGE, CHAT_FORMAT, JOIN_MESSAGE;
	
	public static void loadConfigs() {
//		if(!sqlFile.exists()){
			sqlCfg.addDefault("host", "localhost");
			sqlCfg.addDefault("port", 3306);
			sqlCfg.addDefault("username", "username");
			sqlCfg.addDefault("database", "database");
			sqlCfg.addDefault("password", "password");
			sqlCfg.addDefault("poolSize", 50);
			sqlCfg.addDefault("maxLifetime", 1800);
			sqlCfg.addDefault("timeout", 30000L);
			sqlCfg.addDefault("ssl", false);
			sqlCfg.addDefault("verifyCertificate", true);
			sqlCfg.addDefault("prefix", "");
			sqlCfg.options().copyDefaults(true);
			try {
				sqlCfg.save(sqlFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}
		
//		if (!file.exists()) {
			LogUtils.log("Creating Config files...");
			
			cfg.addDefault("general.max_length", 3600);
			cfg.addDefault("general.record_on_startup", false);
			cfg.addDefault("general.save_on_stop", false);
			cfg.addDefault("general.use_mysql", false);
			cfg.addDefault("general.upload_worlds", false);
			cfg.addDefault("general.blacklisted_upload_worlds", Collections.singletonList("black_listed_world_name"));
			cfg.addDefault("general.use_offline_skins", true);
			cfg.addDefault("general.quality", "high");
			cfg.addDefault("general.cleanup_replays", -1);
			cfg.addDefault("general.hide_players", false);
			cfg.addDefault("general.add_new_players", false);	
			cfg.addDefault("general.update_notifications", true);
			
			cfg.addDefault("general.death_message", "&6{name} &7died.");
			cfg.addDefault("general.quit_message", "&6{name} &7left the game.");
			cfg.addDefault("general.join_message", "&6{name} &7joined the game.");

			cfg.addDefault("replaying.world.reset_changes", false);
			
			cfg.addDefault("recording.blocks.enabled", true);
			cfg.addDefault("recording.blocks.real_changes", true);
			cfg.addDefault("recording.entities.enabled", false);
			cfg.addDefault("recording.entities.items.enabled", true);
			cfg.addDefault("recording.chat.enabled", false);
			cfg.addDefault("recording.chat.format", "&r<{name}> {message}");
			cfg.options().copyDefaults(true);
			cfg.options().header("Only set upload_worlds to true if you're running a minigame server in a bungee/velocity network. Mysql is compulsory for this mode.");
			cfg.options().header("When upload_worlds in true advancedReplay uses an uploaded copy of the world to show the replay. Useful if you want to view the replay in other servers.");


			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}
			
		ItemConfig.loadConfig();
		
		loadData(true);
		
	}
	
	public static void loadData(boolean initial) {
		MAX_LENGTH = cfg.getInt("general.max_length");
		SAVE_STOP = cfg.getBoolean("general.save_on_stop");
		RECORD_STARTUP = cfg.getBoolean("general.record_on_startup", false);
		USE_OFFLINE_SKINS = cfg.getBoolean("general.use_offline_skins");
		QUALITY = ReplayQuality.valueOf(cfg.getString("general.quality", "high").toUpperCase());
		HIDE_PLAYERS = cfg.getBoolean("general.hide_players");
		CLEANUP_REPLAYS = cfg.getInt("general.cleanup_replays", -1);
		ADD_PLAYERS = cfg.getBoolean("general.add_new_players");
		UPDATE_NOTIFY = cfg.getBoolean("general.update_notifications");
		UPLOAD_WORLDS = cfg.getBoolean("general.upload_worlds");
		BLACKLISTED_UPLOAD_WORDLS = cfg.getStringList("general.blacklisted_upload_worlds");
		if (initial ) USE_DATABASE = cfg.getBoolean("general.use_mysql");

		DEATH_MESSAGE = cfg.getString("general.death_message");
		LEAVE_MESSAGE = cfg.getString("general.quit_message");
		JOIN_MESSAGE = cfg.getString("general.join_message");
		
		WORLD_RESET = cfg.getBoolean("replaying.world.reset_changes", false);
		
		CHAT_FORMAT = cfg.getString("recording.chat.format");
		RECORD_BLOCKS = cfg.getBoolean("recording.blocks.enabled");
		REAL_CHANGES = cfg.getBoolean("recording.blocks.real_changes");
		RECORD_ITEMS = cfg.getBoolean("recording.entities.items.enabled");
		RECORD_ENTITIES = cfg.getBoolean("recording.entities.enabled");
		RECORD_CHAT = cfg.getBoolean("recording.chat.enabled");

		if (USE_DATABASE) {
			
			String host = sqlCfg.getString("host");
			int port = sqlCfg.getInt("port", 3306);
			String username = sqlCfg.getString("username");
			String database = sqlCfg.getString("database");
			String password = sqlCfg.getString("password");
			int poolSize = sqlCfg.getInt("poolSize", 50);
			int maxLifetime = sqlCfg.getInt("maxLifetime", 1800);
			long timeout = sqlCfg.getLong("timeout", 30000L);
			boolean ssl = sqlCfg.getBoolean("ssl", false);
			boolean verifyCertificate = sqlCfg.getBoolean("verifyCertificate", true);
			String prefix = sqlCfg.getString("prefix", "");

			MySQLDatabase mysql =
					new MySQLDatabase(host, port, database, username, password, prefix, poolSize, maxLifetime, ssl, verifyCertificate, timeout);
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
