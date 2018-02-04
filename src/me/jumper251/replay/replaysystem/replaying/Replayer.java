package me.jumper251.replay.replaysystem.replaying;

import java.util.ArrayList;
import java.util.HashMap;





import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;



import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.utils.INPC;



public class Replayer {

	private HashMap<String, INPC> npcs;
	
	private Player watcher;
	
	private Replay replay;
	
	BukkitRunnable run;
	
	private int currentTicks;
	private double speed, tmpTicks;
	
	private boolean paused, started;
	
	private ReplayingUtils utils;
	private ReplaySession session;
	
	public Replayer(Replay replay, Player watcher) {
		this.replay = replay;
		this.watcher = watcher;
		this.npcs = new HashMap<String, INPC>();
		this.utils = new ReplayingUtils(this);
		this.session = new ReplaySession(this);
		this.paused = false;
		
		ReplayHelper.replaySessions.put(watcher.getName(), this);
	}
	
	
	public void start() {
		ReplayData data = this.replay.getData();
		int duration = data.getDuration();
		for (ActionData startData : data.getActions().get(0)) {
			if (startData.getPacketData() instanceof SpawnData) {
				SpawnData spawnData = (SpawnData) startData.getPacketData();
				watcher.teleport(LocationData.toLocation(spawnData.getLocation()));
				break;
			}
		}
		
		this.speed = 1;
		
		executeTick(0, false);
		
		this.run = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if (Replayer.this.paused) return;
				
				Replayer.this.tmpTicks += speed;
				if (Replayer.this.tmpTicks % 1 != 0) return;
				
				if (currentTicks < duration) {

					executeTick(currentTicks, false);
						
					Replayer.this.currentTicks++;

					
					if ((currentTicks + 2) < duration && speed == 2)  {
						executeTick(currentTicks, false);
						
						Replayer.this.currentTicks++;
					}
					
					updateXPBar();
				} else {
					
					stop();
				}
			}
		};
		
		this.run.runTaskTimerAsynchronously(ReplaySystem.getInstance(), 1, 1);
		
	}
	
	public void executeTick(int tick, boolean reversed) {
		ReplayData data = this.replay.getData();
		
		if (!data.getActions().isEmpty() && data.getActions().containsKey(tick)) {
			if (tick == 0 && started) return;
			this.started = true;
			
			List<ActionData> list = data.getActions().get(tick);
			for (ActionData action : list) {
								
				utils.handleAction(action, data, reversed);
				
				if (action.getType() == ActionType.CUSTOM) {
					if (ReplayAPI.getInstance().getHookManager().isRegistered()) {
						ReplayAPI.getInstance().getHookManager().getHook().onPlay(action, Replayer.this);
					}
				}
			
			}
			
			if (tick == 0) data.getActions().remove(tick);
		}
	}
	
	private void updateXPBar() {
		int level = currentTicks / 20;
		float percentage = (float) currentTicks / this.replay.getData().getDuration();

		this.watcher.setLevel(level);
		this.watcher.setExp(percentage);
	}
	
	public void stop() {
		sendMessage("Replay finished.");
		
		this.run.cancel();
		this.getReplay().getData().getActions().clear();
		
		for (INPC npc : this.npcs.values()) {
			npc.remove();
		}
		
		this.utils.despawn(new ArrayList<Entity>(this.utils.getEntities().values()));
		
		this.npcs.clear();
		
		this.replay.setPlaying(false);
		this.session.stopSession();
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
	
	public ReplaySession getSession() {
		return session;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void setSpeed(double speed) {
		this.tmpTicks = 1;
		this.speed = speed;
		
		
		ReplayHelper.sendTitle(watcher, null, speed >= 1 ? "¤ax" + speed : "¤cx" + speed, 10);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public int getCurrentTicks() {
		return currentTicks;
	}
	
	public void setCurrentTicks(int currentTicks) {
		this.currentTicks = currentTicks;
	}
	
	public void sendMessage(String message) {
		if (message != null) {
			this.watcher.sendMessage(ReplaySystem.PREFIX + message);
		}
	}
}
