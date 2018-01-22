package me.jumper251.replay.replaysystem.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import me.jumper251.replay.replaysystem.data.types.InvData;
import me.jumper251.replay.replaysystem.data.types.ItemData;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class NPCManager {

	public static List<String> names = new ArrayList<String>();
	
	
	
	public static List<WrapperPlayServerEntityEquipment> updateEquipment(int id, InvData data) {
		List<WrapperPlayServerEntityEquipment> list = new ArrayList<WrapperPlayServerEntityEquipment>();

			WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();
			packet.setEntityID(id);
			packet.setSlot(ItemSlot.HEAD);
			packet.setItem(fromID(data.getHead()));
			list.add(packet);
		
		
			WrapperPlayServerEntityEquipment packet1 = new WrapperPlayServerEntityEquipment();
			packet1.setEntityID(id);
			packet1.setSlot(ItemSlot.CHEST);
			packet1.setItem(fromID(data.getChest()));
			list.add(packet1);
		
		
			WrapperPlayServerEntityEquipment packet2 = new WrapperPlayServerEntityEquipment();
			packet2.setEntityID(id);
			packet2.setSlot(ItemSlot.LEGS);
			packet2.setItem(fromID(data.getLeg()));
			list.add(packet2);
		
		
			WrapperPlayServerEntityEquipment packet3 = new WrapperPlayServerEntityEquipment();
			packet3.setEntityID(id);
			packet3.setSlot(ItemSlot.FEET);
			packet3.setItem(fromID(data.getBoots()));
			list.add(packet3);
		
		
			WrapperPlayServerEntityEquipment packet4 = new WrapperPlayServerEntityEquipment();
			packet4.setEntityID(id);
			packet4.setSlot(ItemSlot.MAINHAND);
			packet4.setItem(fromID(data.getMainHand()));
			list.add(packet4);
		
		
		if(!VersionUtil.isCompatible(VersionEnum.V1_8)) {
			WrapperPlayServerEntityEquipment oacket5 = new WrapperPlayServerEntityEquipment();
			oacket5.setEntityID(id);
			oacket5.setSlot(ItemSlot.OFFHAND);
			oacket5.setItem(fromID(data.getOffHand()));
			list.add(oacket5);
		}
		
		return list;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack fromID(ItemData data) {
		if (data == null) return new ItemStack(0);
		return new ItemStack(data.getId(), 1, (short)data.getSubId());
	}
	
	@SuppressWarnings("deprecation")
	public static ItemData fromItemStack(ItemStack stack) {
		if (stack == null) return null;
		return new ItemData(stack.getTypeId(), stack.getData().getData());
	}
	
	@SuppressWarnings("deprecation")
	public static InvData copyFromPlayer(Player player, boolean armor, boolean off) {
		InvData data = new InvData();
		
		if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
			data.setMainHand(fromItemStack(player.getItemInHand()));
		} else {
			data.setMainHand(fromItemStack(player.getInventory().getItemInMainHand()));
			if (off) {
				data.setOffHand(fromItemStack(player.getInventory().getItemInOffHand()));
			}
		}
		
		if (armor) {
			data.setHead(fromItemStack(player.getInventory().getHelmet()));
			
			data.setChest(fromItemStack(player.getInventory().getChestplate()));
			
			data.setLeg(fromItemStack(player.getInventory().getLeggings()));
			
			data.setBoots(fromItemStack(player.getInventory().getBoots()));
			
		}
	
		
		return data;
	}
	
	public static boolean isArmor(ItemStack stack) {
		if (stack == null) return false;
		return ARMOR.contains(stack.getType());
	}
	
	public static String getArmorType(ItemStack stack) {
		if (stack == null) return null;
		
		if (stack.getType().toString().contains("HELMET")) return "head";
		if (stack.getType().toString().contains("CHESTPLATE")) return "chest";
		if (stack.getType().toString().contains("LEGGINGS")) return "leg";
		if (stack.getType().toString().contains("BOOTS")) return "boots";
		
		return null;

	}
	
	public static boolean wearsArmor(Player p, String type) {
		PlayerInventory inv = p.getInventory();
		if (type == null) return false;
		
		if (type.equals("head") && isArmor(inv.getHelmet())) return true;
		if (type.equals("chest") && isArmor(inv.getChestplate())) return true;
		if (type.equals("leg") && isArmor(inv.getLeggings())) return true;
		if (type.equals("boots") && isArmor(inv.getBoots())) return true;

		return false;
	}
	 
	
	private final static List<Material> ARMOR = Arrays.asList(new Material[] {
			Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
			Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
			Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
			Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS,
			Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS
	});
	
}
