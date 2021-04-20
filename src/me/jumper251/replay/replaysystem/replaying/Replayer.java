package me.jumper251.replay.replaysystem.replaying;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;





import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import com.google.common.collect.Sets;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.IReplayHook;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.data.types.ChatData;
import me.jumper251.replay.replaysystem.utils.entities.IEntity;
import me.jumper251.replay.replaysystem.utils.entities.INPC;



public class Replayer {

	private HashMap<String, INPC> npcs;
	
	private HashMap<Integer, IEntity> entities;
	
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
			Map<String, Set<String>> messageRecipients = new HashMap<>();
			for (ActionData action : list) {
				if(action.getType() == ActionType.PACKET && action.getPacketData() instanceof ChatData) {
					ChatData chat = (ChatData) action.getPacketData();
					if(messageRecipients.containsKey(chat.getMessage())) {
						messageRecipients.get(chat.getMessage()).add(chat.getRecipient());
					} else {
						messageRecipients.put(chat.getMessage(), Sets.newHashSet(chat.getRecipient()));
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
			
			if(!reversed) {
				messageRecipients.forEach((message, recipients) -> {
					String hoverText = recipients.stream().allMatch(recipient -> recipient == null || recipient.isEmpty())
							? ""
							: "Received: " + String.join(", ", recipients);
					TextComponent component = new TextComponent(ReplaySystem.PREFIX + "§r");
					for (BaseComponent baseComponent : TextComponent.fromLegacyText(message)) {
						component.addExtra(baseComponent);
					}
					component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent(hoverText) }));
					this.watcher.spigot().sendMessage(component);
				});
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
		this.session.stopSession();
	}
	
	public HashMap<String, INPC> getNPCList() {
		return npcs;
	}
	
	public HashMap<Integer, IEntity> getEntityList() {
		return entities;
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
}
