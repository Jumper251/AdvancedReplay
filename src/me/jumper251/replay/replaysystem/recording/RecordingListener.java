package me.jumper251.replay.replaysystem.recording;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.jumper251.replay.listener.AbstractListener;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.types.AnimationData;
import me.jumper251.replay.replaysystem.data.types.BlockChangeData;
import me.jumper251.replay.replaysystem.data.types.InvData;
import me.jumper251.replay.replaysystem.data.types.ItemData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.MetadataUpdate;
import me.jumper251.replay.replaysystem.data.types.ProjectileData;
import me.jumper251.replay.replaysystem.utils.ItemUtils;
import me.jumper251.replay.replaysystem.utils.NPCManager;

public class RecordingListener extends AbstractListener {

	
	private PacketRecorder packetRecorder;
	private Recorder recorder;
	
	public RecordingListener(PacketRecorder packetRecorder) {
		this.packetRecorder = packetRecorder;
		
		this.recorder = this.packetRecorder.getRecorder();
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
    			if (recorder.getPlayers().contains(p.getName())) {
    				this.packetRecorder.addData(p.getName(), NPCManager.copyFromPlayer(p, true, true));
    			}

		}
		
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (recorder.getPlayers().contains(p.getName())) {
			ItemStack stack = p.getInventory().getItem(e.getNewSlot());
			InvData data = NPCManager.copyFromPlayer(p, true, true);
			data.setMainHand(NPCManager.fromItemStack(stack));
			
			this.packetRecorder.addData(p.getName(), data);
			
			if (recorder.getData().getWatcher(p.getName()).isBlocking()) {
   				recorder.getData().getWatcher(p.getName()).setBlocking(false);
   				this.packetRecorder.addData(p.getName(), new MetadataUpdate(recorder.getData().getWatcher(p.getName()).isBurning(), false));
			}
		}

		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler 
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (recorder.getPlayers().contains(p.getName())) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				boolean isInteractable = e.getClickedBlock() != null && ItemUtils.isInteractable(e.getClickedBlock().getType());
				if(e.getItem() != null && ItemUtils.isUsable(e.getItem().getType()) && (!isInteractable || p.isSneaking())) {
					if (!this.recorder.getData().getWatcher(p.getName()).isBlocking()) {
						this.recorder.getData().getWatcher(p.getName()).setBlocking(true);
						this.packetRecorder.addData(p.getName(), new MetadataUpdate(this.recorder.getData().getWatcher(p.getName()).isBurning(), true));
					
					}
				}
				
				
				if (NPCManager.isArmor(e.getItem()) && !NPCManager.wearsArmor(p, NPCManager.getArmorType(e.getItem()))) {
					InvData data = NPCManager.copyFromPlayer(p, true, true);
					String armorType = NPCManager.getArmorType(e.getItem());
					if (armorType != null) {
						if (armorType.equals("head")) data.setHead(NPCManager.fromItemStack(e.getItem()));
						if (armorType.equals("chest")) data.setChest(NPCManager.fromItemStack(e.getItem()));
						if (armorType.equals("leg")) data.setLeg(NPCManager.fromItemStack(e.getItem()));
						if (armorType.equals("boots")) data.setBoots(NPCManager.fromItemStack(e.getItem()));
					}
					data.setMainHand(null);
					
					this.packetRecorder.addData(p.getName(), data);

				}
			}
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
	            if (p.getTargetBlock((Set<Material>) null, 5).getType() == Material.FIRE){
	    				LocationData location = LocationData.fromLocation(p.getTargetBlock((Set<Material>) null, 5).getLocation());

	    				ItemData before = new ItemData(Material.FIRE.getId(), 0);
	    				ItemData after = new ItemData(0, 0);
	    			
	    				this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
	            }

			}
		}

	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		if (recorder.getPlayers().contains(p.getName())) {
			if (recorder.getData().getWatcher(p.getName()).isBlocking()) {
   				recorder.getData().getWatcher(p.getName()).setBlocking(false);
   				this.packetRecorder.addData(p.getName(), new MetadataUpdate(recorder.getData().getWatcher(p.getName()).isBurning(), false));
   				
				InvData data = NPCManager.copyFromPlayer(p, true, true);
				if (p.getItemInHand() != null && p.getItemInHand().getAmount() <= 1) {
					data.setMainHand(null);
				}
				
				this.packetRecorder.addData(p.getName(), data);

   			}
		}
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			if (recorder.getPlayers().contains(p.getName())) {
				
				this.packetRecorder.addData(p.getName(), new AnimationData(1));
				
				if (p.getFireTicks() > 20 && !this.recorder.getData().getWatcher(p.getName()).isBurning()) {
					this.recorder.getData().getWatcher(p.getName()).setBurning(true);
					this.packetRecorder.addData(p.getName(), new MetadataUpdate(true, this.recorder.getData().getWatcher(p.getName()).isBlocking()));
				} else if (p.getFireTicks() <= 20 && this.recorder.getData().getWatcher(p.getName()).isBurning()){
					this.recorder.getData().getWatcher(p.getName()).setBurning(false);
					this.packetRecorder.addData(p.getName(), new MetadataUpdate(false, this.recorder.getData().getWatcher(p.getName()).isBlocking()));
				}
			}
		}
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {
			this.recorder.addData(this.recorder.getCurrentTick(), new ActionData(0, ActionType.DEPSAWN, p.getName(), null));
			this.recorder.getPlayers().remove(p.getName());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (this.recorder.getPlayers().contains(p.getName())) {
			this.recorder.addData(this.recorder.getCurrentTick(), new ActionData(0, ActionType.DEPSAWN, p.getName(), null));
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {

			this.recorder.createSpawnAction(p, e.getRespawnLocation());
		}

	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onThrow(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {
			InvData data = NPCManager.copyFromPlayer(p, true, true);
			if (data.getMainHand() != null && p.getItemInHand() != null && p.getItemInHand().getAmount() <= 1 && p.getItemInHand().getType() == Material.getMaterial(data.getMainHand().getId())) {
				data.setMainHand(null);
			}
			
			this.packetRecorder.addData(p.getName(), data);
		}
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onLaunch(ProjectileLaunchEvent e) {
		Projectile proj = e.getEntity();
		if (proj.getShooter() instanceof Player) {
			Player p = (Player)proj.getShooter();
			if (this.recorder.getPlayers().contains(p.getName())) {
				LocationData spawn = LocationData.fromLocation(p.getEyeLocation());
				LocationData velocity = LocationData.fromLocation(proj.getVelocity().toLocation(p.getWorld()));
				
				this.packetRecorder.addData(p.getName(),  new ProjectileData(spawn, velocity, proj.getType()));
				this.packetRecorder.addData(p.getName(), NPCManager.copyFromPlayer(p, true, true));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {
			LocationData location = LocationData.fromLocation(e.getBlockPlaced().getLocation());
			
			ItemData before = new ItemData(e.getBlockReplacedState().getTypeId(), e.getBlockReplacedState().getData().getData());
			ItemData after = new ItemData(e.getBlockPlaced().getTypeId(), e.getBlockPlaced().getData());
			
			this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
		}

	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {
			LocationData location = LocationData.fromLocation(e.getBlock().getLocation());

			ItemData before = new ItemData(e.getBlock().getTypeId(), e.getBlock().getData());
			ItemData after = new ItemData(0, 0);
			
			this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));

		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFill(PlayerBucketFillEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {
			LocationData location = LocationData.fromLocation(e.getBlockClicked().getLocation());

			ItemData before = new ItemData(e.getBlockClicked().getState().getTypeId(), e.getBlockClicked().getState().getData().getData());
			ItemData after = new ItemData(0, 0);
			
			this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEmpty(PlayerBucketEmptyEvent e) {
		Player p = e.getPlayer();
		if (this.recorder.getPlayers().contains(p.getName())) {
			Block block = e.getBlockClicked().getRelative(e.getBlockFace());
			LocationData location = LocationData.fromLocation(block.getLocation());
			
			ItemData before = new ItemData(block.getTypeId(), block.getData());
			ItemData after = new ItemData(e.getBucket() == Material.WATER_BUCKET ? 9 : 11, 0);
			
			this.packetRecorder.addData(p.getName(), new BlockChangeData(location, before, after));
		}
	}
}
