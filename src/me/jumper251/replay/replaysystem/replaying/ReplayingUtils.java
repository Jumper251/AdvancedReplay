package me.jumper251.replay.replaysystem.replaying;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.ActionType;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.types.AnimationData;
import me.jumper251.replay.replaysystem.data.types.BlockChangeData;
import me.jumper251.replay.replaysystem.data.types.EntityActionData;
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
import me.jumper251.replay.utils.MathUtils;

public class ReplayingUtils {

	private Replayer replayer;
	
	public ReplayingUtils(Replayer replayer) {
		this.replayer = replayer;
	}
	
	public void handleAction(ActionData action, ReplayData data) {
		
		if (action.getType() == ActionType.SPAWN) {
			spawnNPC(action);
		}	
	
		if (action.getType() == ActionType.PACKET && this.replayer.getNPCList().containsKey(action.getName())) {
			INPC npc = this.replayer.getNPCList().get(action.getName());

			
			if (action.getPacketData() instanceof MovingData) {
				MovingData movingData = (MovingData) action.getPacketData();
				npc.teleport(new Location(replayer.getWatchingPlayer().getWorld(), movingData.getX(), movingData.getY(), movingData.getZ()), true);
				npc.look(movingData.getYaw(), movingData.getPitch());
			
			}
			
			if (action.getPacketData() instanceof EntityActionData) {
				EntityActionData eaData = (EntityActionData) action.getPacketData();
				if (eaData.getAction() == PlayerAction.START_SNEAKING) {
					data.getWatcher(action.getName()).setSneaking(true);
					npc.setData(data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData())));
				} else if (eaData.getAction() == PlayerAction.STOP_SNEAKING) {
					data.getWatcher(action.getName()).setSneaking(false);
					npc.setData(data.getWatcher(action.getName()).getMetadata(new MetadataBuilder(npc.getData())));
				}
				npc.updateMetadata();
				
				
			}
			
			if (action.getPacketData() instanceof AnimationData) {
				AnimationData animationData = (AnimationData) action.getPacketData();
				npc.animate(animationData.getId());
			}
			
			if (action.getPacketData() instanceof InvData) {
				InvData invData = (InvData) action.getPacketData();
				for (WrapperPlayServerEntityEquipment packet : NPCManager.updateEquipment(npc.getId(), invData)) {
					packet.sendPacket(replayer.getWatchingPlayer());
				}
			}
			
			if (action.getPacketData() instanceof MetadataUpdate) {
				MetadataUpdate update = (MetadataUpdate) action.getPacketData();
				
				data.getWatcher(action.getName()).setBurning(update.isBurning());
				data.getWatcher(action.getName()).setBlocking(update.isBlocking());
		
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

				setBlockChange(blockChange, npc.getId());
			}
			

		}
		
		if (action.getType() == ActionType.DEPSAWN  && replayer.getNPCList().containsKey(action.getName())) {
			INPC npc = this.replayer.getNPCList().get(action.getName());
			npc.remove();
			replayer.getNPCList().remove(action.getName());

		}
	}
	
	
	private void spawnNPC(ActionData action) {
		SpawnData spawnData = (SpawnData)action.getPacketData();
		INPC npc = new PacketNPC(MathUtils.randInt(10000, 20000), spawnData.getUuid(), action.getName());
		this.replayer.getNPCList().put(action.getName(), npc);
		this.replayer.getReplay().getData().getWatchers().put(action.getName(), new PlayerWatcher(action.getName()));
		
		int tabMode = Bukkit.getPlayer(action.getName()) != null ? 0 : 2;
		Location spawn = LocationData.toLocation(spawnData.getLocation());
		npc.setData(new MetadataBuilder(this.replayer.getWatchingPlayer()).setArrows(0).resetValue().getData());

		if (spawnData.getSignature() != null && Bukkit.getPlayer(action.getName()) == null) {
			WrappedGameProfile profile = new WrappedGameProfile(spawnData.getUuid(), action.getName());
			WrappedSignedProperty signed = new WrappedSignedProperty(spawnData.getSignature().getName(), spawnData.getSignature().getValue(), spawnData.getSignature().getSignature());
			profile.getProperties().put(spawnData.getSignature().getName(), signed);
			npc.setProfile(profile);
		}
		
		npc.spawn(spawn, tabMode, this.replayer.getWatchingPlayer());
		npc.look(spawnData.getLocation().getYaw(), spawnData.getLocation().getPitch());
	  
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
				
				loc.getBlock().setTypeIdAndData(id, (byte) subId, true);
				
				
			}
		}.runTask(ReplaySystem.getInstance());
	}
}
