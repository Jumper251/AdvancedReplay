package me.jumper251.replay.utils.version;


import com.google.common.base.Enums;
import me.jumper251.replay.legacy.LegacyMaterial;
import me.jumper251.replay.utils.LogUtils;
import me.jumper251.replay.utils.ReflectionHelper;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MaterialBridge {

    CLOCK("WATCH"),
    OAK_DOOR("WOOD_DOOR"),
    PLAYER_HEAD("SKULL_ITEM"),
    GOLDEN_HELMET("GOLD_HELMET"),
    GOLDEN_CHESTPLATE("GOLD_CHESTPLATE"),
    GOLDEN_LEGGINGS("GOLD_LEGGINGS"),
    GOLDEN_BOOTS("GOLD_BOOTS"),
    WOODEN_SWORD("WOOD_SWORD"),
    GOLDEN_SWORD("GOLD_SWORD"),
    OAK_BUTTON("WOOD_BUTTON"),
    COMPARATOR("REDSTONE_COMPARATOR"),
    COMPARATOR_OFF("REDSTONE_COMPARATOR_OFF"),
    COMPARATOR_ON("REDSTONE_COMPARATOR_ON"),
    COMMAND_BLOCK("COMMAND"),
    CHAIN_COMMAND_BLOCK("COMMAND_CHAIN"),
    COMMAND_BLOCK_MINECART("COMMAND_MINECART"),
    REPEATING_COMMAND_BLOCK("COMMAND_REPEATING"),
    BURNING_FURNACE("BURNING_FURNACE"),
    OAK_WALL_SIGN("WALL_SIGN"),
    OAK_SIGN("SIGN_POST"),
    ENCHANTING_TABLE("ENCHANTMENT_TABLE"),
    OAK_TRAPDOOR("TRAP_DOOR"),
    OAK_FENCE_GATE("FENCE_GATE"),
    OAK_FENCE("FENCE"),
    NETHER_BRICK_FENCE("NETHER_FENCE"),
    CRAFTING_TABLE("WORKBENCH"),
    FIRE_CHARGE("FIREBALL"),
    LAVA("STATIONARY_LAVA"),
    WATER("STATIONARY_WATER");

    private static final Map<String, Material> materialMap = new HashMap<>();

    static {
        EnumSet.allOf(MaterialBridge.class).forEach(materialBridge -> {
            Material mat = Enums.getIfPresent(Material.class, materialBridge.name())
                    .or(() -> {
                        try {
                            return Material.valueOf(materialBridge.getMaterialName());
                        } catch (Exception e) {
                            return Material.AIR;
                        }
                    });
            materialMap.put(materialBridge.name(), mat);
        });
    }

    private String materialName;

    MaterialBridge(String materialName) {
        this.materialName = materialName;
    }

    public static Material fromID(int id) {
        if (VersionUtil.isAbove(VersionEnum.V1_13)) {
            if (id != 0) {
                LogUtils.log("Could not parse material by id on 1.13+");
            }
            return Material.AIR;
        } else {
            return LegacyMaterial.getMaterialById(id);
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

    public Material toMaterial() {
        return materialMap.get(name());
    }

    public String getMaterialName() {
        return materialName;
    }
}
