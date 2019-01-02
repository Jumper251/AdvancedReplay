package me.jumper251.replay.replaysystem.replaying;



import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
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
import me.jumper251.replay.replaysystem.data.types.EntityAnimationData;
import me.jumper251.replay.replaysystem.data.types.EntityData;
import me.jumper251.replay.replaysystem.data.types.EntityItemData;
import me.jumper251.replay.replaysystem.data.types.EntityMovingData;
import me.jumper251.replay.replaysystem.data.types.FishingData;
import me.jumper251.replay.replaysystem.data.types.InvData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.MetadataUpdate;
import me.jumper251.replay.replaysystem.data.types.MovingData;
import me.jumper251.replay.replaysystem.data.types.ProjectileData;
import me.jumper251.replay.replaysystem.data.types.SpawnData;
import me.jumper251.replay.replaysystem.data.types.VelocityData;
import me.jumper251.replay.replaysystem.data.types.WorldChangeData;
import me.jumper251.replay.replaysystem.recording.PlayerWatcher;
import me.jumper251.replay.replaysystem.utils.MetadataBuilder;
import me.jumper251.replay.replaysystem.utils.NPCManager;
import me.jumper251.replay.replaysystem.utils.entities.IEntity;
import me.jumper251.replay.replaysystem.utils.entities.INPC;
import me.jumper251.replay.replaysystem.utils.entities.PacketEntity;
import me.jumper251.replay.replaysystem.utils.entities.PacketEntityOld;
import me.jumper251.replay.replaysystem.utils.entities.PacketNPC;
import me.jumper251.replay.replaysystem.utils.entities.PacketNPCOld;
import me.jumper251.replay.utils.MaterialBridge;
import me.jumper251.replay.utils.MathUtils;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class ReplayingUtils {

	private Replayer replayer;
	
	private ActionData lastSpawnAction;
	
	private HashMap<Integer, Entity> itemEntities;
	private HashMap<Integer, Integer> hooks;

	public ReplayingUtils(Replayer replayer) {
		this.replayer = replayer;
		this.itemEntities = new HashMap<Integer, Entity>();
		this.hooks = new HashMap<Integer, Integer>();
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
				
				if (animationData.getId() == 1 && !VersionUtil.isCompatible(VersionEnum.V1_8)) {
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
				
				spawnProjectile(projectile, null, replayer.getWatchingPlayer().getWorld(), 0);
			}
			
			if (action.getPacketData() instanceof BlockChangeData) {
				BlockChangeData blockChange = (BlockChangeData) action.getPacketData();
				
				if (reversed) {
					blockChange = new BlockChangeData(blockChange.getLocation(), blockChange.getAfter(), blockChange.getBefore());
				}
				
				setBlockChange(blockChange);
			}
			
			if (action.getPacketData() instanceof BedEnterData) {
				BedEnterData bed = (BedEnterData) action.getPacketData();
				
				npc.sleep(LocationData.toLocation(bed.getLocation()));
			}
			
			if (action.getPacketData() instanceof EntityItemData) {
				EntityItemData entityData = (EntityItemData) action.getPacketData();

				if (entityData.getAction() == 0 && !reversed) {
					spawnItemStack(entityData);
				} else if (entityData.getAction() == 1){
					if (itemEntities.containsKey(entityData.getId())) {
						despawn(Arrays.asList(new Entity[] { itemEntities.get(entityData.getId()) }), null);
						
						itemEntities.remove(entityData.getId());
					}
				} else {
					if (hooks.containsKey(entityData.getId())) {
						despawn(null, new int[] { hooks.get(entityData.getId()) });

						hooks.remove(entityData.getId());
					}
				}
			}
			
			if (action.getPacketData() instanceof EntityData) {
				EntityData entityData = (EntityData) action.getPacketData();

				if (entityData.getAction() == 0) {
					if (!reversed) {
					IEntity entity = VersionUtil.isCompatible(VersionEnum.V1_8) ? new PacketEntityOld(EntityType.valueOf(entityData.getType()))  : new PacketEntity(EntityType.valueOf(entityData.getType()));
					entity.spawn(LocationData.toLocation(entityData.getLocation()), this.replayer.getWatchingPlayer());
					replayer.getEntityList().put(entityData.getId(), entity);
					} else if (replayer.getEntityList().containsKey(entityData.getId())){
						IEntity ent = replayer.getEntityList().get(entityData.getId());
						ent.remove();

					}
					
				} else if (entityData.getAction() == 1) {
					if (!reversed && replayer.getEntityList().containsKey(entityData.getId())) {
					IEntity ent = replayer.getEntityList().get(entityData.getId());
					ent.remove();
					} else {
						IEntity entity = VersionUtil.isCompatible(VersionEnum.V1_8) ? new PacketEntityOld(EntityType.valueOf(entityData.getType()))  : new PacketEntity(EntityType.valueOf(entityData.getType()));
						entity.spawn(LocationData.toLocation(entityData.getLocation()), this.replayer.getWatchingPlayer());
						replayer.getEntityList().put(entityData.getId(), entity);
					}
				}
			}
			
			if (action.getPacketData() instanceof EntityMovingData) {
				EntityMovingData entityMoving = (EntityMovingData) action.getPacketData();
				if (replayer.getEntityList().containsKey(entityMoving.getId())) {
					IEntity ent = replayer.getEntityList().get(entityMoving.getId());
					ent.teleport(new Location(ent.getOrigin().getWorld(), entityMoving.getX(), entityMoving.getY(), entityMoving.getZ()), true);
					ent.look(entityMoving.getYaw(), entityMoving.getPitch());
				}
			}
			
			if (action.getPacketData() instanceof EntityAnimationData) {
				EntityAnimationData entityAnimating = (EntityAnimationData) action.getPacketData();
				if (replayer.getEntityList().containsKey(entityAnimating.getEntId()) && !reversed) {

					IEntity ent = replayer.getEntityList().get(entityAnimating.getEntId());
					ent.animate(entityAnimating.getId());
				}
			}
			
			if (action.getPacketData() instanceof WorldChangeData) {
				WorldChangeData worldChange = (WorldChangeData) action.getPacketData();
				Location loc = LocationData.toLocation(worldChange.getLocation());
				
				npc.despawn();
				npc.setOrigin(loc);
				npc.setLocation(loc);
				
				npc.respawn(replayer.getWatchingPlayer());
				
			}
			
			if (action.getPacketData() instanceof FishingData) {
				FishingData fishing = (FishingData) action.getPacketData();
				
				spawnProjectile(null, fishing, replayer.getWatchingPlayer().getWorld(), npc.getId());
				
			}
			
			if (action.getPacketData() instanceof VelocityData) {
				VelocityData velocity = (VelocityData) action.getPacketData();
				int entID = -1;
				if (hooks.containsKey(velocity.getId())) entID = hooks.get(velocity.getId());
				if (replayer.getEntityList().containsKey(velocity.getId())) entID = replayer.getEntityList().get(velocity.getId()).getId();

				if (entID != -1) {
					WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity();
					packet.setEntityID(entID);
					packet.setVelocityX(velocity.getX());
					packet.setVelocityY(velocity.getY());
					packet.setVelocityZ(velocity.getZ());
					
					packet.sendPacket(replayer.getWatchingPlayer());
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
		
		if (ConfigManager.HIDE_PLAYERS && !action.getName().equals(this.replayer.getWatchingPlayer().getName())) {
			tabMode = 2;
		}
		
		if ((spawnData.getSignature() != null && Bukkit.getPlayer(action.getName()) == null) || (spawnData.getSignature() != null && ConfigManager.HIDE_PLAYERS && !action.getName().equals(this.replayer.getWatchingPlayer().getName()))) {
			WrappedGameProfile profile = new WrappedGameProfile(spawnData.getUuid(), action.getName());
			WrappedSignedProperty signed = new WrappedSignedProperty(spawnData.getSignature().getName(), spawnData.getSignature().getValue(), spawnData.getSignature().getSignature());
			profile.getProperties().put(spawnData.getSignature().getName(), signed);
			npc.setProfile(profile);
		}

		npc.spawn(spawn, tabMode, this.replayer.getWatchingPlayer());
		npc.look(spawnData.getLocation().getYaw(), spawnData.getLocation().getPitch());

		this.lastSpawnAction = action;
	  
	}
	
	private void spawnProjectile(ProjectileData projData, FishingData fishing, World world, int id) {
		if (projData != null && projData.getType() != EntityType.FISHING_HOOK) {
			new BukkitRunnable() {
			
				@Override
				public void run() {
					Projectile proj = (Projectile) world.spawnEntity(LocationData.toLocation(projData.getSpawn()), projData.getType());
					proj.setVelocity(LocationData.toLocation(projData.getVelocity()).toVector());
				
				}
			}.runTask(ReplaySystem.getInstance());
		} 
		
		if (fishing != null) {
			Location loc = LocationData.toLocation(fishing.getLocation());
			
			WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity();
			
			int rndID = MathUtils.randInt(2000, 30000);
			packet.setEntityID(rndID);
			packet.setObjectData(id);
			packet.setUniqueId(UUID.randomUUID());
			
			packet.setOptionalSpeedX(fishing.getX());
			packet.setOptionalSpeedY(fishing.getY());
			packet.setOptionalSpeedZ(fishing.getZ());
			
			packet.setType(90);
			packet.setX(loc.getX());
			packet.setY(loc.getY());
			packet.setZ(loc.getZ());
			packet.setPitch(loc.getPitch());
			packet.setYaw(loc.getYaw());

			hooks.put(fishing.getId(), rndID);
			packet.sendPacket(replayer.getWatchingPlayer());
		}
	}
	
	private void setBlockChange(BlockChangeData blockChange) {
		final Location loc = LocationData.toLocation(blockChange.getLocation());
		
		new BukkitRunnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (blockChange.getAfter().getId() == 0 && blockChange.getBefore().getId() != 0 && MaterialBridge.fromID(blockChange.getBefore().getId()) != Material.FIRE && blockChange.getBefore().getId() != 11 && blockChange.getBefore().getId() != 9) {
					loc.getWorld().playEffect(loc, Effect.STEP_SOUND, blockChange.getBefore().getId(), 15);
					
				}
				int id = blockChange.getAfter().getId();
				int subId = blockChange.getAfter().getSubId();
				
				if (id == 9) id = 8;
				if (id == 11) id = 10;
				
				if (ConfigManager.REAL_CHANGES) {
					if (VersionUtil.isCompatible(VersionEnum.V1_13)) {
						loc.getBlock().setType(MaterialBridge.fromID(id), true);
					} else {
						loc.getBlock().setTypeIdAndData(id, (byte) subId, true);
					}
				} else {
					if (VersionUtil.isCompatible(VersionEnum.V1_13)) {
						replayer.getWatchingPlayer().sendBlockChange(loc, MaterialBridge.fromID(id), (byte) subId);
					} else {
						replayer.getWatchingPlayer().sendBlockChange(loc, id, (byte) subId);
					}
				}
				
				
			}
		}.runTask(ReplaySystem.getInstance());
	}
	
	private void spawnItemStack(EntityItemData entityData) {
		final Location loc = LocationData.toLocation(entityData.getLocation());
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Item item = loc.getWorld().dropItemNaturally(loc, new ItemStack(MaterialBridge.fromID(entityData.getItemData().getId()), 1, (short) entityData.getItemData().getSubId()));
				item.setVelocity(LocationData.toLocation(entityData.getVelocity()).toVector());
				
				itemEntities.put(entityData.getId(), item);
				
			}
		}.runTask(ReplaySystem.getInstance());
	}
	
	public void despawn(List<Entity> entities, int[] ids) {
		
		if (entities != null && entities.size() > 0) {
			new BukkitRunnable() {
			
				@Override
				public void run() {
					for (Entity en : entities) {
						if (en != null) en.remove();
					}
				}
			}.runTask(ReplaySystem.getInstance());
		}
		
		if (ids != null && ids.length > 0) {
			WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
			packet.setEntityIds(ids);
			
			packet.sendPacket(replayer.getWatchingPlayer());
		}
	}
	
	public HashMap<Integer, Entity> getEntities() {
		return itemEntities;
	}
}
