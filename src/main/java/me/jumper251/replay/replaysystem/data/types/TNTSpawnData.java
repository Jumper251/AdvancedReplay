package me.jumper251.replay.replaysystem.data.types;

public class TNTSpawnData extends PacketData {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 2309497657075148314L;
	
	private int id;
	private LocationData locationData;
	
	
	public TNTSpawnData(int id, LocationData locationData) {
		this.id = id;
		this.locationData = locationData;
	}
	
	public int getId() {
		return id;
	}
	
	public LocationData getLocationData() {
		return locationData;
	}
	
}
