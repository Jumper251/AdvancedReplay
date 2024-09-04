package me.jumper251.replay.utils;


import me.jumper251.replay.legacy.LegacyMaterial;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.Material;
import org.bukkit.block.Block;

public enum MaterialBridge {

    CLOCK(Material.CLOCK, "WATCH"),
    OAK_DOOR(Material.OAK_DOOR, "WOOD_DOOR"),
    PLAYER_HEAD(Material.PLAYER_HEAD, "SKULL_ITEM"),
    GOLDEN_HELMET(Material.GOLDEN_HELMET, "GOLD_HELMET"),
    GOLDEN_CHESTPLATE(Material.GOLDEN_CHESTPLATE, "GOLD_CHESTPLATE"),
    GOLDEN_LEGGINGS(Material.GOLDEN_LEGGINGS, "GOLD_LEGGINGS"),
    GOLDEN_BOOTS(Material.GOLDEN_BOOTS, "GOLD_BOOTS"),
    WOODEN_SWORD(Material.WOODEN_SWORD, "WOOD_SWORD"),
    GOLDEN_SWORD(Material.GOLDEN_SWORD, "GOLD_SWORD"),
    OAK_BUTTON(Material.OAK_BUTTON, "WOOD_BUTTON"),
    COMPARATOR(Material.COMPARATOR, "REDSTONE_COMPARATOR"),
    COMPARATOR_OFF(null, "REDSTONE_COMPARATOR_OFF"),
    COMPARATOR_ON(null, "REDSTONE_COMPARATOR_ON"),
    COMMAND_BLOCK(Material.COMMAND_BLOCK, "COMMAND"),
    CHAIN_COMMAND_BLOCK(Material.CHAIN_COMMAND_BLOCK, "COMMAND_CHAIN"),
    COMMAND_BLOCK_MINECART(Material.COMMAND_BLOCK_MINECART, "COMMAND_MINECART"),
    REPEATING_COMMAND_BLOCK(Material.REPEATING_COMMAND_BLOCK, "COMMAND_REPEATING"),
    BURNING_FURNACE(null, "BURNING_FURNACE"),
    OAK_WALL_SIGN(Material.OAK_WALL_SIGN, "WALL_SIGN"),
    OAK_SIGN(Material.OAK_SIGN, "SIGN_POST"),
    ENCHANTING_TABLE(Material.ENCHANTING_TABLE, "ENCHANTMENT_TABLE"),
    OAK_TRAPDOOR(Material.OAK_TRAPDOOR, "TRAP_DOOR"),
    OAK_FENCE_GATE(Material.OAK_FENCE_GATE, "FENCE_GATE"),
    OAK_FENCE(Material.OAK_FENCE, "FENCE"),
    NETHER_BRICK_FENCE(Material.NETHER_BRICK_FENCE, "NETHER_FENCE"),
    CRAFTING_TABLE(Material.CRAFTING_TABLE, "WORKBENCH"),
    FIRE_CHARGE(Material.FIRE_CHARGE, "FIREBALL"),
    LAVA(Material.LAVA, "STATIONARY_LAVA"),
    WATER(Material.WATER, "STATIONARY_WATER");


    private String materialName;
    private Material material;

    MaterialBridge(Material material, String materialName) {
        this.materialName = materialName;
        this.material = material;
    }

    @SuppressWarnings("deprecation")
    public static Material fromID(int id) {
        if (VersionUtil.isAbove(VersionEnum.V1_13)) {
            for (Material mat : Material.values()) {
                if (mat.getId() == id) return mat;
            }

            return null;
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
        if (VersionUtil.isAbove(VersionEnum.V1_13)) {
            return material;
        }
        return Material.valueOf(getMaterialName());

    }

    public String getMaterialName() {
        return materialName;
    }
}
