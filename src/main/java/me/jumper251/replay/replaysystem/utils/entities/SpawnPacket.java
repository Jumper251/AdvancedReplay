package me.jumper251.replay.replaysystem.utils.entities;

import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpawnPacket {

    private WrapperPlayServerSpawnEntityLiving spawnEntityLiving;

    private WrapperPlayServerSpawnEntity spawnEntity;

    public SpawnPacket(WrapperPlayServerSpawnEntityLiving spawnEntityLiving) {
        this.spawnEntityLiving = spawnEntityLiving;
    }

    public SpawnPacket(WrapperPlayServerSpawnEntity spawnEntity) {
        this.spawnEntity = spawnEntity;
    }

    public void setEntityID(int id) {
        if (isOld()) {
            spawnEntityLiving.setEntityID(id);
        } else {
            spawnEntity.setEntityID(id);
        }
    }

    public void setType(EntityType type) {
        if (isOld()) {
            spawnEntityLiving.setType(type);
        } else {
            spawnEntity.getHandle().getEntityTypeModifier().write(0, type);
        }
    }

    public void setMetadata(WrappedDataWatcher data) {
        if (isOld()) {
            spawnEntityLiving.setMetadata(data);
        }
    }

    public void setX(double x) {
        if (isOld()) {
            spawnEntityLiving.setX(x);
        } else {
            spawnEntity.setX(x);
        }
    }

    public void setY(double y) {
        if (isOld()) {
            spawnEntityLiving.setY(y);
        } else {
            spawnEntity.setY(y);
        }
    }

    public void setZ(double z) {
        if (isOld()) {
            spawnEntityLiving.setZ(z);
        } else {
            spawnEntity.setZ(z);
        }
    }

    public void setYaw(float yaw) {
        if (isOld()) {
            spawnEntityLiving.setYaw(yaw);
        }
    }

    public void setPitch(float pitch) {
        if (isOld()) {
            spawnEntityLiving.setPitch(pitch);
        }
    }

    public void setUniqueId(UUID uuid) {
        if (isOld()) {
            spawnEntityLiving.setUniqueId(uuid);
        } else {
            spawnEntity.setUniqueId(uuid);
        }
    }

    public void sendPacket(Player player) {
        if (isOld()) {
            spawnEntityLiving.sendPacket(player);
        } else {
            spawnEntity.sendPacket(player);
        }
    }


    public boolean isOld() {
        return spawnEntityLiving != null;
    }
}
