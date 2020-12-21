package me.jumper251.replay.replaysystem.utils;

import java.util.ArrayList;
import java.util.List;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.Material;

public class ItemUtils {

	private static final List<Material> INTERACTABLE = new ArrayList<> ();

	public static boolean isInteractable (Material mat) {
		if (mat == null)
			return false;

		return INTERACTABLE.contains (mat);
	}

	public static boolean isUsable (Material mat) {
		if (mat == null)
			return false;

		return mat.isEdible () || mat == Material.POTION || mat == Material.MILK_BUCKET || mat == Material.BOW || (!VersionUtil.isCompatible (VersionEnum.V1_8) && mat == Material.SHIELD) || (VersionUtil.isCompatible (VersionEnum.V1_8) && isSword (mat));
	}

	public static boolean isSword (Material mat) {
		return mat == Material.WOOD_SWORD || mat == Material.GOLD_SWORD || mat == Material.IRON_SWORD || mat == Material.DIAMOND_SWORD;
	}

	static {
		INTERACTABLE.add (Material.STONE_BUTTON);
		INTERACTABLE.add (Material.LEVER);
		INTERACTABLE.add (Material.CHEST);
		INTERACTABLE.add (Material.WOOD_BUTTON);
		INTERACTABLE.add (Material.WOOD_DOOR);
		INTERACTABLE.add (Material.REDSTONE_COMPARATOR);
		INTERACTABLE.add (Material.REDSTONE_COMPARATOR_OFF);
		INTERACTABLE.add (Material.REDSTONE_COMPARATOR_ON);
		if (!VersionUtil.isCompatible (VersionEnum.V1_8)) {
			INTERACTABLE.add (Material.COMMAND);
			INTERACTABLE.add (Material.COMMAND_CHAIN);
			INTERACTABLE.add (Material.COMMAND_MINECART);
			INTERACTABLE.add (Material.COMMAND_REPEATING);
		}
		INTERACTABLE.add (Material.BREWING_STAND);
		INTERACTABLE.add (Material.FURNACE);
		INTERACTABLE.add (Material.BURNING_FURNACE);
		INTERACTABLE.add (Material.SIGN);
		INTERACTABLE.add (Material.SIGN_POST);
		INTERACTABLE.add (Material.WALL_SIGN);
		INTERACTABLE.add (Material.TRAPPED_CHEST);
		INTERACTABLE.add (Material.TRAP_DOOR);
		INTERACTABLE.add (Material.ENCHANTMENT_TABLE);
		INTERACTABLE.add (Material.DROPPER);
		INTERACTABLE.add (Material.DISPENSER);
		INTERACTABLE.add (Material.ENDER_CHEST);
		INTERACTABLE.add (Material.FENCE_GATE);
		INTERACTABLE.add (Material.BEACON);
		INTERACTABLE.add (Material.NOTE_BLOCK);
		INTERACTABLE.add (Material.JUKEBOX);
		INTERACTABLE.add (Material.HOPPER);
		INTERACTABLE.add (Material.SPRUCE_DOOR);
		INTERACTABLE.add (Material.ACACIA_DOOR);
		INTERACTABLE.add (Material.DARK_OAK_DOOR);
		INTERACTABLE.add (Material.JUNGLE_DOOR);
		INTERACTABLE.add (Material.BIRCH_DOOR);
		INTERACTABLE.add (Material.SPRUCE_FENCE_GATE);
		INTERACTABLE.add (Material.ACACIA_FENCE_GATE);
		INTERACTABLE.add (Material.JUNGLE_FENCE_GATE);
		INTERACTABLE.add (Material.BIRCH_FENCE_GATE);
		INTERACTABLE.add (Material.DARK_OAK_FENCE_GATE);
		INTERACTABLE.add (Material.FENCE);
		INTERACTABLE.add (Material.SPRUCE_FENCE);
		INTERACTABLE.add (Material.JUNGLE_FENCE);
		INTERACTABLE.add (Material.ACACIA_FENCE);
		INTERACTABLE.add (Material.BIRCH_FENCE);
		INTERACTABLE.add (Material.DARK_OAK_FENCE);
		INTERACTABLE.add (Material.NETHER_FENCE);
		INTERACTABLE.add (Material.ANVIL);
		INTERACTABLE.add (Material.DAYLIGHT_DETECTOR);
		INTERACTABLE.add (Material.DAYLIGHT_DETECTOR_INVERTED);
		INTERACTABLE.add (Material.WORKBENCH);
	}
}
