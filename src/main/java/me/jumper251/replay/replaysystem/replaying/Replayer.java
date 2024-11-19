package me.jumper251.replay.replaysystem.replaying;

import java.util.ArrayList;



import java.util.Collection;
import java.util.HashMap;





import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.jumper251.replay.filesystem.MessageBuilder;
import me.jumper251.replay.filesystem.Messages;
import me.jumper251.replay.replaysystem.replaying.session.ReplaySession;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;



import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.IReplayHook;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.types.ItemData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.utils.entities.IEntity;
import me.jumper251.replay.replaysystem.utils.entities.INPC;



public class Replayer {

	private HashMap<String, INPC> npcs;
	
	private HashMap<Integer, IEntity> entities;
	
	private Map<Location, ItemData> blockChanges;
	
	private Player watcher;
	
	private Replay replay;
	
	private BukkitRunnable run;
	
	private int currentTicks;
	private double speed, tmpTicks;
	
	private boolean paused, started;
	
	private ReplayingUtils utils;
	private ReplaySession session;
		
	public Replayer(Replay replay, Player watcher) {
		this.replay = replay;
		this.watcher = watcher;
		this.npcs = new HashMap<String, INPC>();
		this.entities = new HashMap<Integer, IEntity>();
		this.blockChanges = new HashMap<>();
		
		this.utils = new ReplayingUtils(this);
		this.session = new ReplaySession(this);
		this.paused = false;
	}
	
	
	public boolean start() {
		ReplayData data = this.replay.getData();
		int duration = data.getDuration();
		this.session.setStart(watcher.getLocation());
		SpawnData spawnData = null;
		if (data.getActions().containsKey(0)) {
			for (ActionData startData : data.getActions().get(0)) {
				if (startData.getPacketData() instanceof SpawnData) {
					spawnData = (SpawnData) startData.getPacketData();
					break;
				}
			}
		} else {
			spawnData = findFirstSpawn(data).orElse(null);
		}

		if (spawnData != null && !spawnData.getLocation().isValidWorld()) {
			sendMessage(Messages.REPLAYING_WORLD_NOT_FOUND.arg("world", spawnData.getLocation().getWorld()));
			return false;
		}

		ReplayHelper.replaySessions.put(watcher.getName(), this);

		if (spawnData != null) {
			watcher.teleport(LocationData.toLocation(spawnData.getLocation()));
		}

		this.session.startSession();

		this.speed = 1;
		
		executeTick(0, ReplayingMode.PLAYING);
		
		this.run = new BukkitRunnable() {
			
			@Override
			public void run() {
				updateProgress();

				if (Replayer.this.paused) return;
				
				Replayer.this.tmpTicks += speed;
				if (Replayer.this.tmpTicks % 1 != 0) return;
				
				if (currentTicks < duration) {

					executeTick(currentTicks++, ReplayingMode.PLAYING);

					if ((currentTicks + 2) < duration && speed == 2)  {
						executeTick(currentTicks++, ReplayingMode.PLAYING);

					}
					
				} else {
					
					stop();
				}
			}
		};
		
		this.run.runTaskTimerAsynchronously(ReplaySystem.getInstance(), 1, 1);

		return true;
	}

	public void executeTick(int tick, ReplayingMode mode) {
		ReplayData data = this.replay.getData();
		if (!data.getActions().isEmpty() && data.getActions().containsKey(tick)) {

			if (tick == 0 && started) return;
			this.started = true;

			List<ActionData> list = data.getActions().get(tick);
			for (ActionData action : list) {

				utils.handleAction(action, data, mode);

				if (action.getType() == ActionType.CUSTOM) {
					if (ReplayAPI.getInstance().getHookManager().isRegistered()) {
						for (IReplayHook hook : ReplayAPI.getInstance().getHookManager().getHooks()) {
							hook.onPlay(action, Replayer.this);
						}
					}
				}
			
			}
			
			if (tick == 0) data.getActions().remove(tick);
		}
	}

	private void updateProgress() {
		ConfigManager.PROGRESS_TYPE.update(this);
	}
	
	private Optional<SpawnData> findFirstSpawn(ReplayData data) {
		return data.getActions().values().stream()
				.flatMap(Collection::stream)
				.filter(action -> action.getPacketData() instanceof SpawnData)
				.map(action -> (SpawnData) action.getPacketData())
				.findFirst();
	}
	
	public void stop() {
		sendMessage(Messages.REPLAYING_FINISHED_WATCHING.getBuilder());
		
		this.run.cancel();
		this.getReplay().getData().getActions().clear();
		
		for (INPC npc : this.npcs.values()) {
			npc.remove();
		}
		
		for (IEntity entity : this.entities.values()) {
			entity.remove();
		}
		
		this.utils.despawn(new ArrayList<Entity>(this.utils.getEntities().values()), null);
				
		this.npcs.clear();
		
		this.replay.setPlaying(false);
		
		if (ConfigManager.WORLD_RESET) this.utils.resetChanges(this.blockChanges);

		this.session.stopSession();
	}
	
	public HashMap<String, INPC> getNPCList() {
		return npcs;
	}
	
	public HashMap<Integer, IEntity> getEntityList() {
		return entities;
	}
	
	public Map<Location, ItemData> getBlockChanges() {
		return blockChanges;
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
		
		
		ReplayHelper.sendTitle(watcher, " ", speed >= 1 ? "§ax" + speed : "§cx" + speed, 10);
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
			this.watcher.sendMessage(Messages.PREFIX.getFullMessage() + message);
		}
	}

	public void sendMessage(MessageBuilder message) {
		if (message != null) {
			message.send(this.watcher);
		}
	}
}
