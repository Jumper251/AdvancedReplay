package me.jumper251.replay.replaysystem.replaying;

import org.bukkit.Bukkit;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.ItemConfig;
import me.jumper251.replay.filesystem.ItemConfigOption;
import me.jumper251.replay.filesystem.ItemConfigType;

public class ReplaySession {

	private Replayer replayer;
	
	private Player player;
	
	private ItemStack content[];
	
	private Location start;
	
	private ReplayPacketListener packetListener;
	
	public ReplaySession(Replayer replayer) {
		this.replayer = replayer;
		
		this.player = this.replayer.getWatchingPlayer();
		
		this.packetListener = new ReplayPacketListener(replayer);
		this.startSession();
	}
	
	public void startSession() {
		this.content = this.player.getInventory().getContents();
		this.start = this.player.getLocation();
		
		this.player.setHealth(20);
		this.player.setFoodLevel(20);
		this.player.getInventory().clear();
		
		ItemConfigOption teleport = ItemConfig.getItem(ItemConfigType.TELEPORT);
		ItemConfigOption time = ItemConfig.getItem(ItemConfigType.SPEED);
		ItemConfigOption leave = ItemConfig.getItem(ItemConfigType.LEAVE);
		ItemConfigOption backward = ItemConfig.getItem(ItemConfigType.BACKWARD);
		ItemConfigOption forward = ItemConfig.getItem(ItemConfigType.FORWARD);
		ItemConfigOption pauseResume = ItemConfig.getItem(ItemConfigType.RESUME);

		
		this.player.getInventory().setItem(teleport.getSlot(), ReplayHelper.createItem(teleport));
		this.player.getInventory().setItem(time.getSlot(), ReplayHelper.createItem(time));
		this.player.getInventory().setItem(backward.getSlot(), ReplayHelper.createItem(backward));
		this.player.getInventory().setItem(pauseResume.getSlot(), ReplayHelper.getPauseItem());
		this.player.getInventory().setItem(forward.getSlot(), ReplayHelper.createItem(forward));
		this.player.getInventory().setItem(leave.getSlot(), ReplayHelper.createItem(leave));
		
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
				player.getInventory().clear();
				player.getInventory().setContents(content);
				
				if (player.getGameMode() != GameMode.CREATIVE) {
					player.setFlying(false);
					player.setAllowFlight(false);
				}
				player.setLevel(0);
				player.setExp(0);
				
				player.teleport(start);
				
				
				if (ConfigManager.HIDE_PLAYERS) {
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (all == player) continue;
						
						player.showPlayer(all);
					}
				}
				
			}
		}.runTask(ReplaySystem.getInstance());
	}
	
	public ReplayPacketListener getPacketListener() {
		return packetListener;
	}
}
