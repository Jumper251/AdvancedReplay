package me.jumper251.replay.replaysystem.data.types;

import java.io.Serializable;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;


public class SerializableItemStack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6849346246817837690L;
	
	
	private Map<String, Object> itemStack;
	
	private boolean hasEnchantment;
	
	private int color;
	
	private boolean hasColor;
	
	
	public SerializableItemStack() {

	}
	
	public SerializableItemStack(Map<String, Object> itemStack) {
		this.itemStack = itemStack;
	}

	
	public SerializableItemStack(Map<String, Object> itemStack, boolean hasEnchantment, int color, boolean hasColor) {
		this(itemStack);
		
		this.hasEnchantment = hasEnchantment;
		this.color = color;
		this.hasColor = hasColor;
	}

	
	public Map<String, Object> getItemStack() {
		return itemStack;
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean hasEnchantment() {
		return hasEnchantment;
	}
	
	public boolean hasColor() {
		return hasColor;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setHasColor(boolean hasColor) {
		this.hasColor = hasColor;
	}
	
	public void setHasEnchantment(boolean hasEnchantment) {
		this.hasEnchantment = hasEnchantment;
	}
	
	public void setItemStack(Map<String, Object> itemStack) {
		this.itemStack = itemStack;
	}
	
	public ItemStack toItemStack() {
		ItemStack stack = ItemStack.deserialize(this.itemStack);
		if (this.hasEnchantment) stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		
		if (this.hasColor) {
			LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
			meta.setColor(Color.fromRGB(this.color));
			stack.setItemMeta(meta);
		}
		
		return stack;
	}
	
	
	public static SerializableItemStack fromItemStack(ItemStack stack) {
		return fromItemStack(stack, false);
	}
	
	public static SerializableItemStack fromMaterial(Material mat) {
		return fromItemStack(new ItemStack(mat), false);
	}
	
	public static SerializableItemStack fromItemStack(ItemStack stack, boolean block) {
		Map<String, Object> serialized = stack.serialize();
		serialized.entrySet().removeIf(e -> !e.getKey().equalsIgnoreCase("v") && !e.getKey().equalsIgnoreCase("type") && !e.getKey().equalsIgnoreCase("damage"));
		
		SerializableItemStack serializableItemStack = new SerializableItemStack(serialized);
		
		if (!block) {
			serializableItemStack.setHasEnchantment(stack.getEnchantments().size() > 0);

			if (stack.hasItemMeta() && stack.getItemMeta() instanceof LeatherArmorMeta) {
				LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
				serializableItemStack.setHasColor(true);
				serializableItemStack.setColor(meta.getColor().asRGB());
			}
		}
		
		return serializableItemStack;
	}

}
