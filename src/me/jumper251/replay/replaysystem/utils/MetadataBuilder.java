package me.jumper251.replay.replaysystem.utils;


import java.lang.reflect.Method;

import org.bukkit.entity.Entity;

import com.comphenix.protocol.wrappers.EnumWrappers.EntityPose;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;



public class MetadataBuilder {

	private WrappedDataWatcher data;

	public MetadataBuilder(Entity en) {
		this.data = WrappedDataWatcher.getEntityWatcher(en).deepClone();
	}
	
	public MetadataBuilder(WrappedDataWatcher data) {
		this.data = data;

	}
	
	public MetadataBuilder setValue(int index, Object value) {
		this.data.setObject(index, value);
		
		return this;
	}
	
	public MetadataBuilder setInvisible() {
		return setValue(0, (byte) 0x20);
	}
	
	public MetadataBuilder setCrouched() {
		return setValue(0, (byte) 0x02);
	}
	
	public MetadataBuilder resetValue() {
		if (VersionUtil.isAbove(VersionEnum.V1_14)) {
			return setValue(0, (byte) 0).setPoseField("STANDING");
		} else {
			return setValue(0, (byte) 0);
		}
	}
	
	public MetadataBuilder setArrows(int amount) {
		
		if (VersionUtil.isBetween(VersionEnum.V1_10, VersionEnum.V1_13)) {
			return setValue(10, amount);
		} else if (VersionUtil.isBetween(VersionEnum.V1_14, VersionEnum.V1_16)) {
			return setValue(11, amount);
		} else if (VersionUtil.isAbove(VersionEnum.V1_17)) {
			return setValue(12, amount);
		} else {
			return setValue(9, amount);
		}
		
	}
	
	public MetadataBuilder setGlowing() {
		return setValue(0, (byte) 0x20);
	}
	
	public MetadataBuilder setSilent() {
		return setValue(4, true);
	}
	
	public MetadataBuilder setNoGravity() {
		return setValue(5, true);
	}
	
	public MetadataBuilder setHealth(float amount) {
		return setValue(7, amount); //Might be Version problems with field numbers http://wiki.vg/Entity_metadata
	}
	
	public MetadataBuilder setAir(int amount) {
		return setValue(1, amount);
	}
	
	public WrappedDataWatcher getData() {
		return this.data;
	}
	
	public MetadataBuilder setPoseField(String type) {
		if (VersionUtil.isAbove(VersionEnum.V1_17)) {
			return setValue(6, EntityPose.valueOf(type).toNms());
		}
		
		Object enumField = null;
		
		try {
			Class<?> entityPose = Class.forName("net.minecraft.server." + VersionUtil.VERSION + ".EntityPose");
			
			Method valueOf = entityPose.getMethod("valueOf", String.class);
			enumField = valueOf.invoke(null, type);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return setValue(6, enumField);
	}
}
