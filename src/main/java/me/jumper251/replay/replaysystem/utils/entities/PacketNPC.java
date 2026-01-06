package me.jumper251.replay.replaysystem.utils.entities;

import java.util.*;


import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam.Mode;
import com.comphenix.packetwrapper.v15.WrapperPlayServerRelEntityMoveLook;

import me.jumper251.replay.replaysystem.utils.NPCManager;
import me.jumper251.replay.utils.MathUtils;
import me.jumper251.replay.utils.StringUtils;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.util.Vector;


public class PacketNPC implements INPC{

	private int id;

	private UUID uuid;
	
	private String name;
	
	private int tabMode;
	
	private WrappedDataWatcher data;
	
	private WrappedGameProfile profile;
	
	private Location location, origin;
	
	private NPCSpawnPacket spawnPacket;
	
	private float yaw, pitch;
	
	private Player[] visible;
	
	private Player oldVisible;
	
	private List<WrapperPlayServerEntityEquipment> lastEquipment;
	
	public PacketNPC(int id, UUID uuid, String name) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.tabMode = 1;
		this.lastEquipment = new ArrayList<>();
		this.visible = new Player[]{};

        if (VersionUtil.isAbove(VersionEnum.V1_20)) {
			this.spawnPacket = new NPCSpawnPacket(new WrapperPlayServerSpawnEntity());
		} else {
			this.spawnPacket = new NPCSpawnPacket(new WrapperPlayServerNamedEntitySpawn());
		}
	}
	
	public PacketNPC() {
		this(MathUtils.randInt(50000, 400000), UUID.randomUUID(), StringUtils.getRandomString(6));
	}
	
	public void spawn(Location loc, int tabMode, Player... players) {
		this.tabMode = tabMode;
		this.visible = players;
		this.oldVisible = players[0];
		this.location = loc;
		this.origin = loc;
		NPCManager.names.add(this.name);

		this.spawnPacket.setEntityID(this.id);
		this.spawnPacket.setUniqueId(uuid);
		this.spawnPacket.setPosition(loc.toVector());
		this.spawnPacket.setYaw(this.yaw);
		this.spawnPacket.setPitch(this.pitch);
	
		if(this.data != null) this.spawnPacket.setMetadata(this.data);
		
		for(Player player : players) {
			if(tabMode != 0) {
				getInfoAddPacket().sendPacket(player);
			}
			this.spawnPacket.sendPacket(player);

			if(tabMode == 1) {
				getInfoRemovePacket().sendPacket(player);
			}
		}
		
	}
	
	public void respawn(Player... players) {
		this.visible = players;
		this.spawnPacket.setMetadata(this.data);
		this.spawnPacket.setPosition(this.location.toVector());
        this.spawnPacket.setYaw(this.yaw);
        this.spawnPacket.setPitch(this.pitch);
		
		for (Player player : this.visible) {
			this.spawnPacket.sendPacket(player);
			
			for (WrapperPlayServerEntityEquipment equipment : this.lastEquipment) {
				equipment.sendPacket(player);
			}
		}

        this.look(this.yaw, this.pitch);
        this.updateMetadata();
	}
	
	public void despawn() {
		WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
		
		if (VersionUtil.isAbove(VersionEnum.V1_17)) {
			destroyPacket.getHandle().getIntLists().write(0, Arrays.asList(this.id));
		} else {
			destroyPacket.setEntityIds(new int[] { this.id });
		}		
		
		for (Player player : this.visible) {
			if(player != null){				
				destroyPacket.sendPacket(player);
			}
		}
		
		Arrays.fill(this.visible, null);
		
	}
	
	public void remove() {
        NPCManager.names.remove(this.name);
		
		WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
		
		if (VersionUtil.isAbove(VersionEnum.V1_17)) {
			destroyPacket.getHandle().getIntLists().write(0, Arrays.asList(this.id));
		} else {
			destroyPacket.setEntityIds(new int[] { this.id });
		}	
		
		if(this.oldVisible != null){				
			if(this.tabMode == 2){
				if (VersionUtil.isAbove(VersionEnum.V1_19)) {
					ProtocolLibrary.getProtocolManager().sendServerPacket(this.oldVisible, getPlayerInfoRemovePacket());
				} else {
					getInfoRemovePacket().sendPacket(this.oldVisible);
				}
			}
				
			destroyPacket.sendPacket(this.oldVisible);

		}
		
	}
	
	public void teleport(Location loc, boolean onGround) {
		this.location = loc;
		
		WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();

		packet.setEntityID(this.id);
		if (VersionUtil.isAbove(VersionEnum.V1_21)) {
			InternalStructure internalStructure = packet.getHandle().getStructures().getValues().get(0);
			internalStructure.getVectors().write(0, new Vector(loc.getX(), loc.getY(), loc.getZ()));
			internalStructure.getFloat().write(0, loc.getYaw());
			internalStructure.getFloat().write(1, loc.getPitch());
		} else {
			packet.setX(loc.getX());
			packet.setY(loc.getY());
			packet.setZ(loc.getZ());

			packet.setPitch(loc.getPitch());
			packet.setYaw(loc.getYaw());
		}
		packet.setOnGround(onGround);
		
		for(Player player : this.visible) {
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
        this.yaw = yaw;
        this.pitch = pitch;

		for(Player player : this.visible) {
			if(player != null) {
				packet.sendPacket(player);
				head.sendPacket(player);
			}
		}
	}
	
	public void look(float yaw, float pitch) {
		  WrapperPlayServerEntityLook lookPacket = new WrapperPlayServerEntityLook();
		  WrapperPlayServerEntityHeadRotation head = new WrapperPlayServerEntityHeadRotation();
		  
		  head.setEntityID(this.id);
		  head.setHeadYaw(((byte)(yaw * 256 / 360)));
		  lookPacket.setEntityID(this.id);
		  lookPacket.setOnGround(true);
		  lookPacket.setPitch(pitch);
		  lookPacket.setYaw(yaw);
		  
		  for(Player player : this.visible) {
			  if(player != null) {
				  lookPacket.sendPacket(player);
				  head.sendPacket(player);
			  }
		  }
	}
	
	public void updateMetadata() {
		WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
		
		packet.setEntityID(this.id);
		if (VersionUtil.isAbove(VersionEnum.V1_19)) {
			// https://www.spigotmc.org/threads/unable-to-modify-entity-metadata-packet-using-protocollib-1-19-3.582442/
			List<WrappedDataValue> wrappedDataValueList = Lists.newArrayList();
			this.data.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
				WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
				wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
			});
			packet.getHandle().getDataValueCollectionModifier().write(0, wrappedDataValueList);
		} else {
			packet.setMetadata(this.data.getWatchableObjects());
		}
		
		for (Player player : this.visible) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	public void animate(int id) {
		if (id == 1 && VersionUtil.isAbove(VersionEnum.V1_20)) {
			hurt();
			return;
		}

		WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation();
		
		packet.setEntityID(this.id);
		packet.setAnimation(id);
		
		for (Player player : this.visible) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}
	}

	private void hurt() {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.HURT_ANIMATION);
		packet.getIntegers().write(0, this.id);

		for (Player player : this.visible) {
			if (player != null) {
				ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
			}
		}
	}
	
	public void sleep(Location loc) {
		WrapperPlayServerBed packet = new WrapperPlayServerBed();
		
		packet.setEntityID(this.id);
		packet.setLocation(new BlockPosition(loc.toVector()));
		
		for (Player player : this.visible) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	
	public void addToTeam(String team) {
		WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
		
		packet.setName(team);
		packet.setMode(Mode.PLAYERS_ADDED);
		packet.setPlayers(Arrays.asList(this.name));
		
		for(Player player : this.visible) {
			if(player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	public WrapperPlayServerPlayerInfo getInfoAddPacket() {
		WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo();
		infoPacket.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
		
		WrappedGameProfile profile = this.profile != null ? this.profile : new WrappedGameProfile(this.uuid, this.name);
		PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
		List<PlayerInfoData> dataList = new ArrayList<>();
		dataList.add(data);
		
		infoPacket.setData(dataList);	
		return infoPacket;
	}

	public WrapperPlayServerPlayerInfo getInfoRemovePacket() {
		WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo();
		infoPacket.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);

		WrappedGameProfile profile = this.profile != null ? this.profile : new WrappedGameProfile(this.uuid, this.name);
		PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
		List<PlayerInfoData> dataList = new ArrayList<>();
		dataList.add(data);

		infoPacket.setData(dataList);
		return infoPacket;
	}

	private PacketContainer getPlayerInfoRemovePacket() {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
		packet.getModifier().writeDefaults();
		packet.getModifier().withType(List.class).write(0, Collections.singletonList(uuid));

		return packet;
	}
	
	public int getId() {
		return id;
	}
	
	public WrapperPlayServerNamedEntitySpawn getSpawnPacket() {
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setData(WrappedDataWatcher data) {
		this.data = data;
	}
	
	public WrappedDataWatcher getData() {
		return data;
	}
	
	public void setProfile(WrappedGameProfile profile) {
		this.profile = profile;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setOrigin(Location origin) {
		this.origin = origin;
	}
	

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getOrigin() {
		return origin;
	}
	
	public Player[] getVisible() {
		return this.visible;
	}
	
	public void setLastEquipment(List<WrapperPlayServerEntityEquipment> list) {
		this.lastEquipment = list;
		
	}
}
