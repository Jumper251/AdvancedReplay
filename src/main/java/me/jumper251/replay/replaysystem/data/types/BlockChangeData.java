package me.jumper251.replay.replaysystem.data.types;

public class BlockChangeData extends PacketData {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -5904448938888041162L;
	
	private LocationData location;
	
	private ItemData before, after;
	
	private boolean playEffect = true;
	private boolean doBlockChange = true;
	
	public BlockChangeData(LocationData location, ItemData before, ItemData after) {
		this.location = location;
		this.before = before;
		this.after = after;
	}
	
	public BlockChangeData(LocationData location, ItemData before, ItemData after, boolean playEffect, boolean blockChange) {
		this.location = location;
		this.before = before;
		this.after = after;
		this.playEffect = playEffect;
		this.doBlockChange = blockChange;
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
	
	public boolean doPlayEffect() {
		return playEffect;
	}
	
	/**
	 * @return whether the block change needs to be played.
	 * <p>
	 * In case of an explosion the block change is already sent by the explosion packet.
	 * If ConfigManager.REAL_CHANGES is true, this value is ignored for the block change,
	 * but continues to be used for playing the Fuse sound.
	 */
	public boolean doBlockChange() {
		return doBlockChange;
	}
	
	public void setDoBlockChange(boolean doBlockChange) {
		this.doBlockChange = doBlockChange;
	}
}
