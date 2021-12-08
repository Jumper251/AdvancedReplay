package me.jumper251.replay.replaysystem.recording;

import java.io.Serializable;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityPose;

import me.jumper251.replay.replaysystem.utils.MetadataBuilder;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class PlayerWatcher implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5198365909032922108L;
	
	
	private boolean sneaking, burning, blocking, elytra, swimming;
	
	private String name;
	
	public PlayerWatcher(String name) {
		this.sneaking = false;
		this.burning = false;
		this.blocking = false;
		this.elytra = false;
		this.swimming = false;
		this.name = name;
	}
	
	public WrappedDataWatcher getMetadata(MetadataBuilder builder) {
		
		if (isValueActive()) {
			byte sneakByte = (byte) (this.sneaking ? 0x02 : 0);
			byte burnByte = (byte) (this.burning ? 0x01 : 0);
			byte oldBlock = (byte) (this.blocking ? 0x10 : 0);
			byte elytraByte = (byte) (this.elytra ? 0x80 : 0);

			if (VersionUtil.isAbove(VersionEnum.V1_13)) {
				if (!(VersionUtil.isCompatible(VersionEnum.V1_13) && this.swimming)) {
					oldBlock = 0;
				} else {
					oldBlock = (byte) (0x10 | 0x08);
				}
			}
			
			byte value = (byte) (burnByte | sneakByte | oldBlock | elytraByte);
			
			
			builder.setValue(0, value);
			
			if (VersionUtil.isAbove(VersionEnum.V1_14)) {
				builder.setPoseField(getActivePose());
			}
			
			
			
		} else {
			builder.resetValue();
		}
		
		if (!VersionUtil.isCompatible(VersionEnum.V1_8)) {
			byte blockByte = (byte) (this.blocking ? 0x01 : 0);

			if (VersionUtil.isBetween(VersionEnum.V1_10, VersionEnum.V1_13)) {
				builder.setValue(6, blockByte);
			} else if (VersionUtil.isBetween(VersionEnum.V1_14, VersionEnum.V1_16)) {
				builder.setValue(7, blockByte);
			} else if (VersionUtil.isAbove(VersionEnum.V1_17)) {
				builder.setValue(8, blockByte);
			} else {
				builder.setValue(5, blockByte);
			}
		}

		return builder.getData();
	}
	
	private String getActivePose() {
		if (this.sneaking) {
			
			return VersionUtil.isCompatible(VersionEnum.V1_14) ? "SNEAKING" : EntityPose.CROUCHING.toString();
		} else if (this.swimming) {
			
			return EntityPose.SWIMMING.toString();
		} else {
			
			return EntityPose.STANDING.toString();
		}
		
	}
	
	private boolean isValueActive() {
		return this.sneaking || this.blocking || this.burning || this.elytra || this.swimming;
	}
	
	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}
	
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}
	
	public void setBurning(boolean burning) {
		this.burning = burning;
	}
	
	public void setElytra(boolean elytra) {
		this.elytra = elytra;
	}
	
	public void setSwimming(boolean swimming) {
		this.swimming = swimming;
	}
	
	public boolean isBurning() {
		return burning;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	public boolean isElytra() {
		return elytra;
	}
	
	public boolean isSwimming() {
		return swimming;
	}
	
	public boolean isSneaking() {
		return sneaking;
	}
	
	public String getName() {
		return name;
	}
	
}
