package me.jumper251.replay.replaysystem.recording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.IReplayHook;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.PacketData;
import me.jumper251.replay.replaysystem.data.types.SignatureData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.utils.NPCManager;
import me.jumper251.replay.utils.ReplayManager;

public class Recorder {

	private List<String> players;
	
	private Replay replay;
	
	private ReplayData data;
	
	BukkitRunnable run;
	private int currentTick;
	private PacketRecorder packetRecorder;
	
	public Recorder(Replay replay, List<Player> players) {
		this.players = new ArrayList<String>();
		this.data = new ReplayData();
		this.replay = replay;
		
		HashMap<String, PlayerWatcher> tmpWatchers = new HashMap<String, PlayerWatcher>();
		for (Player player: players) {
			this.players.add(player.getName());
			tmpWatchers.put(player.getName(), new PlayerWatcher(player.getName()));
		}
		
		this.data.setWatchers(tmpWatchers);
	}
	
	public void start() {
		this.packetRecorder = new PacketRecorder(this);
		this.packetRecorder.register();
		
		for (String names : this.players) {
			if (Bukkit.getPlayer(names) != null) {
				Player player = Bukkit.getPlayer(names);
				createSpawnAction(player, player.getLocation());
			}
		}
		
		this.run = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				for (String name : packetRecorder.getPacketData().keySet()) {
					List<PacketData> list = packetRecorder.getPacketData().get(name);
					for (PacketData packetData : list) {
						ActionData actionData = new ActionData(currentTick, ActionType.PACKET, name, packetData);
						addData(currentTick, actionData);
					}
					
				}
				
				packetRecorder.getPacketData().clear();
				
				if (ReplayAPI.getInstance().getHookManager().isRegistered()) {
					IReplayHook hook = ReplayAPI.getInstance().getHookManager().getHook();
					
					for (String names : players) {
						PacketData customData = hook.onRecord(names);
						if (customData != null) {
							ActionData customAction = new ActionData(currentTick, ActionType.CUSTOM, names, customData);
							addData(currentTick, customAction);
						}
					}
				}
				
				
				Recorder.this.currentTick++;
			}
		};
		
		this.run.runTaskTimerAsynchronously(ReplaySystem.getInstance(), 1, 1);
	}
	
	public void addData(int tick, ActionData actionData) {
		List<ActionData> list = new ArrayList<ActionData>();
		if(this.data.getActions().containsKey(tick)) {
			list = this.data.getActions().get(tick);
		}
		
		list.add(actionData);
		this.data.getActions().put(tick, list);
	}
	
	public void stop(boolean save) {
		this.packetRecorder.unregister();
		this.run.cancel();

		if (save) {
			this.data.setDuration(this.currentTick);
			this.data.setWatchers(new HashMap<String, PlayerWatcher>());
			this.replay.setData(this.data);
			ReplaySaver.save(this.replay);
		} else {
			this.data.getActions().clear();
		}
		
		this.replay.setRecording(false);

		
		if (ReplayManager.activeReplays.containsKey(this.replay.getId())) {
			ReplayManager.activeReplays.remove(this.replay.getId());
		}
	}
	
	public void createSpawnAction(Player player, Location loc) {
		Multimap<String, WrappedSignedProperty> map = WrappedGameProfile.fromPlayer(player).getProperties();
		SignatureData signature = null;
		for (String prop : map.asMap().keySet()) {
			for (WrappedSignedProperty sp : map.get(prop)) {
				signature = new SignatureData(sp.getName(), sp.getValue(), sp.getSignature());
			}
		}
		
		ActionData spawnData = new ActionData(0, ActionType.SPAWN, player.getName(), new SpawnData(player.getUniqueId(), LocationData.fromLocation(loc), signature));
		addData(currentTick, spawnData);
		
		ActionData invData = new ActionData(currentTick, ActionType.PACKET, player.getName(), NPCManager.copyFromPlayer(player, true, true));
		addData(currentTick, invData);
	}
	
	public List<String> getPlayers() {
		return players;
	}
	
	public ReplayData getData() {
		return data;
	}
	
	public int getCurrentTick() {
		return currentTick;
	}
}
