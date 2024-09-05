package me.jumper251.replay.replaysystem.data.types;


import me.jumper251.replay.legacy.LegacyMaterial;
import me.jumper251.replay.utils.VersionUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ItemData extends PacketData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3882181315164039909L;
	
	
	private int id, subId;
	
	private SerializableItemStack itemStack;

	public ItemData(int id, int subId) {
		this.id = id;
		this.subId = subId;
	}
	
	public ItemData(SerializableItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSubId() {
		return subId;
	}
	
	public SerializableItemStack getItemStack() {
		return itemStack;
	}

	public Material toMaterial() {
		if (itemStack != null) {
			return itemStack.getMaterial();
		}
		System.out.println("item stack is null id, subid: " + id + ", " + subId);

		return LegacyMaterial.getMaterialById(id);
	}

	public static ItemData fromMaterial(Material material) {
		if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
			return new ItemData(SerializableItemStack.fromMaterial(material));
		} else {
			return new ItemData(material.getId(), 0);
		}
	}

	public static ItemData fromBlock(Block block) {
		if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
			return new ItemData(SerializableItemStack.fromMaterial(block.getBlockData().getMaterial()));
		} else {
			return new ItemData(block.getState().getType().getId(), block.getState().getData().getData());
		}
	}

	public static ItemData air() {
		if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13)) {
			return fromMaterial(Material.AIR);
		} else {
			return new ItemData(0, 0);
		}
	}

}
