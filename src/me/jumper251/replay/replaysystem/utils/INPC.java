package me.jumper251.replay.replaysystem.utils;

import java.util.UUID;



import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public interface INPC {

	void spawn(Location loc, int tabMode, Player... players);
	
	void respawn();
	
	void despawn();
	
	void remove();
	
	void teleport(Location loc, boolean onGround);
	
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
	
	
	
}
