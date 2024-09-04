package me.jumper251.replay.replaysystem.utils;

import me.jumper251.replay.utils.MaterialBridge;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;


public class ItemUtils {

    private static final List<Material> INTERACTABLE = new ArrayList<>();

    static {
        INTERACTABLE.add(Material.STONE_BUTTON);
        INTERACTABLE.add(Material.LEVER);
        INTERACTABLE.add(Material.CHEST);
        INTERACTABLE.add(MaterialBridge.OAK_BUTTON.toMaterial());
        INTERACTABLE.add(MaterialBridge.OAK_BUTTON.toMaterial());
        INTERACTABLE.add(MaterialBridge.COMPARATOR.toMaterial());

        if (!VersionUtil.isCompatible(VersionEnum.V1_8)) {
            INTERACTABLE.add(MaterialBridge.COMMAND_BLOCK.toMaterial());
            INTERACTABLE.add(MaterialBridge.CHAIN_COMMAND_BLOCK.toMaterial());
            INTERACTABLE.add(MaterialBridge.COMMAND_BLOCK_MINECART.toMaterial());
            INTERACTABLE.add(MaterialBridge.REPEATING_COMMAND_BLOCK.toMaterial());
            INTERACTABLE.add(MaterialBridge.COMPARATOR_OFF.toMaterial());
            INTERACTABLE.add(MaterialBridge.COMPARATOR_ON.toMaterial());
            INTERACTABLE.add(MaterialBridge.BURNING_FURNACE.toMaterial());
        }
        INTERACTABLE.add(Material.BREWING_STAND);
        INTERACTABLE.add(Material.FURNACE);
        INTERACTABLE.add(MaterialBridge.OAK_SIGN.toMaterial());
        INTERACTABLE.add(MaterialBridge.OAK_WALL_SIGN.toMaterial());
        INTERACTABLE.add(Material.TRAPPED_CHEST);
        INTERACTABLE.add(MaterialBridge.OAK_TRAPDOOR.toMaterial());
        INTERACTABLE.add(MaterialBridge.ENCHANTING_TABLE.toMaterial());
        INTERACTABLE.add(Material.DROPPER);
        INTERACTABLE.add(Material.DISPENSER);
        INTERACTABLE.add(Material.ENDER_CHEST);
        INTERACTABLE.add(MaterialBridge.OAK_FENCE_GATE.toMaterial());
        INTERACTABLE.add(Material.BEACON);
        INTERACTABLE.add(Material.NOTE_BLOCK);
        INTERACTABLE.add(Material.JUKEBOX);
        INTERACTABLE.add(Material.HOPPER);
        INTERACTABLE.add(Material.SPRUCE_DOOR);
        INTERACTABLE.add(Material.ACACIA_DOOR);
        INTERACTABLE.add(Material.DARK_OAK_DOOR);
        INTERACTABLE.add(Material.JUNGLE_DOOR);
        INTERACTABLE.add(Material.BIRCH_DOOR);
        INTERACTABLE.add(Material.SPRUCE_FENCE_GATE);
        INTERACTABLE.add(Material.ACACIA_FENCE_GATE);
        INTERACTABLE.add(Material.JUNGLE_FENCE_GATE);
        INTERACTABLE.add(Material.BIRCH_FENCE_GATE);
        INTERACTABLE.add(Material.DARK_OAK_FENCE_GATE);
        INTERACTABLE.add(MaterialBridge.OAK_FENCE.toMaterial());
        INTERACTABLE.add(Material.SPRUCE_FENCE);
        INTERACTABLE.add(Material.JUNGLE_FENCE);
        INTERACTABLE.add(Material.ACACIA_FENCE);
        INTERACTABLE.add(Material.BIRCH_FENCE);
        INTERACTABLE.add(Material.DARK_OAK_FENCE);
        INTERACTABLE.add(MaterialBridge.NETHER_BRICK_FENCE.toMaterial());
        INTERACTABLE.add(Material.ANVIL);
        INTERACTABLE.add(Material.DAYLIGHT_DETECTOR);

        INTERACTABLE.add(MaterialBridge.CRAFTING_TABLE.toMaterial());
    }

    public static boolean isInteractable(Material mat) {
        if (mat == null) return false;

        return INTERACTABLE.contains(mat);
    }

    public static boolean isUsable(Material mat) {
        if (mat == null) return false;

        return mat.isEdible() || mat == Material.POTION || mat == Material.MILK_BUCKET || mat == Material.BOW || (!VersionUtil.isCompatible(VersionEnum.V1_8) && mat == Material.SHIELD) || (VersionUtil.isCompatible(VersionEnum.V1_8) && isSword(mat));
    }

    public static boolean isSword(Material mat) {
        return mat.name().contains("SWORD");
    }
}
