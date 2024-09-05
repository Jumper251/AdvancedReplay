package me.jumper251.replay.filesystem;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.utils.LogUtils;
import me.jumper251.replay.utils.MaterialBridge;
import me.jumper251.replay.utils.VersionUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemConfig {


    private static final Map<String, String> FIXES = new HashMap<>();

    public static File file = new File(ReplaySystem.getInstance().getDataFolder(), "items.yml");

    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static HashMap<ItemConfigType, ItemConfigOption> items = new HashMap<>();

    static {
        FIXES.put("SKULL_ITEM", "PLAYER_HEAD");
        FIXES.put("WOOD_DOOR", "OAK_DOOR");
        FIXES.put("WATCH", "CLOCK");
    }

    public static void loadConfig() {
        addDefaults();

        if (!file.exists()) {
            for (ItemConfigType type : items.keySet()) {
                String name = type.name().toLowerCase();
                ItemConfigOption item = items.get(type);

                cfg.set("items." + name + ".name", item.getName());
                cfg.set("items." + name + ".id", item.getMaterial().name() + (item.getData() != 0 ? ":" + item.getData() : ""));
                cfg.set("items." + name + ".slot", item.getSlot());

                if (item.getOwner() != null) {
                    cfg.set("items." + name + ".owner", item.getOwner());
                }

                if (item.getTexture() != null) {
                    cfg.set("items." + name + ".texture", item.getTexture());
                }

                cfg.set("items." + name + ".enabled", item.isEnabled());

            }

            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void loadData() {
        for (ItemConfigType type : ItemConfigType.values()) {
            String name = type.name().toLowerCase();

            String displayName = cfg.getString("items." + name + ".name");
            String owner = cfg.getString("items." + name + ".owner");
            String texture = cfg.getString("items." + name + ".texture");
            String matString = cfg.getString("items." + name + ".id").toUpperCase();
            int slot = cfg.getInt("items." + name + ".slot");
            boolean enabled = cfg.getBoolean("items." + name + ".enabled", true);

            int data = 0;
            if (matString.contains(":")) {
                String[] split = matString.split(":");
                matString = split[0];
                data = Integer.valueOf(split[1]);
            }

            Material material = Material.STONE;

            if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_13) && FIXES.containsKey(matString)) {
                matString = FIXES.get(matString);
            }
            try {
                material = Material.valueOf(matString);
            } catch (Exception e) {
                LogUtils.log("Could not load item " + name + " with material " + matString);
            }

            items.put(type, new ItemConfigOption(material, displayName, slot, owner, data, texture).enable(enabled));

        }
    }


    public static ItemConfigOption getItem(ItemConfigType type) {
        return items.get(type);
    }

    public static ItemConfigType getByIdAndName(Material material, String name) {
        for (ItemConfigType type : items.keySet()) {
            ItemConfigOption option = items.get(type);
						
			/*if (option.getMaterial() == Material.WOOD_DOOR && (VersionUtil.isCompatible(VersionEnum.V1_13) || VersionUtil.isCompatible(VersionEnum.V1_14) || VersionUtil.isCompatible(VersionEnum.V1_15) || VersionUtil.isCompatible(VersionEnum.V1_16) || VersionUtil.isCompatible(VersionEnum.V1_17) || VersionUtil.isCompatible(VersionEnum.V1_18) || VersionUtil.isCompatible(VersionEnum.V1_19) || VersionUtil.isCompatible(VersionEnum.V1_20) || VersionUtil.isCompatible(VersionEnum.V1_21))) {
				if (material.name().equals(MaterialBridge.WOOD_DOOR.getMaterialName()) && option.getName().equals(name)) return type;
			}*/

            if (option.getMaterial() == material && option.getName().equals(name)) return type;
        }
        return null;
    }

    private static void addDefaults() {
        items.put(ItemConfigType.TELEPORT, new ItemConfigOption(Material.COMPASS, "&7Teleport", 0));
        items.put(ItemConfigType.SPEED, new ItemConfigOption(MaterialBridge.CLOCK.toMaterial(), "&cSlow &8[&eRight&8] &aFast &8[&eShift Right&8]", 1));
        items.put(ItemConfigType.LEAVE, new ItemConfigOption(MaterialBridge.OAK_DOOR.toMaterial(), "&7Leave replay", 8));
        items.put(ItemConfigType.RESUME, new ItemConfigOption(Material.SLIME_BLOCK, "&aResume", 4));

        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_21)) {
            items.put(ItemConfigType.FORWARD, new ItemConfigOption(MaterialBridge.PLAYER_HEAD.toMaterial(), "&a» &e10 seconds", 5, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM0ZWYwNjM4NTM3MjIyYjIwZjQ4MDY5NGRhZGMwZjg1ZmJlMDc1OWQ1ODFhYTdmY2RmMmU0MzEzOTM3NzE1OCJ9fX0="));
            items.put(ItemConfigType.BACKWARD, new ItemConfigOption(MaterialBridge.PLAYER_HEAD.toMaterial(), "&c« &e10 seconds", 3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhYWNhZDE5M2UyMjI2OTcxZWQ5NTMwMmRiYTQzMzQzOGJlNDY0NGZiYWI1ZWJmODE4MDU0MDYxNjY3ZmJlMiJ9fX0="));
            items.put(ItemConfigType.PAUSE, new ItemConfigOption(MaterialBridge.PLAYER_HEAD.toMaterial(), "&cPause", 4, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM4NTFjZmI2NzE3Nzk1OTc0ZDMyMmU3YmI2OThlOWFiNTdlMWUwZTIyOTUwNDg2YTY5OWM4MThlMDJlOTBiMjkifX19"));
        } else {
            items.put(ItemConfigType.FORWARD, new ItemConfigOption(MaterialBridge.PLAYER_HEAD.toMaterial(), "&a» &e10 seconds", 5, "MHF_ArrowRight", 3));
            items.put(ItemConfigType.BACKWARD, new ItemConfigOption(MaterialBridge.PLAYER_HEAD.toMaterial(), "&c« &e10 seconds", 3, "MHF_ArrowLeft", 3));
            items.put(ItemConfigType.PAUSE, new ItemConfigOption(MaterialBridge.PLAYER_HEAD.toMaterial(), "&cPause", 4, "Push_red_button", 3));
        }
    }


}
