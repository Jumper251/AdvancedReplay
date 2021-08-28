package me.jumper251.replay.replaysystem.utils.entities;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface INPC {

    void spawn(Location loc, int tabMode, Player... players);

    void respawn(Player... players);

    void despawn();

    void remove();

    void teleport(Location loc, boolean onGround);

    void move(Location loc, boolean onGround, float yaw, float pitch);

    void look(float yaw, float pitch);

    void updateMetadata();

    void animate(int id);

    void sleep(Location loc);

    void addToTeam(String team);

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    UUID getUuid();

    void setUuid(UUID uuid);

    WrappedDataWatcher getData();

    void setData(WrappedDataWatcher data);

    void setProfile(WrappedGameProfile profile);

    void setPitch(float pitch);

    void setYaw(float yaw);

    Location getLocation();

    void setLocation(Location location);

    Location getOrigin();

    void setOrigin(Location origin);

    Player[] getVisible();

    void setLastEquipment(List<WrapperPlayServerEntityEquipment> list);
}
