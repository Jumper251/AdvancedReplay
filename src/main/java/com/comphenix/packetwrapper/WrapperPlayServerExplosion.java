package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import me.jumper251.replay.utils.VersionUtil;
import org.bukkit.Sound;

import java.util.List;

/**
 * Wrapper class for the Play.Server.EXPLOSION packet.
 * <p>
 * This wrapper was tested from 1.8 to 1.20.
 * <p>
 * Starting with 1.20 particle and sound information is added.
 *
 * @author Mark Cockram - NubeBuster
 */
public class WrapperPlayServerExplosion extends AbstractPacket {
	
	public static final PacketType TYPE = PacketType.Play.Server.EXPLOSION;
	
	public WrapperPlayServerExplosion() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}
	
	public WrapperPlayServerExplosion(PacketContainer packet) {
		super(packet, TYPE);
	}
	
	/**
	 * Retrieve the X position of the explosion.
	 *
	 * @return The current X position.
	 */
	public double getX() {
		return handle.getDoubles().read(0);
	}
	
	/**
	 * Set the X position of the explosion.
	 *
	 * @param value - new X position.
	 */
	public void setX(double value) {
		handle.getDoubles().write(0, value);
	}
	
	/**
	 * Retrieve the Y position of the explosion.
	 *
	 * @return The current Y position.
	 */
	public double getY() {
		return handle.getDoubles().read(1);
	}
	
	/**
	 * Set the Y position of the explosion.
	 *
	 * @param value - new Y position.
	 */
	public void setY(double value) {
		handle.getDoubles().write(1, value);
	}
	
	/**
	 * Retrieve the Z position of the explosion.
	 *
	 * @return The current Z position.
	 */
	public double getZ() {
		return handle.getDoubles().read(2);
	}
	
	/**
	 * Set the Z position of the explosion.
	 *
	 * @param value - new Z position.
	 */
	public void setZ(double value) {
		handle.getDoubles().write(2, value);
	}
	
	/**
	 * Retrieve the strength of the explosion.
	 *
	 * @return The current explosion strength.
	 */
	public float getPower() {
		return handle.getFloat().read(0);
	}
	
	/**
	 * Set the strength of the explosion.
	 *
	 * @param value - new explosion strength.
	 */
	public void setPower(float value) {
		handle.getFloat().write(0, value);
	}
	
	/**
	 * Retrieve the records of affected blocks.
	 *
	 * @return The list of affected blocks.
	 */
	public List<BlockPosition> getToBlow() {
		return handle.getBlockPositionCollectionModifier().read(0);
	}
	
	/**
	 * Set the records of affected blocks.
	 *
	 * @param value - new list of affected blocks.
	 */
	public void setToBlow(List<BlockPosition> value) {
		handle.getBlockPositionCollectionModifier().write(0, value);
	}
	
	/**
	 * Retrieve the X motion of the player being pushed by the explosion.
	 *
	 * @return The current X motion.
	 */
	public float getKnockbackX() {
		return handle.getFloat().read(1);
	}
	
	/**
	 * Set the X motion of the player being pushed by the explosion.
	 *
	 * @param value - new X motion.
	 */
	public void setKnockbackX(float value) {
		handle.getFloat().write(1, value);
	}
	
	/**
	 * Retrieve the Y motion of the player being pushed by the explosion.
	 *
	 * @return The current Y motion.
	 */
	public float getKnockbackY() {
		return handle.getFloat().read(2);
	}
	
	/**
	 * Set the Y motion of the player being pushed by the explosion.
	 *
	 * @param value - new Y motion.
	 */
	public void setKnockbackY(float value) {
		handle.getFloat().write(2, value);
	}
	
	/**
	 * Retrieve the Z motion of the player being pushed by the explosion.
	 *
	 * @return The current Z motion.
	 */
	public float getKnockbackZ() {
		return handle.getFloat().read(3);
	}
	
	/**
	 * Set the Z motion of the player being pushed by the explosion.
	 *
	 * @param value - new Z motion.
	 */
	public void setKnockbackZ(float value) {
		handle.getFloat().write(3, value);
	}
	
	/**
	 * Retrieve the small explosion particle ID.
	 *
	 * @return The current small explosion particle ID.
	 */
	public EnumWrappers.Particle getSmallExplosionParticleId() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		return handle.getParticles().read(8);
	}
	
	/**
	 * Set the small explosion particle ID.
	 *
	 * @param value - new small explosion particle ID.
	 */
	public void setSmallExplosionParticleId(EnumWrappers.Particle value) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		handle.getParticles().write(8, value);
	}
	
	/**
	 * Retrieve the small explosion particle data.
	 *
	 * @return The current small explosion particle data.
	 */
	public WrappedParticle<?> getSmallExplosionParticles() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		return handle.getNewParticles().read(0);
	}
	
	/**
	 * Set the small explosion particle data.
	 *
	 * @param value - new small explosion particle data.
	 */
	public void setSmallExplosionParticles(WrappedParticle<?> value) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		handle.getNewParticles().write(0, value);
	}
	
	/**
	 * Retrieve the large explosion particle ID.
	 *
	 * @return The current large explosion particle ID.
	 */
	public EnumWrappers.Particle getLargeExplosionParticleId() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		return handle.getParticles().read(9);
	}
	
	/**
	 * Set the large explosion particle ID.
	 *
	 * @param value - new large explosion particle ID.
	 */
	public void setLargeExplosionParticleId(EnumWrappers.Particle value) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		handle.getParticles().write(9, value);
	}
	
	/**
	 * Retrieve the large explosion particle data.
	 *
	 * @return The current large explosion particle data.
	 */
	public WrappedParticle<?> getLargeExplosionParticles() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		return handle.getNewParticles().read(1);
	}
	
	/**
	 * Set the large explosion particle data.
	 *
	 * @param value - new large explosion particle data.
	 */
	public void setLargeExplosionParticles(WrappedParticle<?> value) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		handle.getNewParticles().write(1, value);
	}
	
	/**
	 * Retrieve the sound effect of the explosion.
	 *
	 * @return The current sound effect.
	 */
	public Sound getSound() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		return handle.getSoundEffects().read(0);
	}
	
	/**
	 * Set the sound effect of the explosion.
	 *
	 * @param value - new sound effect.
	 */
	public void setSound(Sound value) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		handle.getSoundEffects().write(0, value);
	}
	
	/**
	 * Set the block interaction type from a string value.
	 *
	 * @param enumValue - string representation of the enum value.
	 */
	public void setBlockInteractionFromString(String enumValue) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		try {
			Class<?> enumClass = Class.forName("net.minecraft.world.level.Explosion$Effect");
			Enum<?> newEnumValue = Enum.valueOf((Class<Enum>) enumClass, enumValue);
			handle.getEnumModifier((Class<Enum>) enumClass, 10).write(0, newEnumValue);
		} catch (ClassNotFoundException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Serialize the block interaction type to a string.
	 *
	 * @return The string representation of the block interaction type.
	 */
	public String getBblockInteractionAsString() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		Enum<?> blockInteraction = getBlockInteraction();
		return blockInteraction != null ? blockInteraction.name() : "UNKNOWN";
	}
	
	/**
	 * Set the block interaction type.
	 *
	 * @param <T>   - enum type.
	 * @param value - new block interaction type.
	 */
	public <T extends Enum<T>> void setBlockInteraction(T value) {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return;
		handle.getEnumModifier(value.getDeclaringClass(), 10).write(0, value);
	}
	
	/**
	 * Retrieve the block interaction type.
	 *
	 * @param <T> - enum type.
	 * @return The current block interaction type.
	 */
	public <T extends Enum<T>> T getBlockInteraction() {
		if (VersionUtil.isBelow(VersionUtil.VersionEnum.V1_19)) return null;
		try {
			Class<T> enumClass = (Class<T>) Class.forName("net.minecraft.world.level.Explosion$Effect");
			return handle.getEnumModifier(enumClass, 10).read(0);
		} catch (ClassNotFoundException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
