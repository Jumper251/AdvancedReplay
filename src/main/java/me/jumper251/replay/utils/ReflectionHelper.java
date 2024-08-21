package me.jumper251.replay.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;


public class ReflectionHelper {
	
	private static ReflectionHelper instance;

	private Class<?> materialClass;
	
	private Class<?> blockClass;
	
	private Class<?> blockDataClass;
	
	private Class<?> playerClass;

	private Class<?> entityToggleSwimEventClass;

	
	private Method matchMaterial;

	private Method materialName;
	
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

			if (VersionUtil.isAbove(VersionEnum.V1_13)) {
				this.entityToggleSwimEventClass = Class.forName("org.bukkit.event.entity.EntityToggleSwimEvent");
			}
			
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

			if (VersionUtil.isAbove(VersionEnum.V1_21)) {
				this.materialName = this.materialClass.getMethod("name");
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

	public Object getMaterialName(Object material) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.materialName.invoke(material);
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Event> getSwimEvent() {
		return (Class<? extends Event>) this.entityToggleSwimEventClass;
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
	
	@SuppressWarnings("unchecked")
	public <T extends Event> void registerEvent(Class<T> event, Listener listener, Consumer<T> handler) {
		Bukkit.getPluginManager().registerEvent(event, listener, EventPriority.MONITOR, (l, e) -> {
			if (e.getClass().equals(event)) {
				handler.accept((T) e);
			}
		}, ReplaySystem.getInstance(), true);
		
	}
	
	
	public static ReflectionHelper getInstance() {
		if (instance == null) instance = new ReflectionHelper();
		
		return instance;
	}
	
	
}
