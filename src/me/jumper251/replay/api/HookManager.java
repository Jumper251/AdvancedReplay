package me.jumper251.replay.api;

public class HookManager {

	private IReplayHook hook;
	
	
	public void registerHook(IReplayHook hook) {
		this.hook = hook;
	}
	
	public void unregisterHook() {
		this.hook = null;
	}
	
	public boolean isRegistered() {
		return this.hook != null;
	}
	
	public IReplayHook getHook() {
		return hook;
	}
}
