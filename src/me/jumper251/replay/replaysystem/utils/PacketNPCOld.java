package me.jumper251.replay.replaysystem.utils;

import java.util.ArrayList;







import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerBed;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityHeadRotation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityLook;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.packetwrapper.old.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam.Mode;
import com.comphenix.packetwrapper.old.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import me.jumper251.replay.utils.MathUtils;
import me.jumper251.replay.utils.StringUtils;



public class PacketNPCOld implements INPC{

	private int id;

	private UUID uuid;
	
	private String name;
	
	private int tabMode;
	
	private WrappedDataWatcher data;
	
	private WrappedGameProfile profile;
	
	private WrapperPlayServerNamedEntitySpawn spawnPacket;
	
	private Location location, origin;
	
	private float yaw, pitch;
	
	private Player[] visible;
	
	private Player oldVisible;
	
	private List<WrapperPlayServerEntityEquipment> lastEquipment;
	
	public PacketNPCOld(int id, UUID uuid, String name) {
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.tabMode = 1;
		this.lastEquipment = new ArrayList<WrapperPlayServerEntityEquipment>();
		this.spawnPacket = new WrapperPlayServerNamedEntitySpawn();
	}
	
	public PacketNPCOld() {
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
		this.spawnPacket.setPlayerUUID(uuid);
		this.spawnPacket.setPosition(loc.toVector());
		this.spawnPacket.setYaw(this.yaw);
		this.spawnPacket.setPitch(this.pitch);
		

		if(this.data != null) this.spawnPacket.setMetadata(this.data);
	

		for(Player player : Arrays.asList(players)) {
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
		
		for (Player player : Arrays.asList(this.visible)) {
			this.spawnPacket.sendPacket(player);
		}
	}
	
	public void despawn() {
		WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
		
		destroyPacket.setEntityIds(new int[] { this.id });
		
		for (Player player : Arrays.asList(this.visible)) {
			destroyPacket.sendPacket(player);
		}
		
		Arrays.fill(this.visible, null);
	}
	
	public void remove() {
		if(NPCManager.names.contains(this.name)) {
			NPCManager.names.remove(this.name);
		}
		
		WrapperPlayServerEntityDestroy destroyPacket = new WrapperPlayServerEntityDestroy();
		
		destroyPacket.setEntityIds(new int[] { this.id });
		
		for(Player player : Arrays.asList(this.visible)) {
			if(player != null){				
				if(this.tabMode == 2){
					getInfoRemovePacket().sendPacket(player);
				}
				
				destroyPacket.sendPacket(player);

			}
		}
	}
	
	public void teleport(Location loc, boolean onGround) {
		this.location = loc;

		WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();

		packet.setEntityID(this.id);
		packet.setX(loc.getX());
		packet.setY(loc.getY());
		packet.setZ(loc.getZ());
		packet.setPitch(loc.getPitch());
		packet.setYaw(loc.getYaw());
	
		
		for(Player player : Arrays.asList(this.visible)) {
			if(player != null) {
				packet.sendPacket(player);
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
		  
		  for(Player player : Arrays.asList(this.visible)) {
			  if(player != null) {
				  lookPacket.sendPacket(player);
				  head.sendPacket(player);
			  }
		  }
		  
	}
	

	public void updateMetadata() {
		WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
		
		packet.setEntityID(this.id);
		packet.setMetadata(this.data.getWatchableObjects());
		
		for (Player player : Arrays.asList(this.visible)) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	public void animate(int id) {
		WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation();
		
		packet.setEntityID(this.id);
		packet.setAnimation(id);
		
		for (Player player : Arrays.asList(this.visible)) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	public void sleep(Location loc) {
		WrapperPlayServerBed packet = new WrapperPlayServerBed();
		
		packet.setEntityID(this.id);
		packet.setLocation(new BlockPosition(loc.toVector()));
		
		for (Player player : Arrays.asList(this.visible)) {
			if (player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	public void addToTeam(String team) {
		WrapperPlayServerScoreboardTeam packet = new WrapperPlayServerScoreboardTeam();
		
		packet.setName(team);
		packet.setMode(Mode.PLAYERS_ADDED);
		packet.setPlayers(Arrays.asList(new String[] { this.name }));
		
		for(Player player : Arrays.asList(this.visible)) {
			if(player != null) {
				packet.sendPacket(player);
			}
		}
	}
	
	private WrapperPlayServerPlayerInfo getInfoAddPacket() {
		WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo();
		infoPacket.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
		
		WrappedGameProfile profile = this.profile != null ? this.profile : new WrappedGameProfile(this.uuid, this.name);
		PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
		List<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
		dataList.add(data);
		
		infoPacket.setData(dataList);	
		return infoPacket;
	}

	private WrapperPlayServerPlayerInfo getInfoRemovePacket() {
		WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo();
		infoPacket.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);

		WrappedGameProfile profile = this.profile != null ? this.profile : new WrappedGameProfile(this.uuid, this.name);
		PlayerInfoData data = new PlayerInfoData(profile, 1, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(this.name));
		List<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
		dataList.add(data);

		infoPacket.setData(dataList);
		return infoPacket;
	}
	
	public int getId() {
		return id;
	}
	
	public WrapperPlayServerNamedEntitySpawn getSpawnPacket() {
		return spawnPacket;
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
	
	public void setProfile(WrappedGameProfile profile) {
		this.profile = profile;
	}
	
	public WrappedDataWatcher getData() {
		return data;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	@Override
	public Location getOrigin() {
		return origin;
	}
	
	@Override
	public void setOrigin(Location origin) {
		this.origin = origin;
		
	}
	
	@Override
	public void setLocation(Location location) {
		this.location = location;
		
	}
	
	public Player[] getVisible() {
		return this.visible;
	}
	
	public void setLastEquipment(List<WrapperPlayServerEntityEquipment> list) {
		this.lastEquipment = list;
		
	}
}
