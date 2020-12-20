package me.jumper251.replay.replaysystem.utils.entities;

import java.util.List;

import java.util.UUID;



import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

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
	
	String getName();
	
	UUID getUuid();
	
	void setId(int id);
	
	void setName(String name);
	
	void setUuid(UUID uuid);
	
	void setData(WrappedDataWatcher data);
	
	WrappedDataWatcher getData();
	
	void setProfile(WrappedGameProfile profile);
	
	void setPitch(float pitch);
	
	void setYaw(float yaw);
	
	Location getLocation();
	
	void setOrigin(Location origin);
	
	void setLocation(Location location);
	
	Location getOrigin();
	
	Player[] getVisible();
	
	void setLastEquipment(List<WrapperPlayServerEntityEquipment> list);
	
	
	
}
