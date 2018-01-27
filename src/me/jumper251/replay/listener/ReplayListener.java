package me.jumper251.replay.listener;


import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.SkullMeta;

import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import me.jumper251.replay.replaysystem.utils.INPC;

public class ReplayListener extends AbstractListener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			if (ReplayHelper.replaySessions.containsKey(p.getName())) {
				e.setCancelled(true);
				
				Replayer replayer = ReplayHelper.replaySessions.get(p.getName());
				if (p.getItemInHand() == null) return;
				
				if (p.getItemInHand().getTypeId() == 397) {
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
				
				if (p.getItemInHand().getType() == Material.WOOD_DOOR) {
					replayer.stop();
				}
				
				if (p.getItemInHand().getType() == Material.COMPASS) {
					ReplayHelper.createTeleporter(p, replayer);
				}
				
				if (p.getItemInHand().getTypeId() == 397 || p.getItemInHand().getType() == Material.SLIME_BLOCK) {
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
					
					if (e.getCurrentItem().getTypeId() == 397) {
						SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
						if (replayer.getNPCList().containsKey(meta.getOwner())) {
							INPC npc = replayer.getNPCList().get(meta.getOwner());
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
				npc.respawn();
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
}
