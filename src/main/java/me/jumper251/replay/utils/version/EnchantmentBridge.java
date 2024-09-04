package me.jumper251.replay.utils.version;

import org.bukkit.enchantments.Enchantment;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnchantmentBridge {

    UNBREAKING("DURABILITY");

    private static final Map<EnchantmentBridge, Enchantment> enchantmentTypeMap = new HashMap<>();

    static {
        EnumSet.allOf(EnchantmentBridge.class).forEach(type -> {
            Enchantment enchantment = Enchantment.getByName(type.name());
            if (enchantment == null) {
                enchantment = Enchantment.getByName(type.getLegacyName());
            }
            enchantmentTypeMap.put(type, enchantment);
        });
    }

    private String legacyName;

    EnchantmentBridge(String legacyName) {
        this.legacyName = legacyName;
    }

    public Enchantment toEnchantment() {
        return enchantmentTypeMap.get(this);
    }

    public String getLegacyName() {
        return legacyName;
    }
}
