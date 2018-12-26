package me.jumper251.replay.replaysystem.utils.entities;



import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public interface IEntity {

	void spawn(Location loc, Player... players);
	
	void respawn(Player... players);
	
	void despawn();
	
	void remove();
	
	void teleport(Location loc, boolean onGround);

	void look(float yaw, float pitch);
	
	void updateMetadata();
	
	void animate(int id); 
	
	int getId();
		
	void setId(int id);

	void setData(WrappedDataWatcher data);
	
	WrappedDataWatcher getData();
	
	void setPitch(float pitch);
	
	void setYaw(float yaw);
	
	Location getLocation();
	
	void setOrigin(Location origin);
	
	void setLocation(Location location);
	
	Location getOrigin();
	
	Player[] getVisible();

}
