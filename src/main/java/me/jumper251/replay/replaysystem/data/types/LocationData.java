package me.jumper251.replay.replaysystem.data.types;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -849472505875330147L;

	private double x, y, z;

	private float yaw, pitch;

	private String world;

	public LocationData (double x, double y, double z, String world) {
		this.x     = x;
		this.y     = y;
		this.z     = z;
		this.world = world;
	}

	public float getPitch () {
		return pitch;
	}

	public String getWorld () {
		return world;
	}

	public double getX () {
		return x;
	}

	public double getY () {
		return y;
	}

	public float getYaw () {
		return yaw;
	}

	public double getZ () {
		return z;
	}

	public void setYaw (float yaw) {
		this.yaw = yaw;
	}

	public void setPitch (float pitch) {
		this.pitch = pitch;
	}

	public static LocationData fromLocation (Location loc) {
		return new LocationData (loc.getX (), loc.getY (), loc.getZ (), loc.getWorld ().getName ());
	}

	public static Location toLocation (LocationData locationData) {
		return new Location (Bukkit.getWorld (locationData.getWorld ()), locationData.getX (), locationData.getY (), locationData.getZ ());
	}
}
