package me.jumper251.replay.utils.version;

import com.google.common.base.Enums;
import org.bukkit.entity.EntityType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EntityBridge {

    FISHING_BOBBER("FISHING_HOOK"),
    ITEM("DROPPED_ITEM"),
    TNT("PRIMED_TNT");

    private static final Map<String, EntityType> entityTypeMap = new HashMap<>();

    static {
        EnumSet.allOf(EntityBridge.class).forEach(entityBridge -> {
            EntityType type = Enums.getIfPresent(EntityType.class, entityBridge.name()).toJavaUtil()
                    .orElse(EntityType.valueOf(entityBridge.getLegacyName()));
            entityTypeMap.put(entityBridge.getLegacyName(), type);
        });
    }

    private String legacyName;

    EntityBridge(String legacyName) {
        this.legacyName = legacyName;
    }

    public EntityType toEntityType() {
        return entityTypeMap.get(name());
    }

    public String getLegacyName() {
        return legacyName;
    }
}
