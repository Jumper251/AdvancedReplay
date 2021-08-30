package me.jumper251.replay.utils;




import org.bukkit.Material;
import org.bukkit.block.Block;

import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public enum MaterialBridge {
	
	WATCH("CLOCK"),
	WOOD_DOOR("WOODEN_DOOR");

	private String materialName;
	
	private MaterialBridge(String materialName) {
		this.materialName = materialName;
	}
	
	public Material toMaterial() {
		return Material.valueOf(this.toString());
		
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	@SuppressWarnings("deprecation")
	public static Material fromID(int id) {
		if (VersionUtil.isCompatible(VersionEnum.V1_13) || VersionUtil.isCompatible(VersionEnum.V1_14) || VersionUtil.isCompatible(VersionEnum.V1_15) || VersionUtil.isCompatible(VersionEnum.V1_16) || VersionUtil.isCompatible(VersionEnum.V1_17)) {
			for (Material mat : Material.values()) {
				if (mat.getId() == id) return mat;
			}
			
			return null;
			
		} else {
			return Material.getMaterial(id);
		}
	}
	
	public static Material getWithoutLegacy(String material) {
		try {
			Object enumField = ReflectionHelper.getInstance().matchMaterial(material, false);

			return (Material) enumField;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Material getBlockDataMaterial(Block block) {
		try {
			Object blockData = ReflectionHelper.getInstance().getBlockData(block);
			Object materialField = ReflectionHelper.getInstance().getBlockDataMaterial(blockData);
			
			return (Material) materialField;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
