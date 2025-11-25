package me.jumper251.replay.replaysystem.data.types;

import org.bukkit.util.Vector;

public class VelocityData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4046621273672598227L;
	
	private double x, y, z;
	
	private int id;
	
	public VelocityData(int id, double x, double y, double z) {
		this.id = id;
		this.y = y;
		this.x = x;
		this.z = z;
	}
	
	public int getId() {
		return id;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}

    public Vector toVector() {
        return new Vector(x, y, z);
    }

}
