package me.jumper251.replay.replaysystem.data.types;

public class EntityData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2309497657075148314L;
	
	private int action, id;
	
	private LocationData location;
	
	private String type;
	
	public EntityData(int action, int id, LocationData location, String type) {
		this.action = action;
		this.id = id;
		this.location = location;
		this.type = type;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getId() {
		return id;
	}
	
	public LocationData getLocation() {
		return location;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
