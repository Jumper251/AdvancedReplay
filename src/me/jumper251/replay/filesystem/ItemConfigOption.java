package me.jumper251.replay.filesystem;

import org.bukkit.Material;


public class ItemConfigOption {
	
	private Material material;
	
	private String name;
	
	private int slot;
	
	private String owner;
	
	private int data;
	
	public ItemConfigOption(Material material, String name, int slot) {
		this.material = material;
		this.name = name;
		this.slot = slot;
	}
	
	public ItemConfigOption(Material material, String name, int slot, String owner, int data) {
		this(material, name, slot);
		this.data = data;
		this.owner = owner;
	}
	
	
	public Material getMaterial() {
		return material;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setData(int data) {
		this.data = data;
	}
	
	public int getData() {
		return data;
	}

	
}
