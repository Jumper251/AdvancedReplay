package me.jumper251.replay.replaysystem.utils.entities;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;

import me.jumper251.replay.replaysystem.data.types.FishingData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class FishingUtils {

	public static WrapperPlayServerSpawnEntity createHookPacket(FishingData fishing, int throwerID, int entID) {
		Location loc = LocationData.toLocation(fishing.getLocation());
		
		WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity();
		
		packet.setEntityID(entID);
		if (VersionUtil.isBelow(VersionEnum.V1_13)) {
			packet.setObjectData(throwerID);
			packet.setType(90);
		}
		packet.setUniqueId(UUID.randomUUID());
		
		packet.setOptionalSpeedX(fishing.getX());
		packet.setOptionalSpeedY(fishing.getY());
		packet.setOptionalSpeedZ(fishing.getZ());
		
		if (VersionUtil.isAbove(VersionEnum.V1_14)) {
			packet.setObjectData(throwerID); // Object data index changed
			packet.getHandle().getEntityTypeModifier().write(0, EntityType.FISHING_HOOK);
		}

		packet.setX(loc.getX());
		packet.setY(loc.getY());
		packet.setZ(loc.getZ());
		
		return packet;
	}
	
	public static com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity createHookPacketOld(FishingData fishing, int throwerID, int entID) {
		Location loc = LocationData.toLocation(fishing.getLocation());
		
		com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity packet = new com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity();
		
		packet.setEntityID(entID);
		packet.setObjectData(throwerID);
		packet.setType(90);
		
		packet.setOptionalSpeedX(fishing.getX());
		packet.setOptionalSpeedY(fishing.getY());
		packet.setOptionalSpeedZ(fishing.getZ());
		
		
		packet.setX(loc.getX());
		packet.setY(loc.getY());
		packet.setZ(loc.getZ());
		packet.setPitch(loc.getPitch());
		packet.setYaw(loc.getYaw());
		
		return packet;
	}
	
	
}
