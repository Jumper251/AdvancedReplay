package me.jumper251.replay.replaysystem.utils.entities;


import java.util.Arrays;
import java.util.UUID;

import com.comphenix.packetwrapper.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.v15.WrapperPlayServerRelEntityMoveLook;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import me.jumper251.replay.utils.MathUtils;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class PacketEntity implements IEntity{

	private int id;
			
	private WrappedDataWatcher data;
		
	private Location location, origin;
	
	private SpawnPacket spawnPacket;
	
	private float yaw, pitch;
	
	private Player[] visible;
	
	private Player oldVisible;
	
	
	private EntityType type;

	
	public PacketEntity(int id, EntityType type) {
		this.id = id;
		if (VersionUtil.isAbove(VersionEnum.V1_19)) {
			this.spawnPacket = new SpawnPacket(new WrapperPlayServerSpawnEntity());
		} else {
			this.spawnPacket = new SpawnPacket(new WrapperPlayServerSpawnEntityLiving());
		}
		this.type = type;
	}
	
	public PacketEntity(EntityType type) {
		this(MathUtils.randInt(50000, 400000), type);
	}
	
	@Override
	public void spawn(Location loc, Player... players) {
		this.visible = players;
		this.oldVisible = players[0];
		this.location = loc;
		this.origin = loc;

		this.spawnPacket.setEntityID(this.id);
		this.spawnPacket.setType(this.type);
		this.spawnPacket.setUniqueId(UUID.randomUUID());
		this.spawnPacket.setX(this.location.getX());
		this.spawnPacket.setY(this.location.getY());
		this.spawnPacket.setZ(this.location.getZ());
		this.spawnPacket.setYaw(this.yaw);
		this.spawnPacket.setPitch(this.pitch);
		
		if(this.data != null) this.spawnPacket.setMetadata(this.data);

		for(Player player : Arrays.asList(players)) {
			this.spawnPacket.sendPacket(player);
		}
	}

	@Override
	public void respawn(Player... players) {
		
	}

	@Override
	public void despawn() {
		WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
		
		if (VersionUtil.isAbove(VersionEnum.V1_17)) {
			destroyPacket.getHandle().getIntLists().write(0, Arrays.asList(this.id));
		} else {
			destroyPacket.setEntityIds(new int[] { this.id });
		}	
		
		for (Player player : Arrays.asList(this.visible)) {
			if(player != null){				
				destroyPacket.sendPacket(player);
			}
		}
		
		Arrays.fill(this.visible, null);		
	}

	@Override
	public void remove() {
		WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
		
		if (VersionUtil.isAbove(VersionEnum.V1_17)) {
			destroyPacket.getHandle().getIntLists().write(0, Arrays.asList(this.id));
		} else {
			destroyPacket.setEntityIds(new int[] { this.id });
		}	
		
		if(this.oldVisible != null){				
			destroyPacket.sendPacket(this.oldVisible);
		}		
	}

	@Override
	public void teleport(Location loc, boolean onGround) {
		this.location = loc;
		
		WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();

		packet.setEntityID(this.id);
		packet.setX(loc.getX());
		packet.setY(loc.getY());
		packet.setZ(loc.getZ());
		packet.setPitch(loc.getPitch());
		packet.setYaw(loc.getYaw());
		packet.setOnGround(onGround);
		
		for(Player player : Arrays.asList(this.visible)) {
			if(player != null) {
				packet.sendPacket(player);
			}
		}		
	}

	public void move(Location loc, boolean onGround, float yaw, float pitch) {
		WrapperPlayServerRelEntityMoveLook packet = new WrapperPlayServerRelEntityMoveLook();
		WrapperPlayServerEntityHeadRotation head = new WrapperPlayServerEntityHeadRotation();

		packet.setEntityID(this.id);

		head.setEntityID(this.id);
		head.setHeadYaw(((byte)(yaw * 256 / 360)));
		
		packet.setDx((short) ((loc.getX() * 32 - this.location.getX() * 32) * 128));
		packet.setDy((short) ((loc.getY() * 32 - this.location.getY() * 32) * 128));
		packet.setDz((short) ((loc.getZ() * 32 - this.location.getZ() * 32) * 128));
		packet.setPitch(pitch);
		packet.setYaw(yaw);

		this.location = loc;

		for(Player player : Arrays.asList(this.visible)) {
			if(player != null) {
				packet.sendPacket(player);
				head.sendPacket(player);
			}
		}
	}
	
	
	@Override
	public void look(float yaw, float pitch) {
		  WrapperPlayServerEntityLook lookPacket = new WrapperPlayServerEntityLook();
		  WrapperPlayServerEntityHeadRotation head = new WrapperPlayServerEntityHeadRotation();

		  head.setEntityID(this.id);
		  head.setHeadYaw(((byte)(yaw * 256 / 360)));
		  lookPacket.setEntityID(this.id);
		  lookPacket.setOnGround(true);
		  lookPacket.setPitch(pitch);
		  lookPacket.setYaw(yaw);
		  
		  for(Player player : Arrays.asList(this.visible)) {
			  if(player != null) {
				  lookPacket.sendPacket(player);
				  head.sendPacket(player);
			  }
		  }		
	}

	@Override
	public void updateMetadata() {
		
	}

	@Override
	public void animate(int id) {
		WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus();
		
		packet.setEntityID(this.id);
		packet.setEntityStatus((byte)id);
		
		for (Player player : Arrays.asList(this.visible)) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}		
	}

	@Override
	public int getId() {
		return this.id;
	}


	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setData(WrappedDataWatcher data) {
		this.data = data;
	}

	@Override
	public WrappedDataWatcher getData() {
		return this.data;
	}

	@Override
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	@Override
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Location getOrigin() {
		return this.origin;
	}

	@Override
	public Player[] getVisible() {
		return this.visible;
	}

}
