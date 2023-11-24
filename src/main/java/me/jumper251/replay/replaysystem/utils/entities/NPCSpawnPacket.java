package me.jumper251.replay.replaysystem.utils.entities;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class NPCSpawnPacket {

    private WrapperPlayServerNamedEntitySpawn spawnNamedEntity;

    private WrapperPlayServerSpawnEntity spawnEntity;

    public NPCSpawnPacket(WrapperPlayServerNamedEntitySpawn spawnNamedEntity) {
        this.spawnNamedEntity = spawnNamedEntity;
    }

    public NPCSpawnPacket(WrapperPlayServerSpawnEntity spawnEntity) {
        this.spawnEntity = spawnEntity;
        spawnEntity.getHandle().getEntityTypeModifier().write(0, EntityType.PLAYER);
    }

    public void setEntityID(int id) {
        if (isOld()) {
            spawnNamedEntity.setEntityID(id);
        } else {
            spawnEntity.setEntityID(id);
        }
    }

    public void setPosition(Vector position) {
        if (isOld()) {
            spawnNamedEntity.setPosition(position);
        } else {
            spawnEntity.setPosition(position);
        }
    }


    public void setMetadata(WrappedDataWatcher data) {
        if (isOld()) {
            spawnNamedEntity.setMetadata(data);
        }
    }

    public void setX(double x) {
        if (isOld()) {
            spawnNamedEntity.setX(x);
        } else {
            spawnEntity.setX(x);
        }
    }

    public void setY(double y) {
        if (isOld()) {
            spawnNamedEntity.setY(y);
        } else {
            spawnEntity.setY(y);
        }
    }

    public void setZ(double z) {
        if (isOld()) {
            spawnNamedEntity.setZ(z);
        } else {
            spawnEntity.setZ(z);
        }
    }

    public void setYaw(float yaw) {
        if (isOld()) {
            spawnNamedEntity.setYaw(yaw);
        }
    }

    public void setPitch(float pitch) {
        if (isOld()) {
            spawnNamedEntity.setPitch(pitch);
        }
    }

    public void setUniqueId(UUID uuid) {
        if (isOld()) {
            spawnNamedEntity.setPlayerUUID(uuid);
        } else {
            spawnEntity.setUniqueId(uuid);
        }
    }

    public void sendPacket(Player player) {
        if (isOld()) {
            spawnNamedEntity.sendPacket(player);
        } else {
            spawnEntity.sendPacket(player);
        }
    }


    public boolean isOld() {
        return spawnNamedEntity != null;
    }
}
