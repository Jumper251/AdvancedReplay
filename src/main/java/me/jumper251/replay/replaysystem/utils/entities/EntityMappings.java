package me.jumper251.replay.replaysystem.utils.entities;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public class EntityMappings {

    private static EntityMappings instance;


    private BiMap<String, Integer> entityLookup = HashBiMap.create();

    private EntityMappings() {
        this.createMappings();
    }

    public static EntityMappings getInstance() {
        if (instance == null) instance = new EntityMappings();

        return instance;
    }

    public String getType(int id) {
        return this.entityLookup.inverse().get(id);
    }

    public int getTypeId(String entityType) {
        return this.entityLookup.getOrDefault(entityType, 0);
    }

    private void createMappings() {
        entityLookup.put("AXOLOTL", 3);
        entityLookup.put("BAT", 4);
        entityLookup.put("BEE", 5);
        entityLookup.put("BLAZE", 6);
        entityLookup.put("CAT", 8);
        entityLookup.put("CAVE_SPIDER", 9);
        entityLookup.put("CHICKEN", 10);
        entityLookup.put("COD", 11);
        entityLookup.put("COW", 12);
        entityLookup.put("CREEPER", 13);
        entityLookup.put("DOLPHIN", 14);
        entityLookup.put("DONKEY", 15);
        entityLookup.put("DROWNED", 17);
        entityLookup.put("ELDER_GUARDIAN", 18);
        entityLookup.put("ENDER_DRAGON", 20);
        entityLookup.put("ENDERMAN", 21);
        entityLookup.put("ENDERMITE", 22);
        entityLookup.put("EVOKER", 23);
        entityLookup.put("FOX", 29);
        entityLookup.put("GHAST", 30);
        entityLookup.put("GIANT", 31);
        entityLookup.put("GLOW_SQUID", 33);
        entityLookup.put("GOAT", 34);
        entityLookup.put("GUARDIAN", 35);
        entityLookup.put("HOGLIN", 36);
        entityLookup.put("HORSE", 37);
        entityLookup.put("HUSK", 38);
        entityLookup.put("ILLUSIONER", 39);
        entityLookup.put("IRON_GOLEM", 40);
        entityLookup.put("LLAMA", 46);
        entityLookup.put("MAGMA_CUBE", 48);
        entityLookup.put("MULE", 57);
        entityLookup.put("MUSHROOM_COW", 58);
        entityLookup.put("OCELOT", 59);
        entityLookup.put("PANDA", 61);
        entityLookup.put("PARROT", 62);
        entityLookup.put("PHANTOM", 63);
        entityLookup.put("PIG", 64);
        entityLookup.put("PIGLIN", 65);
        entityLookup.put("PIGLIN_BRUTE", 66);
        entityLookup.put("PILLAGER", 67);
        entityLookup.put("POLAR_BEAR", 68);
        entityLookup.put("PUFFERFISH", 70);
        entityLookup.put("RABBIT", 71);
        entityLookup.put("RAVAGER", 72);
        entityLookup.put("SALMON", 73);
        entityLookup.put("SHEEP", 74);
        entityLookup.put("SHULKER", 75);
        entityLookup.put("SILVERFISH", 77);
        entityLookup.put("SKELETON", 78);
        entityLookup.put("SKELETON_HORSE", 79);
        entityLookup.put("SLIME", 80);
        entityLookup.put("SNOWMAN", 82);
        entityLookup.put("SPIDER", 85);
        entityLookup.put("SQUID", 86);
        entityLookup.put("STRAY", 87);
        entityLookup.put("STRIDER", 88);
        entityLookup.put("TRADER_LLAMA", 94);
        entityLookup.put("TROPICAL_FISH", 95);
        entityLookup.put("TURTLE", 96);
        entityLookup.put("VEX", 97);
        entityLookup.put("VILLAGER", 98);
        entityLookup.put("VINDICATOR", 99);
        entityLookup.put("WANDERING_TRADER", 100);
        entityLookup.put("WITCH", 101);
        entityLookup.put("WITHER", 102);
        entityLookup.put("WITHER_SKELETON", 103);
        entityLookup.put("WOLF", 105);
        entityLookup.put("ZOGLIN", 106);
        entityLookup.put("ZOMBIE", 107);
        entityLookup.put("ZOMBIE_HORSE", 108);
        entityLookup.put("ZOMBIE_VILLAGER", 109);
        entityLookup.put("ZOMBIFIED_PIGLIN", 110);
        entityLookup.put("WARDEN", 111);
        entityLookup.put("FROG", 112);
        entityLookup.put("TADPOLE", 113);
        entityLookup.put("ALLAY", 114);
        entityLookup.put("CAMEL", 115);
        entityLookup.put("SNIFFER", 116);
        entityLookup.put("ARMADILLO", 117);
        entityLookup.put("BREEZE", 118);
        entityLookup.put("BOGGED", 119);
        entityLookup.put("CREAKING", 120);
        entityLookup.put("HAPPY_GHAST", 121);
        entityLookup.put("COPPER_GOLEM", 122);
        entityLookup.put("NAUTILUS", 123);
        entityLookup.put("ZOMBIE_NAUTILUS", 124);
        entityLookup.put("CAMEL_HUSK", 125);
        entityLookup.put("PARCHED", 126);
    }

}
