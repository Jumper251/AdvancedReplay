package me.jumper251.replay.replaysystem.replaying;

import java.util.ArrayList;


import java.util.Collection;
import java.util.HashMap;





import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
import me.jumper251.replay.replaysystem.data.types.ChatData;
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
		
		ReplayHelper.replaySessions.put(watcher.getName(), this);
	}
	
	
	public void start() {
		ReplayData data = this.replay.getData();
		int duration = data.getDuration();
		
		if (data.getActions().containsKey(0)) {
			for (ActionData startData : data.getActions().get(0)) {
				if (startData.getPacketData() instanceof SpawnData) {
					SpawnData spawnData = (SpawnData) startData.getPacketData();
					watcher.teleport(LocationData.toLocation(spawnData.getLocation()));
					break;
				}
			}
		} else {
			Optional<SpawnData> spawnData = findFirstSpawn(data);
			if (spawnData.isPresent()) watcher.teleport(LocationData.toLocation(spawnData.get().getLocation()));
		}
		
		
		this.session.startSession();
		
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
				// Support older replays
				if (action.getType() == ActionType.MESSAGE && action.getPacketData() instanceof ChatData) {
					if (!reversed) {
						ChatData chatData = (ChatData) action.getPacketData();
						sendMessage(chatData);
					}
					continue;
				}

				utils.handleAction(action, data, reversed);

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
		
		// Don't display messages about the start of recording
		if(tick != 0) {
			for (ChatData chatData : data.getMessages().getOrDefault(tick, Collections.emptyList())) {
				if (!reversed) {
					sendMessage(chatData);
				}
			}
		}
	}
	
	private void updateXPBar() {
		int level = currentTicks / 20;
		float percentage = (float) currentTicks / this.replay.getData().getDuration();

		this.watcher.setLevel(level);
		this.watcher.setExp(percentage);
	}
	
	private Optional<SpawnData> findFirstSpawn(ReplayData data) {
		return data.getActions().values().stream()
				.flatMap(Collection::stream)
				.filter(action -> action.getPacketData() instanceof SpawnData)
				.map(action -> (SpawnData) action.getPacketData())
				.findFirst();
	}
	
	public void stop() {
		sendMessage("Replay finished.");
		
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
		
		
		ReplayHelper.sendTitle(watcher, null, speed >= 1 ? "§ax" + speed : "§cx" + speed, 10);
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
	
	public void sendMessage(ChatData chatData) {
		String hoverText = (chatData.getRecipients() == null
				|| chatData.getRecipients().stream().allMatch(recipient -> recipient == null || recipient.isEmpty()))
  				? ""
				: "Received: " + String.join(", ", chatData.getRecipients());
		TextComponent component = new TextComponent(ReplaySystem.PREFIX + "§r");
		for (BaseComponent baseComponent : TextComponent.fromLegacyText(chatData.getMessage())) {
			component.addExtra(baseComponent);
		}
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent(hoverText) }));
		this.watcher.spigot().sendMessage(component);
	}
}
