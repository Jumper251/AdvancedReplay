package me.jumper251.replay.replaysystem.replaying;



import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.MessageBuilder;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.types.AnimationData;
import me.jumper251.replay.replaysystem.data.types.BedEnterData;
import me.jumper251.replay.replaysystem.data.types.BlockChangeData;
import me.jumper251.replay.replaysystem.data.types.EntityActionData;
import me.jumper251.replay.replaysystem.data.types.EntityData;
import me.jumper251.replay.replaysystem.data.types.InvData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.MetadataUpdate;
import me.jumper251.replay.replaysystem.data.types.MovingData;
import me.jumper251.replay.replaysystem.data.types.ProjectileData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.recording.PlayerWatcher;
import me.jumper251.replay.replaysystem.utils.INPC;
import me.jumper251.replay.replaysystem.utils.MetadataBuilder;
import me.jumper251.replay.replaysystem.utils.NPCManager;
import me.jumper251.replay.replaysystem.utils.PacketNPC;
import me.jumper251.replay.replaysystem.utils.PacketNPCOld;
import me.jumper251.replay.utils.MathUtils;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class ReplayingUtils {

	private Replayer replayer;
	
	private ActionData lastSpawnAction;
	
	private HashMap<Integer, Entity> entities;
	
	public ReplayingUtils(Replayer replayer) {
		this.replayer = replayer;
		this.entities = new HashMap<Integer, Entity>();
	}
	
	public void handleAction(ActionData action, ReplayData data, boolean reversed) {
				
		if (action.getType() == ActionType.SPAWN) {
			if (!reversed) {
				spawnNPC(action);
			} else if (reversed && replayer.getNPCList().containsKey(action.getName())){
				INPC npc = this.replayer.getNPCList().get(action.getName());
				npc.remove();
				replayer.getNPCList().remove(action.getName());

			}
		}	
		
		if (action.getType() == ActionType.PACKET && this.replayer.getNPCList().containsKey(action.getName())) {
			INPC npc = this.replayer.getNPCList().get(action.getName());

			
			if (action.getPacketData() instanceof MovingData) {
				MovingData movingData = (MovingData) action.getPacketData();
				npc.teleport(new Location(npc.getOrigin().getWorld(), movingData.getX(), movingData.getY(), movingData.getZ()), true);
				npc.look(movingData.getYaw(), movingData.getPitch());
		
			}
			
			if (action.getPacketData() instanceof EntityActionData) {
				EntityActionData eaData = (EntityActionData) action.getPacketData();
				if (eaData.getAction() == PlayerAction.START_SNEAKING) {
					data.getWatcher(action.getName()).setSneaking(reversed ? false : true);
					npc.setData(data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData())));
				} else if (eaData.getAction() == PlayerAction.STOP_SNEAKING) {
					data.getWatcher(action.getName()).setSneaking(reversed);
					npc.setData(data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData())));
				}
				npc.updateMetadata();
				
				
			}
			
			if (action.getPacketData() instanceof AnimationData) {
				AnimationData animationData = (AnimationData) action.getPacketData();
				npc.animate(animationData.getId());
				
				if (animationData.getId() == 1) {
					replayer.getWatchingPlayer().playSound(npc.getLocation(), Sound.ENTITY_PLAYER_HURT, 5F, 5.0F);
				}
			}
			
			if (action.getPacketData() instanceof InvData) {
				InvData invData = (InvData) action.getPacketData();
				
				if (!VersionUtil.isCompatible(VersionEnum.V1_8)) {
					List<WrapperPlayServerEntityEquipment> equipment = NPCManager.updateEquipment(npc.getId(), invData);
					npc.setLastEquipment(equipment);
					
					for (WrapperPlayServerEntityEquipment packet : equipment) {
						packet.sendPacket(replayer.getWatchingPlayer());
					}
				} else {
					for (com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet : NPCManager.updateEquipmentOld(npc.getId(), invData)) {
						packet.sendPacket(replayer.getWatchingPlayer());
					}
				}
			}
			
			if (action.getPacketData() instanceof MetadataUpdate) {
				MetadataUpdate update = (MetadataUpdate) action.getPacketData();
				
				data.getWatcher(action.getName()).setBurning(!reversed ? update.isBurning() : false);
				data.getWatcher(action.getName()).setBlocking(!reversed ? update.isBlocking() : false);
		
				WrappedDataWatcher dataWatcher = data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData()));
				npc.setData(dataWatcher);
				
				npc.updateMetadata();
				
			
			}
			
			if (action.getPacketData() instanceof ProjectileData) {
				ProjectileData projectile = (ProjectileData) action.getPacketData();
				
				spawnProjectile(projectile, replayer.getWatchingPlayer().getWorld());
			}
			
			if (action.getPacketData() instanceof BlockChangeData) {
				BlockChangeData blockChange = (BlockChangeData) action.getPacketData();
				
				if (reversed) {
					blockChange = new BlockChangeData(blockChange.getLocation(), blockChange.getAfter(), blockChange.getBefore());
				}
				
				setBlockChange(blockChange, npc.getId());
			}
			
			if (action.getPacketData() instanceof BedEnterData) {
				BedEnterData bed = (BedEnterData) action.getPacketData();
				
				npc.sleep(LocationData.toLocation(bed.getLocation()));
			}
			
			if (action.getPacketData() instanceof EntityData) {
				EntityData entityData = (EntityData) action.getPacketData();
				
				if (entityData.getAction() == 0 && !reversed) {
					spawnItemStack(entityData);
				} else {
					if (entities.containsKey(entityData.getId())) {
						despawn(Arrays.asList(new Entity[] { entities.get(entityData.getId()) }));
						
						entities.remove(entityData.getId());
					}
				}
			}
			

		}
		
		if (action.getType() == ActionType.DESPAWN || action.getType() == ActionType.DEATH) {
			if (!reversed  && replayer.getNPCList().containsKey(action.getName())) {
				INPC npc = this.replayer.getNPCList().get(action.getName());
				npc.remove();
				replayer.getNPCList().remove(action.getName());
				
				if (action.getType() == ActionType.DESPAWN) {
					replayer.sendMessage(new MessageBuilder(ConfigManager.LEAVE_MESSAGE)
							.set("name", action.getName())
							.build());
				} else {
					replayer.sendMessage(new MessageBuilder(ConfigManager.DEATH_MESSAGE)
							.set("name", action.getName())
							.build());
				}
				
			} else {

				if (lastSpawnAction != null) {
					spawnNPC(lastSpawnAction);
				}
			}

		}
	}
	
	public void forward() {
		this.replayer.setPaused(true);
		int currentTick = this.replayer.getCurrentTicks();
		int forwardTicks = currentTick + (10 * 20);
		int duration = this.replayer.getReplay().getData().getDuration();
		
		if ((forwardTicks + 2) < duration) {
			for (int i = currentTick; i < forwardTicks; i++) {
				this.replayer.executeTick(i, false);
			}
			this.replayer.setCurrentTicks(forwardTicks);
			this.replayer.setPaused(false);
		}
	}
	
	public void backward() {
		this.replayer.setPaused(true);
		int currentTick = this.replayer.getCurrentTicks();
		int backwardTicks = currentTick - (10 * 20);
		
		if ((backwardTicks - 2) > 0) {
			for (int i = currentTick; i > backwardTicks; i--) {
				
				this.replayer.executeTick(i, true);
			}
			
			this.replayer.setCurrentTicks(backwardTicks);
			this.replayer.setPaused(false);
		}
	}
	
	private void spawnNPC(ActionData action) {
		SpawnData spawnData = (SpawnData)action.getPacketData();
		INPC npc = !VersionUtil.isCompatible(VersionEnum.V1_8) ? new PacketNPC(MathUtils.randInt(10000, 20000), spawnData.getUuid(), action.getName()) : new PacketNPCOld(MathUtils.randInt(10000, 20000), spawnData.getUuid(), action.getName());
		this.replayer.getNPCList().put(action.getName(), npc);
		this.replayer.getReplay().getData().getWatchers().put(action.getName(), new PlayerWatcher(action.getName()));

		int tabMode = Bukkit.getPlayer(action.getName()) != null ? 0 : 2;
		Location spawn = LocationData.toLocation(spawnData.getLocation());
		
		if(VersionUtil.isCompatible(VersionEnum.V1_8)) {
			npc.setData(new MetadataBuilder(this.replayer.getWatchingPlayer()).resetValue().getData());
		} else {
			npc.setData(new MetadataBuilder(this.replayer.getWatchingPlayer()).setArrows(0).resetValue().getData());

		}
		if (spawnData.getSignature() != null && Bukkit.getPlayer(action.getName()) == null) {
			WrappedGameProfile profile = new WrappedGameProfile(spawnData.getUuid(), action.getName());
			WrappedSignedProperty signed = new WrappedSignedProperty(spawnData.getSignature().getName(), spawnData.getSignature().getValue(), spawnData.getSignature().getSignature());
			profile.getProperties().put(spawnData.getSignature().getName(), signed);
			npc.setProfile(profile);
		}

		npc.spawn(spawn, tabMode, this.replayer.getWatchingPlayer());
		npc.look(spawnData.getLocation().getYaw(), spawnData.getLocation().getPitch());

		this.lastSpawnAction = action;
	  
	}
	
	private void spawnProjectile(ProjectileData projData, World world) {
		if (projData.getType() != EntityType.FISHING_HOOK) {
			new BukkitRunnable() {
			
				@Override
				public void run() {
					Projectile proj = (Projectile) world.spawnEntity(LocationData.toLocation(projData.getSpawn()), projData.getType());
					proj.setVelocity(LocationData.toLocation(projData.getVelocity()).toVector());
				
				}
			}.runTask(ReplaySystem.getInstance());
		} else {
			
			
			
		}
	}
	
	private void setBlockChange(BlockChangeData blockChange, int entID) {
		final Location loc = LocationData.toLocation(blockChange.getLocation());
		
		new BukkitRunnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (blockChange.getAfter().getId() == 0 && blockChange.getBefore().getId() != 0 && Material.getMaterial(blockChange.getBefore().getId()) != Material.FIRE && blockChange.getBefore().getId() != 11 && blockChange.getBefore().getId() != 9) {
					loc.getWorld().playEffect(loc, Effect.STEP_SOUND, blockChange.getBefore().getId(), 15);
					
				}
				int id = blockChange.getAfter().getId();
				int subId = blockChange.getAfter().getSubId();
				
				if (id == 9) id = 8;
				if (id == 11) id = 10;
				
				if (ConfigManager.REAL_CHANGES) {
					loc.getBlock().setTypeIdAndData(id, (byte) subId, true);
				} else {
					replayer.getWatchingPlayer().sendBlockChange(loc, id, (byte) subId);
				}
				
				
			}
		}.runTask(ReplaySystem.getInstance());
	}
	
	private void spawnItemStack(EntityData entityData) {
		final Location loc = LocationData.toLocation(entityData.getLocation());
		
		new BukkitRunnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Item item = loc.getWorld().dropItemNaturally(loc, new ItemStack(entityData.getItemData().getId(), 1, (short) entityData.getItemData().getSubId()));
				item.setVelocity(LocationData.toLocation(entityData.getVelocity()).toVector());
				
				entities.put(entityData.getId(), item);
				
			}
		}.runTask(ReplaySystem.getInstance());
	}
	
	public void despawn(List<Entity> entities) {
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Entity en : entities) {
					en.remove();
				}
			}
		}.runTask(ReplaySystem.getInstance());
	}
	
	public HashMap<Integer, Entity> getEntities() {
		return entities;
	}
}
