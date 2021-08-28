package me.jumper251.replay.replaysystem.utils.entities;


import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IEntity {

    void spawn(Location loc, Player... players);

    void respawn(Player... players);

    void despawn();

    void remove();

    void teleport(Location loc, boolean onGround);

    void move(Location loc, boolean onGround, float yaw, float pitch);

    void look(float yaw, float pitch);

    void updateMetadata();

    void animate(int id);

    int getId();

    void setId(int id);

    WrappedDataWatcher getData();

    void setData(WrappedDataWatcher data);

    void setPitch(float pitch);

    void setYaw(float yaw);

    Location getLocation();

    void setLocation(Location location);

    Location getOrigin();

    void setOrigin(Location origin);

    Player[] getVisible();

}
