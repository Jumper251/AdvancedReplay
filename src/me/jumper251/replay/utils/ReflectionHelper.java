package me.jumper251.replay.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.jumper251.replay.utils.VersionUtil.VersionEnum;


public class ReflectionHelper {
	
	private static ReflectionHelper instance;

	private Class<?> materialClass;
	
	private Class<?> blockClass;
	
	private Class<?> blockDataClass;
	
	private Class<?> playerClass;

	
	
	private Method matchMaterial;
	
	private Method getBlockData;
	
	private Method blockDataGetMaterial;
	
	private Method sendTitle;
	
	private ReflectionHelper() {
		this.initializeClasses();
		
		this.initializeMethods();
	}
	
	private void initializeClasses() {
		try {
			this.materialClass = Class.forName("org.bukkit.Material");
			this.blockClass = Class.forName("org.bukkit.block.Block");
			this.blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
			this.playerClass = Class.forName("org.bukkit.entity.Player");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	private void initializeMethods() {
		try {
			this.matchMaterial = this.materialClass.getMethod("matchMaterial", String.class, boolean.class);
			this.getBlockData = this.blockClass.getMethod("getBlockData");
			this.blockDataGetMaterial = this.blockDataClass.getMethod("getMaterial");
			
			if (VersionUtil.isAbove(VersionEnum.V1_17)) {
				this.sendTitle = this.playerClass.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
			}
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public Object matchMaterial(String material, boolean legacy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.matchMaterial.invoke(null, material, legacy);
	}
	
	public Object getBlockData(Block block) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.getBlockData.invoke(block);
	}
	
	public Object getBlockDataMaterial(Object blockData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.blockDataGetMaterial.invoke(blockData);
	}
	
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		try {
			this.sendTitle.invoke(player, title, subtitle, fadeIn, stay, fadeOut);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ReflectionHelper getInstance() {
		if (instance == null) instance = new ReflectionHelper();
		
		return instance;
	}
	
	
}
