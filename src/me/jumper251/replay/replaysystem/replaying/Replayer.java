package me.jumper251.replay.replaysystem.replaying;

import java.util.HashMap;


import java.util.List;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;



import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;

import me.jumper251.replay.replaysystem.utils.INPC;



public class Replayer {

	private HashMap<String, INPC> npcs;
	
	private Player watcher;
	
	private Replay replay;
	
	BukkitRunnable run;
	
	private int currentTicks;
	
	private ReplayingUtils utils;
	
	public Replayer(Replay replay, Player watcher) {
		this.replay = replay;
		this.watcher = watcher;
		this.npcs = new HashMap<String, INPC>();
		this.utils = new ReplayingUtils(this);
	}
	
	
	public void start() {
		ReplayData data = this.replay.getData();
		int duration = data.getDuration();
		
		this.run = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if (currentTicks < duration) {
					if (!data.getActions().isEmpty() && data.getActions().containsKey(currentTicks)) {
						System.out.println("executing tick " + currentTicks);
						List<ActionData> list = data.getActions().get(currentTicks);
						for (ActionData action : list) {
							
							utils.handleAction(action, data);
							
							if (action.getType() == ActionType.CUSTOM) {
								if (ReplayAPI.getInstance().getHookManager().isRegistered()) {
									ReplayAPI.getInstance().getHookManager().getHook().onPlay(action, Replayer.this);
								}
							}
						
						}
						
						data.getActions().remove(currentTicks);
					}
						
					
					Replayer.this.currentTicks++;
					
				} else {
					
					stop();
				}
			}
		};
		
		this.run.runTaskTimerAsynchronously(ReplaySystem.getInstance(), 1, 1);
		
	}
	
	public void stop() {
		sendMessage("Replay finished.");
		
		this.run.cancel();
		
		for (INPC npc : this.npcs.values()) {
			npc.remove();
		}
		
		this.npcs.clear();
		
		this.replay.setPlaying(false);
	}
	
	public HashMap<String, INPC> getNPCList() {
		return npcs;
	}
	
	public Player getWatchingPlayer() {
		return watcher;
	}
	
	public Replay getReplay() {
		return replay;
	}
	
	public ReplayingUtils getUtils() {
		return utils;
	}

	private void sendMessage(String message) {
		this.watcher.sendMessage(ReplaySystem.PREFIX + message);
	}
}
