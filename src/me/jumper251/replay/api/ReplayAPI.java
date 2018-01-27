package me.jumper251.replay.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.jumper251.replay.filesystem.saving.IReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.utils.ReplayManager;

public class ReplayAPI {

	private static ReplayAPI instance;
	
	private HookManager hookManager;
	
	private ReplayAPI() {
		this.hookManager = new HookManager();
	}
	
	public void registerHook(IReplayHook hook) {
		this.hookManager.registerHook(hook);
	}
	
	public void unregisterHook() {
		this.hookManager.unregisterHook();
	}
	
	
	public Replay recordReplay(String name, Player... players) {
		List<Player> toRecord = new ArrayList<Player>();
		
		if (players != null && players.length > 0) { 
			toRecord = Arrays.asList(players);
		} else {
			for (Player all : Bukkit.getOnlinePlayers()) {
				toRecord.add(all);
			}
		}
		
		Replay replay = new Replay();
		if (name != null) replay.setId(name);
		replay.recordAll(toRecord);
		
		return replay;
	}
	
	public void stopReplay(String name, boolean save) {
		if (ReplayManager.activeReplays.containsKey(name)) {
			Replay replay = ReplayManager.activeReplays.get(name);
			if (replay.isRecording()) replay.getRecorder().stop(save);
		}
	}
	
	public void playReplay(String name, Player watcher) {
		if (ReplaySaver.exists(name) && !ReplayHelper.replaySessions.containsKey(watcher.getName())) {
			ReplaySaver.load(name).play(watcher);
		}
	}
	
	public void registerReplaySaver(IReplaySaver replaySaver) {
		ReplaySaver.register(replaySaver);
	}
	
	public HookManager getHookManager() {
		return hookManager;
	}
	
	
	public static ReplayAPI getInstance() {
		if (instance == null) instance = new ReplayAPI();
		
		return instance;
	}
}
