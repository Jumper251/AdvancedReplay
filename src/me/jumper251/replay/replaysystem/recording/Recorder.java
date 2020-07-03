package me.jumper251.replay.replaysystem.recording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.jumper251.replay.replaysystem.data.types.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.IReplayHook;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.ReplayInfo;
import me.jumper251.replay.replaysystem.utils.NPCManager;
import me.jumper251.replay.utils.ReplayManager;
import me.jumper251.replay.utils.fetcher.JsonData;
import me.jumper251.replay.utils.fetcher.PlayerInfo;
import me.jumper251.replay.utils.fetcher.SkinInfo;
import me.jumper251.replay.utils.fetcher.WebsiteFetcher;

public class Recorder {

	private List<String> players;
	
	private Replay replay;
	
	private ReplayData data;
	
	private BukkitRunnable run;
	private int currentTick;
	private PacketRecorder packetRecorder;
	
	private CommandSender sender;
	
	public Recorder(Replay replay, List<Player> players, CommandSender sender) {
		this.players = new ArrayList<String>();
		this.data = new ReplayData();
		this.replay = replay;
		this.sender = sender;
		
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
				
				HashMap<String, List<PacketData>> tmpMap = packetRecorder.getPacketData();
				
				for (String name : tmpMap.keySet()) {
					List<PacketData> list = new ArrayList<>(tmpMap.get(name));
					for (Iterator<PacketData> it = list.iterator(); it.hasNext();) {
						PacketData packetData = it.next();
						
						if (packetData instanceof BlockChangeData && !ConfigManager.RECORD_BLOCKS) continue;
						if (packetData instanceof EntityItemData && !ConfigManager.RECORD_ITEMS) continue;
						if ((packetData instanceof EntityData || packetData instanceof EntityMovingData || packetData instanceof EntityAnimationData) && !ConfigManager.RECORD_ENTITIES) continue;
						if (packetData instanceof ChatData && !ConfigManager.RECORD_CHAT) continue;


						ActionData actionData = new ActionData(currentTick, ActionType.PACKET, name, packetData);
						addData(currentTick, actionData);
						

					}
					
				}

				packetRecorder.getPacketData().keySet().removeAll(tmpMap.keySet());
		
			

				if (ReplayAPI.getInstance().getHookManager().isRegistered()) {
					
					for (IReplayHook hook : ReplayAPI.getInstance().getHookManager().getHooks()) {
						for (String names : players) {
							PacketData customData = hook.onRecord(names);
							if (customData != null) {
								ActionData customAction = new ActionData(currentTick, ActionType.CUSTOM, names, customData);
								addData(currentTick, customAction);
							}
						}
					}
				}
				
				
				Recorder.this.currentTick++;
				
				if ((Recorder.this.currentTick / 20) >= ConfigManager.MAX_LENGTH) stop(ConfigManager.SAVE_STOP);
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
			this.data.setCreator(this.sender.getName());
			this.data.setWatchers(new HashMap<String, PlayerWatcher>());
			this.replay.setData(this.data);
			this.replay.setReplayInfo(new ReplayInfo(this.replay.getId(), this.sender.getName(), System.currentTimeMillis(), this.currentTick));
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
		SignatureData[] signArr = new SignatureData[1];
		
		if (!Bukkit.getOnlineMode() && ConfigManager.USE_OFFLINE_SKINS) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					PlayerInfo info = (PlayerInfo) WebsiteFetcher.getJson("https://api.mojang.com/users/profiles/minecraft/" + player.getName(), true, new JsonData(true, new PlayerInfo()));

					if (info != null) {		
						SkinInfo skin = (SkinInfo) WebsiteFetcher.getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + info.getId() + "?unsigned=false", true, new JsonData(true, new SkinInfo()));
						
						Map<String, String> props = skin.getProperties().get(0);
						signArr[0] =  new SignatureData(props.get("name"), props.get("value"), props.get("signature"));
					}
					
					ActionData spawnData = new ActionData(0, ActionType.SPAWN, player.getName(), new SpawnData(player.getUniqueId(), LocationData.fromLocation(loc), signArr[0]));
					addData(currentTick, spawnData);
				
					ActionData invData = new ActionData(0, ActionType.PACKET, player.getName(), NPCManager.copyFromPlayer(player, true, true));
					addData(currentTick, invData);
				}
			}.runTaskAsynchronously(ReplaySystem.getInstance());
		}
		
		Multimap<String, WrappedSignedProperty> map = WrappedGameProfile.fromPlayer(player).getProperties();
		for (String prop : map.asMap().keySet()) {
			for (WrappedSignedProperty sp : map.get(prop)) {
				signArr[0] =  new SignatureData(sp.getName(), sp.getValue(), sp.getSignature());
			}
		}
		
		if (!ConfigManager.USE_OFFLINE_SKINS || Bukkit.getOnlineMode()) {
			ActionData spawnData = new ActionData(0, ActionType.SPAWN, player.getName(), new SpawnData(player.getUniqueId(), LocationData.fromLocation(loc), signArr[0]));
			addData(currentTick, spawnData);
		
			ActionData invData = new ActionData(currentTick, ActionType.PACKET, player.getName(), NPCManager.copyFromPlayer(player, true, true));
			addData(currentTick, invData);
		}
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
