package me.jumper251.replay.filesystem;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.utils.LogUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class Messages {

    public static final ConfigMessage PREFIX = new ConfigMessage("prefix", "&8[&3Replay&8] &r&7", false);

    public static final ConfigMessage COMMAND_SYNTAX = new ConfigMessage("command.syntax", "Usage: &6/{command} {args}");
    public static final ConfigMessage COMMAND_OVERVIEW = new ConfigMessage("command.overview", "&6/{command} {args} &7 - {desc}", false);
    public static final ConfigMessage COMMAND_NOTFOUND = new ConfigMessage("command.not_found", "&7Command not found.");
    public static final ConfigMessage COMMAND_NO_PERMISSION = new ConfigMessage("command.no_permission", "&cInsufficient permissions");

    public static final ConfigMessage QUALITY_LOW = new ConfigMessage("general.quality_low", "&cLow", false);
    public static final ConfigMessage QUALITY_MEDIUM = new ConfigMessage("general.quality_medium", "&eMedium", false);
    public static final ConfigMessage QUALITY_HIGH = new ConfigMessage("general.quality_high", "&aHigh", false);

    public static final ConfigMessage REPLAYING_WORLD_NOT_FOUND = new ConfigMessage("replaying.world_not_found", "&cThe world for this Replay does not exist or is not loaded. ({world})");
    public static final ConfigMessage REPLAYING_FINISHED_WATCHING = new ConfigMessage("replaying.finish_watching", "Replay finished.");
    public static final ConfigMessage REPLAYING_PLAYER_DEATH = new ConfigMessage("replaying.player_death", "&6{name} &7died.");
    public static final ConfigMessage REPLAYING_PLAYER_LEAVE = new ConfigMessage("replaying.player_leave", "&6{name} &7left the game.");
    public static final ConfigMessage REPLAYING_PLAYER_JOIN = new ConfigMessage("replaying.player_leave", "&6{name} &7joined the game.", false);

    public static final ConfigMessage REPLAY_NOT_FOUND = new ConfigMessage("replay.not_found", "&cReplay not found.");

    public static final ConfigMessage REPLAY_START = new ConfigMessage("replay.start", "&aSuccessfully started recording &e{replay}&7.\n&7Use &6/Replay stop {replay}&7 to save it.");
    public static final ConfigMessage REPLAY_START_TIMED = new ConfigMessage("replay.start_timed", "&aSuccessfully started recording &e{replay}&7.\n&7The Replay will be saved after &6{duration}&7 seconds");
    public static final ConfigMessage REPLAY_START_INFO = new ConfigMessage("replay.start_info", "&7INFO: You are recording all online players.", false);
    public static final ConfigMessage REPLAY_START_EXISTS = new ConfigMessage("replay.start_exists", "&cReplay already exists.");
    public static final ConfigMessage REPLAY_START_TOO_LONG = new ConfigMessage("replay.start_too_long", "&cReplay name is too long.");
    public static final ConfigMessage REPLAY_START_INVALID_NAME = new ConfigMessage("replay.start_name_invalid", "&cReplay name contains invalid characters.");

    public static final ConfigMessage REPLAY_DELETE = new ConfigMessage("replay.delete", "&aSuccessfully deleted replay.");

    public static final ConfigMessage REPLAY_LEAVE = new ConfigMessage("replay.leave", "&cYou need to play a Replay first");

    public static final ConfigMessage REPLAY_PLAY = new ConfigMessage("replay.play", "Replay loaded. Duration &e{duration}&7 seconds.");
    public static final ConfigMessage REPLAY_PLAY_LOAD = new ConfigMessage("replay.play_load", "Loading replay &e{replay}&7...");
    public static final ConfigMessage REPLAY_PLAY_ERROR = new ConfigMessage("replay.play_error", "&cError while loading &o{replay}.replay. &r&cCheck console for more details.");

    public static final ConfigMessage REPLAY_STOP_SAVING = new ConfigMessage("replay.stop_saving", "Saving replay &e{replay}&7...");
    public static final ConfigMessage REPLAY_STOP_NO_SAVE = new ConfigMessage("replay.stop_no_save", "&7Successfully stopped replay &e{replay}");
    public static final ConfigMessage REPLAY_STOP_SAVED = new ConfigMessage("replay.stop_saved", "&7Successfully saved replay");
    public static final ConfigMessage REPLAY_STOP_SAVED_TO = new ConfigMessage("replay.stop_saved_to", "&7Successfully saved replay to &o{path}");
    public static final ConfigMessage REPLAY_STOP_EXISTS = new ConfigMessage("replay.stop_exists", "&cReplay already exists. Use &o-force &r&cto overwrite or &o-nosave &r&cto discard the recording.");

    public static final ConfigMessage REPLAY_JUMP = new ConfigMessage("replay.jump", "Jumping to &e{time} &7seconds...");
    public static final ConfigMessage REPLAY_JUMP_INVALID = new ConfigMessage("replay.jump_invalid", "&cTime has to be between 1 and {duration}");
    public static final ConfigMessage REPLAY_JUMP_NOT_IN_REPLAY = new ConfigMessage("replay.jump_not_in_replay", "&cYou need to play a Replay first");

    public static final ConfigMessage REPLAY_INFO_LOADING = new ConfigMessage("replay.info_loading", "Loading replay &e{replay}&7...");
    public static final ConfigMessage REPLAY_INFO_HEADER = new ConfigMessage("replay.info.header", "&c» &7Information about &e&l{replay} &c«", false);
    public static final ConfigMessage REPLAY_INFO_SPACE = new ConfigMessage("replay.info.space", " ", false);
    public static final ConfigMessage REPLAY_INFO_CREATOR = new ConfigMessage("replay.info.creator", "&7&oCreated by: &9{creator}", false);
    public static final ConfigMessage REPLAY_INFO_DURATION = new ConfigMessage("replay.info.duration", "&7&oDuration: &6{duration} &7&oseconds", false);
    public static final ConfigMessage REPLAY_INFO_PLAYERS = new ConfigMessage("replay.info.players", "&7&oPlayers: &6{players}", false);
    public static final ConfigMessage REPLAY_INFO_QUALITY = new ConfigMessage("replay.info.quality", "&7&oQuality: {quality}", false);
    public static final ConfigMessage REPLAY_INFO_ENTITIES = new ConfigMessage("replay.info.entities", "&7&oEntities: &6{entities}", false);
    public static final ConfigMessage REPLAY_INFO_HOVER_PART1 = new ConfigMessage("replay.info_hover.part1", "&7&oRecorded data: ", false);
    public static final ConfigMessage REPLAY_INFO_HOVER_PART2 = new ConfigMessage("replay.info_hover.part2", "&6&n{action_count}&r", false);
    public static final ConfigMessage REPLAY_INFO_HOVER_PART3 = new ConfigMessage("replay.info_hover.part3", "&7{data}: &b{amount}", false);
    public static final ConfigMessage REPLAY_INFO_HOVER_PART4 = new ConfigMessage("replay.info_hover.part4", " &7&oactions", false);

    public static final ConfigMessage REPLAY_LIST_HEADER = new ConfigMessage("replay.list_header", "Available replays: &8(&6{replays}&8) &7| Page: &e{page}&7/&e{pages}");
    public static final ConfigMessage REPLAY_LIST_ENTRY = new ConfigMessage("replay.list_entry", " &6&o{date}    &e{replay}", false);
    public static final ConfigMessage REPLAY_LIST_NO_REPLAYS = new ConfigMessage("replay.list_no_replays", "&cNo replays found.");
    public static final ConfigMessage REPLAY_LIST_HOVER_HEADER = new ConfigMessage("replay.list_hover.header", "&7Replay &e&l{replay}", false);
    public static final ConfigMessage REPLAY_LIST_HOVER_SPACE = new ConfigMessage("replay.list_hover.space", " ", false);
    public static final ConfigMessage REPLAY_LIST_HOVER_CREATOR = new ConfigMessage("replay.list_hover.creator", "&7Created by: &6{creator}", false);
    public static final ConfigMessage REPLAY_LIST_HOVER_DURATION = new ConfigMessage("replay.list_hover.duration", "&7Duration: &6{duration} &8sec", false);
    public static final ConfigMessage REPLAY_LIST_HOVER_SPACE2 = new ConfigMessage("replay.list_hover.space", " ", false);
    public static final ConfigMessage REPLAY_LIST_HOVER_ACTION = new ConfigMessage("replay.list_hover.action", "&7Click to play!", false);

    public static final ConfigMessage REPLAY_MIGRATE = new ConfigMessage("replay.migrate", "&7Migrating replays to &e{option}");
    public static final ConfigMessage REPLAY_MIGRATE_ERROR = new ConfigMessage("replay.migrate_error", "&cYou can't migrate to the same system.");
    public static final ConfigMessage REPLAY_MIGRATE_INVALID = new ConfigMessage("replay.migrate_invalid", "&cInvalid argument. {options}");

    public static final ConfigMessage REPLAY_RELOAD = new ConfigMessage("replay.reload", "&aSuccessfully reloaded the configuration.");


    private static final Map<String, ConfigMessage> MESSAGES = new LinkedHashMap<>();

    private static final File LANG_FILE = new File(ReplaySystem.getInstance().getDataFolder(), "lang.yml");
    private static FileConfiguration cfg;

    static {
        try {
            for (Field field : Messages.class.getFields()) {
                if (!field.getType().isAssignableFrom(ConfigMessage.class)) continue;
                ConfigMessage message = (ConfigMessage) field.get(null);

                MESSAGES.put(message.getKey(), message);
            }

        } catch (Exception e) {
            LogUtils.log("Error while loading messages: " + e.getMessage());
        }
    }


    public static MessageBuilder combined(ConfigMessage... messages) {
        return new MessageBuilder(messages);
    }

    public static void loadMessages() {
        if (cfg == null) {
            cfg = YamlConfiguration.loadConfiguration(LANG_FILE);
        } else {
            try {
                cfg.load(LANG_FILE);
            } catch (IOException | InvalidConfigurationException e) {
                LogUtils.log("Error while loading messages: " + e.getMessage());
            }
        }

        boolean update = false;

        for (Map.Entry<String, ConfigMessage> entry : MESSAGES.entrySet()) {
            if (cfg.contains(entry.getKey())) {
                entry.getValue().setMessage(cfg.getString(entry.getKey()));
            } else {
                cfg.set(entry.getKey(), entry.getValue().getDefaultMessage());
                entry.getValue().setMessage(entry.getValue().getDefaultMessage());
                update = true;
            }
        }

        if (update) {
            try {
                cfg.save(LANG_FILE);
            } catch (Exception e) {
                LogUtils.log("Error while saving messages: " + e.getMessage());
            }
        }
    }
}
