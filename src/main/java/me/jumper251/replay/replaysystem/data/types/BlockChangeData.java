package me.jumper251.replay.replaysystem.data.types;

public class BlockChangeData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5904448938888041161L;

	
	private LocationData location;
	
	private ItemData before, after;
	
	public BlockChangeData(LocationData location, ItemData before, ItemData after) {
		this.location = location;
		this.before = before;
		this.after = after;
	}
	
	public LocationData getLocation() {
		return location;
	}
	
	public ItemData getAfter() {
		return after;
	}
	
	public ItemData getBefore() {
		return before;
	}
}
