package me.jumper251.replay.replaysystem.replaying;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;

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
		
		ItemStack teleporter = ReplayHelper.creatItem(Material.COMPASS, "§7Teleport");
		ItemStack time = ReplayHelper.creatItem(Material.WATCH, "§cSlow §8[§eRight§8] §aFast §8[§eShift Right§8]");
		ItemStack leave = ReplayHelper.creatItem(Material.WOOD_DOOR, "§7Leave replay");
		ItemStack backward = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		ItemStack forward = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		
		SkullMeta backMeta = (SkullMeta) backward.getItemMeta();
		backMeta.setDisplayName("§c« §e10 seconds");
		backMeta.setOwner("MHF_ArrowLeft");
		backward.setItemMeta(backMeta);
		
		SkullMeta forwardMeta = (SkullMeta) forward.getItemMeta();
		forwardMeta.setDisplayName("§a» §e10 seconds");
		forwardMeta.setOwner("MHF_ArrowRight");
		forward.setItemMeta(forwardMeta);

		
		this.player.getInventory().setItem(0, teleporter);
		this.player.getInventory().setItem(1, time);
		this.player.getInventory().setItem(3, backward);
		this.player.getInventory().setItem(4, ReplayHelper.getPauseItem());
		this.player.getInventory().setItem(5, forward);
		this.player.getInventory().setItem(8, leave);
		
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
