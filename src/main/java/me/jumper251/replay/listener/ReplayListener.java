package me.jumper251.replay.listener;



import java.util.Arrays;


import me.jumper251.replay.api.ReplaySessionFinishEvent;
import me.jumper251.replay.dev.mrflyn.extended.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.ItemConfig;
import me.jumper251.replay.filesystem.ItemConfigOption;
import me.jumper251.replay.filesystem.ItemConfigType;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.ReplayPacketListener;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.replaysystem.utils.entities.INPC;


public class ReplayListener extends AbstractListener {

	@EventHandler
	public void onReplayStop(ReplaySessionFinishEvent e){
		//TODO: fix
		String name = e.getPlayer().getWorld().getName();
		ReplaySystem.getInstance().getLogger().info(name);
		ReplaySystem.getInstance().getLogger().info(WorldHandler.WORLD_WATCHER.toString());
		if (!WorldHandler.WORLD_WATCHER.containsKey(name))return;
		WorldHandler.WORLD_WATCHER.put(name, WorldHandler.WORLD_WATCHER.get(name)-1);
		ReplaySystem.getInstance().getLogger().info(WorldHandler.WORLD_WATCHER.toString());
		// UNLOAD IF WATCHERS<1;
		if (WorldHandler.WORLD_WATCHER.get(name)<1){
			Bukkit.getScheduler().runTask(ReplaySystem.getInstance(), ()->{
				ReplaySystem.getInstance().worldManger.unloadWorld(name);
			});
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			if (ReplayHelper.replaySessions.containsKey(p.getName())) {
				e.setCancelled(true);
				
				Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
				if (p.getItemInHand() == null) return;
				if (p.getItemInHand().getItemMeta() == null) return;
				
				ItemMeta meta = p.getItemInHand().getItemMeta();
				ItemConfigType itemType = ItemConfig.getByIdAndName(p.getItemInHand().getType(), meta.getDisplayName().replaceAll("§", "&"));
				
				if (itemType == ItemConfigType.PAUSE) {
					replayer.setPaused(!replayer.isPaused());
					ReplayHelper.sendTitle(p, " ", "§c❙❙", 20);
				}
					
				if (itemType == ItemConfigType.FORWARD) {
					replayer.getUtils().forward();
					ReplayHelper.sendTitle(p, " ", "§a»»", 20);

				}
				if (itemType == ItemConfigType.BACKWARD) {
					replayer.getUtils().backward();
					ReplayHelper.sendTitle(p, " ", "§c««", 20);

				}
				
				
				if (itemType == ItemConfigType.RESUME) {
					replayer.setPaused(!replayer.isPaused());
					ReplayHelper.sendTitle(p, " ", "§a➤", 20);

				}
				
				if (itemType == ItemConfigType.SPEED) {
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
				
				if (itemType == ItemConfigType.LEAVE) {
					replayer.stop();
				}
				
				if (itemType == ItemConfigType.TELEPORT) {
					ReplayHelper.createTeleporter(p, replayer);
				}
				
				ItemConfigOption pauseResume = ItemConfig.getItem(ItemConfigType.RESUME);
				
				if (itemType == ItemConfigType.PAUSE || itemType == ItemConfigType.RESUME) {
					if (replayer.isPaused()) {
						p.getInventory().setItem(pauseResume.getSlot(), ReplayHelper.getResumeItem());
					} else {
						p.getInventory().setItem(pauseResume.getSlot(), ReplayHelper.getPauseItem());
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
				
				if (e.getView().getTitle().equalsIgnoreCase("§7Teleporter")) {
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
			replayer.stop();
			replayer.getSession().resetPlayer();

		}

	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (ReplayHelper.replaySessions.containsKey(p.getName())) {
			e.setCancelled(true);
		}
		
		boolean isReplayItem = ReplayHelper.replaySessions.values()
				.stream()
				.anyMatch(replayer -> replayer.getUtils().getEntities().containsValue(e.getItem()));
		
		if (isReplayItem) {
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
				p.sendMessage(ReplaySystem.PREFIX + "An update is available: https://www.spigotmc.org/resources/advancedreplay-1-8-1-15.52849/");
			}
		}
	}
}
