package me.jumper251.replay.replaysystem.replaying;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import me.jumper251.replay.api.ReplaySessionStartEvent;
import org.bukkit.Bukkit;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.api.ReplaySessionFinishEvent;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.ItemConfig;
import me.jumper251.replay.filesystem.ItemConfigOption;
import me.jumper251.replay.filesystem.ItemConfigType;

public class ReplaySession {

	private Replayer replayer;
	
	private Player player;
	
	private ItemStack content[];
	
	private int level;
	
	private float xp;
	
	private Location start;
	
	private ReplayPacketListener packetListener;
	
	public ReplaySession(Replayer replayer) {
		this.replayer = replayer;
		
		this.player = this.replayer.getWatchingPlayer();
		
		this.packetListener = new ReplayPacketListener(replayer);
	}
	
	public void startSession() {
		ReplaySessionStartEvent event;
		Bukkit.getPluginManager().callEvent(event = new ReplaySessionStartEvent(this.replayer, this.player));

		if (event.isCancelled()){
			stopSession();
			ReplaySystem.getInstance().getLogger().log(Level.INFO, "Replay start event cancelled.");
			return;
		}

		this.content = this.player.getInventory().getContents();
		this.start = this.player.getLocation();
		
		this.level = this.player.getLevel();
		this.xp = this.player.getExp();

		this.player.setHealth(this.player.getMaxHealth());
		this.player.setFoodLevel(20);
		this.player.getInventory().clear();
		if(!event.isHandlingButtons()) {
			ItemConfigOption teleport = ItemConfig.getItem(ItemConfigType.TELEPORT);
			ItemConfigOption time = ItemConfig.getItem(ItemConfigType.SPEED);
			ItemConfigOption leave = ItemConfig.getItem(ItemConfigType.LEAVE);
			ItemConfigOption backward = ItemConfig.getItem(ItemConfigType.BACKWARD);
			ItemConfigOption forward = ItemConfig.getItem(ItemConfigType.FORWARD);
			ItemConfigOption pauseResume = ItemConfig.getItem(ItemConfigType.PAUSE);

			List<ItemConfigOption> configItems = Arrays.asList(teleport, time, leave, backward, forward, pauseResume);

			configItems.stream()
					.filter(ItemConfigOption::isEnabled)
					.forEach(item -> {
						this.player.getInventory().setItem(item.getSlot(), ReplayHelper.createItem(item));
					});
		}

		this.player.setAllowFlight(true);
		this.player.setFlying(true);
		
		if (ConfigManager.HIDE_PLAYERS) {
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (all == this.player) continue;
				
				this.player.hidePlayer(all);
			}
		}


	}
	
	public void stopSession() {
		if (ReplayHelper.replaySessions.containsKey(this.player.getName())) {
			ReplayHelper.replaySessions.remove(this.player.getName());
		}
		
		this.packetListener.unregister();

		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				resetPlayer();
				
				player.teleport(start);
				
				
				if (ConfigManager.HIDE_PLAYERS) {
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (all == player) continue;
						
						player.showPlayer(all);
					}
				}
				
			}
		}.runTask(ReplaySystem.getInstance());
		
		
		ReplaySessionFinishEvent finishEvent = new ReplaySessionFinishEvent(this.replayer.getReplay(), player, this.replayer);
		Bukkit.getPluginManager().callEvent(finishEvent);
	}
	
	public void resetPlayer() {
		player.getInventory().clear();
		player.getInventory().setContents(content);
		
		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setFlying(false);
			player.setAllowFlight(false);
		}
		
		player.setLevel(level);
		player.setExp(xp);
	}
	
	public ReplayPacketListener getPacketListener() {
		return packetListener;
	}
}
