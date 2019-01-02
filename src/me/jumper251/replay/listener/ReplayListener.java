package me.jumper251.replay.listener;



import java.util.Arrays;




import org.bukkit.Chunk;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.ReplayPacketListener;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.replaysystem.utils.entities.INPC;


public class ReplayListener extends AbstractListener {

	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			if (ReplayHelper.replaySessions.containsKey(p.getName())) {
				e.setCancelled(true);
				
				Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
				if (p.getItemInHand() == null) return;
				
				if (p.getItemInHand().getType().getId() == 397) {
					SkullMeta meta = (SkullMeta) p.getItemInHand().getItemMeta();
					if (meta.getOwner().equalsIgnoreCase("Push_red_button")) {
						replayer.setPaused(!replayer.isPaused());
						ReplayHelper.sendTitle(p, null, "§c❙❙", 20);
					
					}
					
					if (meta.getOwner().equalsIgnoreCase("MHF_ArrowRight")) {
						replayer.getUtils().forward();
						ReplayHelper.sendTitle(p, null, "§a»»", 20);

					}
					if (meta.getOwner().equalsIgnoreCase("MHF_ArrowLeft")) {
						replayer.getUtils().backward();
						ReplayHelper.sendTitle(p, null, "§c««", 20);

					}
				}
				
				if (p.getItemInHand().getType() == Material.SLIME_BLOCK) {
					replayer.setPaused(!replayer.isPaused());
					ReplayHelper.sendTitle(p, null, "§a➤", 20);

				}
				
				if (p.getItemInHand().getType() == Material.WATCH) {
					if (p.isSneaking()) {
						if (replayer.getSpeed() < 1) {
							replayer.setSpeed(1);
						} else if (replayer.getSpeed() == 1) {
							replayer.setSpeed(2);
						}
						
					} else {
						if (replayer.getSpeed() == 2) {
							replayer.setSpeed(1);
						} else if (replayer.getSpeed() ==  1) {
							replayer.setSpeed(0.5D);
						} else if (replayer.getSpeed() == 0.5D) {
							 replayer.setSpeed(0.25D);
						}
					}
					
					
				}
				
				if (p.getItemInHand().getType() == Material.WOOD_DOOR || p.getItemInHand().getType().getId() == 64) {
					replayer.stop();
				}
				
				if (p.getItemInHand().getType() == Material.COMPASS) {
					ReplayHelper.createTeleporter(p, replayer);
				}
				
				if (p.getItemInHand().getType().getId() == 397 || p.getItemInHand().getType() == Material.SLIME_BLOCK) {
					if (replayer.isPaused()) {
						p.getInventory().setItem(4, ReplayHelper.getResumeItem());
					} else {
						p.getInventory().setItem(4, ReplayHelper.getPauseItem());
					}
				}
				
				
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (ReplayHelper.replaySessions.containsKey(p.getName())) {
				e.setCancelled(true);
				if (e.getInventory().getName().equalsIgnoreCase("§7Teleporter")) {
					Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
					
					if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getType().getId() == 397) {
						String owner = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", "");
						if (replayer.getNPCList().containsKey(owner)) {
							INPC npc = replayer.getNPCList().get(owner);
							p.teleport(npc.getLocation());
						}
					}
				}
				
				
			}
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e){
		Player p = (Player) e.getEntity();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			e.setFoodLevel(20);
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (ReplayHelper.replaySessions.containsKey(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
			
			for (INPC npc : replayer.getNPCList().values()) {
				npc.despawn();
				npc.respawn(p);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();	
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
			p.getInventory().clear();
			replayer.stop();
			
		}

	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			Chunk oldChunk = e.getFrom().getChunk();
			Chunk newChunk = e.getTo().getChunk();
			
			if (oldChunk.getWorld() != newChunk.getWorld() || oldChunk.getX() != newChunk.getX() || oldChunk.getZ() != newChunk.getZ()) {
				Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
				
				for (INPC npc : replayer.getNPCList().values()) {

					if (ReplayHelper.isInRange(npc.getLocation(), p.getLocation())) {
						if (!Arrays.asList(npc.getVisible()).contains(p)) {
							npc.respawn(p);
						}
					} else {
						if (Arrays.asList(npc.getVisible()).contains(p)) {
							npc.despawn();
						}
					}
				}
			}
			
		}
		
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			final Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for (INPC npc : replayer.getNPCList().values()) {
						
						npc.despawn();
						
						if (ReplayHelper.isInRange(p.getLocation(), npc.getLocation())) {
							npc.respawn(p);
						}
					}
					
				}
			}.runTaskLater(ReplaySystem.getInstance(), 20);
		}
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			ReplayPacketListener packetListener = ReplayHelper.replaySessions.get(p.getName()).getSession().getPacketListener();
			
			if (packetListener.getPrevious() != -1) {
				packetListener.setCamera(p, p.getEntityId(), packetListener.getPrevious());
				
				p.setAllowFlight(true);
			}
		}


	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(ConfigManager.UPDATE_NOTIFY){
			if(ReplaySystem.updater.isVersionAvailable() && p.hasPermission("replay.admin")){
				p.sendMessage(ReplaySystem.PREFIX + "An update is available: https://www.spigotmc.org/resources/advancedreplay-1-9-1-12.52849/");
			}
		}
	}
}
