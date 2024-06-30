package me.jumper251.replay.replaysystem.data.types;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerExplosion;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import me.jumper251.replay.utils.VersionUtil;
import org.bukkit.Sound;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class ExplosionData extends PacketData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private LocationData location;
	private float strength;
	private List<BlockPositionData> affectedBlocks;
	private float playerMotionX;
	private float playerMotionY;
	private float playerMotionZ;
	private String blockInteraction;
	private EnumWrappers.Particle smallExplosionParticleId;
	private EnumWrappers.Particle largeExplosionParticleId;
	private String smallExplosionParticleData;
	private String largeExplosionParticleData;
	private Sound explosionSound;
	
	public ExplosionData(LocationData location, float strength, List<BlockPositionData> affectedBlocks,
						 float playerMotionX, float playerMotionY, float playerMotionZ, String blockInteraction,
						 EnumWrappers.Particle smallExplosionParticleId,
						 EnumWrappers.Particle largeExplosionParticleId,
						 String smallExplosionParticleData,
						 String largeExplosionParticleData,
						 Sound explosionSound) {
		this.location = location;
		this.strength = strength;
		this.affectedBlocks = affectedBlocks;
		this.playerMotionX = playerMotionX;
		this.playerMotionY = playerMotionY;
		this.playerMotionZ = playerMotionZ;
		this.blockInteraction = blockInteraction;
		this.smallExplosionParticleId = smallExplosionParticleId;
		this.largeExplosionParticleId = largeExplosionParticleId;
		this.smallExplosionParticleData = smallExplosionParticleData;
		this.largeExplosionParticleData = largeExplosionParticleData;
		this.explosionSound = explosionSound;
	}
	
	public LocationData getLocation() {
		return location;
	}
	
	public float getStrength() {
		return strength;
	}
	
	public List<BlockPositionData> getAffectedBlocks() {
		return affectedBlocks;
	}
	
	public float getPlayerMotionX() {
		return playerMotionX;
	}
	
	public float getPlayerMotionY() {
		return playerMotionY;
	}
	
	public float getPlayerMotionZ() {
		return playerMotionZ;
	}
	
	public String getBlockInteraction() {
		return blockInteraction;
	}
	
	public EnumWrappers.Particle getSmallExplosionParticleId() {
		return smallExplosionParticleId;
	}
	
	public EnumWrappers.Particle getLargeExplosionParticleId() {
		return largeExplosionParticleId;
	}
	
	public String getSmallExplosionParticleData() {
		return smallExplosionParticleData;
	}
	
	public String getLargeExplosionParticleData() {
		return largeExplosionParticleData;
	}
	
	public Sound getExplosionSound() {
		return explosionSound;
	}
	
	public void setBlockInteraction(String blockInteraction) {
		this.blockInteraction = blockInteraction;
	}
	
	public void setLocation(LocationData location) {
		this.location = location;
	}
	
	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	public void setAffectedBlocks(List<BlockPositionData> affectedBlocks) {
		this.affectedBlocks = affectedBlocks;
	}
	
	public void setPlayerMotionX(float playerMotionX) {
		this.playerMotionX = playerMotionX;
	}
	
	public void setPlayerMotionY(float playerMotionY) {
		this.playerMotionY = playerMotionY;
	}
	
	public void setPlayerMotionZ(float playerMotionZ) {
		this.playerMotionZ = playerMotionZ;
	}
	
	public void setSmallExplosionParticleId(EnumWrappers.Particle smallExplosionParticleId) {
		this.smallExplosionParticleId = smallExplosionParticleId;
	}
	
	public void setLargeExplosionParticleId(EnumWrappers.Particle largeExplosionParticleId) {
		this.largeExplosionParticleId = largeExplosionParticleId;
	}
	
	public void setSmallExplosionParticleData(String smallExplosionParticleData) {
		this.smallExplosionParticleData = smallExplosionParticleData;
	}
	
	public void setLargeExplosionParticleData(String largeExplosionParticleData) {
		this.largeExplosionParticleData = largeExplosionParticleData;
	}
	
	public void setExplosionSound(Sound explosionSound) {
		this.explosionSound = explosionSound;
	}
	
	public static ExplosionData fromPacket(PacketEvent event) {
		WrapperPlayServerExplosion wrapper = new WrapperPlayServerExplosion(event.getPacket());
		LocationData location = new LocationData(wrapper.getX(), wrapper.getY(), wrapper.getZ(), event.getPlayer().getWorld().getName());
		float strength = wrapper.getPower();
		List<BlockPositionData> affectedBlocks = wrapper.getToBlow().stream()
				.map(position -> new BlockPositionData(position.getX(), position.getY(), position.getZ()))
				.collect(Collectors.toList());
		float playerMotionX = wrapper.getKnockbackX();
		float playerMotionY = wrapper.getKnockbackY();
		float playerMotionZ = wrapper.getKnockbackZ();
		
		if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_20)) {
			String blockInteraction = wrapper.getBblockInteractionAsString();
			EnumWrappers.Particle smallExplosionParticleId = wrapper.getSmallExplosionParticleId();
			EnumWrappers.Particle largeExplosionParticleId = wrapper.getLargeExplosionParticleId();
			
			WrappedParticle<?> smallExplosionParticleData = wrapper.getSmallExplosionParticles();
			WrappedParticle<?> largeExplosionParticleData = wrapper.getLargeExplosionParticles();
			
			//We lose the particle data to serialize, but for explosions this is null anyway
			String smallParticle = smallExplosionParticleData.getParticle().name();
			String largeParticle = largeExplosionParticleData.getParticle().name();
			
			Sound explosionSound = wrapper.getSound();
			return new ExplosionData(location, strength, affectedBlocks, playerMotionX, playerMotionY, playerMotionZ, blockInteraction,
					smallExplosionParticleId, largeExplosionParticleId,
					smallParticle, largeParticle,
					explosionSound);
		}
		return new ExplosionData(location, strength, affectedBlocks, playerMotionX, playerMotionY, playerMotionZ, null, null, null, null, null, null);
	}
	
	public AbstractPacket toPacket() {
		WrapperPlayServerExplosion packet = new WrapperPlayServerExplosion();
		
		LocationData location = getLocation();
		packet.setX(location.getX());
		packet.setY(location.getY());
		packet.setZ(location.getZ());
		packet.setPower(getStrength());
		packet.setToBlow(getAffectedBlocks().stream()
				.map(bp -> new BlockPosition(bp.getX(), bp.getY(), bp.getZ()))
				.collect(Collectors.toList()));
		
		//Viewer should not be effected
		packet.setKnockbackX(0);
		packet.setKnockbackY(0);
		packet.setKnockbackZ(0);
		
		if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_20)) {
			packet.setSound(getExplosionSound());
			packet.setSmallExplosionParticleId(getSmallExplosionParticleId());
			packet.setLargeExplosionParticleId(getLargeExplosionParticleId());
			try {
				Class<?> particleClass = Class.forName("org.bukkit.Particle");
				Enum<?> smallParticleEnum = Enum.valueOf((Class<Enum>) particleClass, getSmallExplosionParticleData());
				Enum<?> largeParticleEnum = Enum.valueOf((Class<Enum>) particleClass, getLargeExplosionParticleData());
				
				// Use reflection to call the WrappedParticle.create method
				Method createMethod = WrappedParticle.class.getMethod("create", particleClass, Object.class);
				Object smallWrappedParticle = createMethod.invoke(null, smallParticleEnum, null);
				Object largeWrappedParticle = createMethod.invoke(null, largeParticleEnum, null);
				
				packet.setSmallExplosionParticles((WrappedParticle<?>) smallWrappedParticle);
				packet.setLargeExplosionParticles((WrappedParticle<?>) largeWrappedParticle);
			} catch (Exception e) {
				e.printStackTrace();
			}
			packet.setBlockInteractionFromString(getBlockInteraction());
		}
		
		return packet;
	}
	
	@Override
	public String toString() {
		return "ExplosionData{" +
				"location=" + location +
				", strength=" + strength +
				", affectedBlocks=" + affectedBlocks +
				", playerMotionX=" + playerMotionX +
				", playerMotionY=" + playerMotionY +
				", playerMotionZ=" + playerMotionZ +
				", blockInteraction=" + blockInteraction +
				", smallExplosionParticleData=" + smallExplosionParticleData +
				", largeExplosionParticleData=" + largeExplosionParticleData +
				", explosionSound='" + explosionSound + '\'' +
				'}';
	}
}
