package me.jumper251.replay.filesystem.saving;

import java.util.ArrayList;
import java.util.List;

import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.utils.fetcher.Consumer;

public class ReplaySaver {

	public static IReplaySaver replaySaver;
	
	public static void register(IReplaySaver saver) {
		replaySaver = saver;
	}
	
	public static void unregister() {
		replaySaver = null;
	}
	
	public static boolean isRegistered() {
		return replaySaver != null;
	}
	
	public static void save(Replay replay) {
		if (isRegistered()) {
			replaySaver.saveReplay(replay);
		}
	}
	
	public static void load(String replayName, Consumer<Replay> consumer) {
		if (isRegistered()) {
			replaySaver.loadReplay(replayName, consumer);
		} else {
			consumer.accept(null);
		}
	}
	
	public static boolean exists(String replayName) {
		if (isRegistered()) {
			return replaySaver.replayExists(replayName);
		} else {
			return false;
		}
	}
	
	public static void delete(String replayName) {
		if (isRegistered()) {
			replaySaver.deleteReplay(replayName);
		}
	}
	
	public static List<String> getReplays() {
		if (isRegistered()) {
			return replaySaver.getReplays();
		} else {
			return new ArrayList<String>();
		}
	}
 }
