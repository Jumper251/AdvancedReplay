package me.jumper251.replay.replaysystem.data.types;

import me.jumper251.replay.replaysystem.recording.PlayerWatcher;

public class MetadataUpdate extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8969498588009941633L;

	private boolean burning, blocking, gliding, swimming;
		
	public MetadataUpdate(boolean burning, boolean blocking) {
		this.burning = burning;
		this.blocking = blocking;
	}
	
	public MetadataUpdate(boolean burning, boolean blocking, boolean gliding) {
		this(burning, blocking);
		
		this.gliding = gliding;
	}
	
	public MetadataUpdate(boolean burning, boolean blocking, boolean gliding, boolean swimming) {
		this(burning, blocking, gliding);
		
		this.swimming = swimming;
	}
	
	public boolean isBurning() {
		return burning;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	public boolean isGliding() {
		return gliding;
	}
	
	public boolean isSwimming() {
		return swimming;
	}

	public static MetadataUpdate fromWatcher(PlayerWatcher watcher) {
		return new MetadataUpdate(watcher.isBurning(), watcher.isBlocking(), watcher.isElytra(), watcher.isSwimming());
	}
	
}
