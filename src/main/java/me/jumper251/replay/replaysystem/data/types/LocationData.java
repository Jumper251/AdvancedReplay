package me.jumper251.replay.replaysystem.data.types;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -849472505875330147L;
	
	private double x, y, z;
	
	private float yaw, pitch;
	
	private String world;
	
	public LocationData(double x, double y, double z, String world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		StringUtils.substringBefore(world, "_game");
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	
	public static LocationData fromLocation(Location loc) {
		return new LocationData(loc.getX(), loc.getY(), loc.getZ(), StringUtils.substringBefore(loc.getWorld().getName(), "_game"));
	}
	
	public static Location toLocation(LocationData locationData) {
		return new Location(Bukkit.getWorld(locationData.getWorld()), locationData.getX(), locationData.getY(), locationData.getZ());
	}

	public static Location toLocation(LocationData locationData, World world) {
		return new Location(world, locationData.getX(), locationData.getY(), locationData.getZ());
	}

	@Override
	public String toString() {
		return "LocationData{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				", yaw=" + yaw +
				", pitch=" + pitch +
				", world='" + world + '\'' +
				'}';
	}
}
