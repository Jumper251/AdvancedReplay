package me.jumper251.replay.filesystem.saving;

import me.jumper251.replay.replaysystem.Replay;

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
	
	public static Replay load(String replayName) {
		if (isRegistered()) {
			return replaySaver.loadReplay(replayName);
		} else {
			return null;
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
 }
