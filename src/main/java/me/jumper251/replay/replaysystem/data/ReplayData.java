package me.jumper251.replay.replaysystem.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.recording.PlayerWatcher;
import me.jumper251.replay.replaysystem.recording.optimization.ReplayQuality;


public class ReplayData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5238528050567979737L;

	
	private HashMap<Integer, List<ActionData>> actions;
	
	private HashMap<String, PlayerWatcher> watchers;
	
	private int duration;
	
	private String creator;
	
	private ReplayQuality quality;
	
	public ReplayData() {
		this.actions = new HashMap<Integer, List<ActionData>>();
		this.watchers = new HashMap<String, PlayerWatcher>();
		
		this.quality = ConfigManager.QUALITY;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public ReplayQuality getQuality() {
		return quality;
	}
	
	public HashMap<Integer, List<ActionData>> getActions() {
		return actions;
	}
	
	public HashMap<String, PlayerWatcher> getWatchers() {
		return watchers;
	}
	
	public void setWatchers(HashMap<String, PlayerWatcher> watchers) {
		this.watchers = watchers;
	}
	
	public PlayerWatcher getWatcher(String name) {
		if (watchers.containsKey(name)) {
			return watchers.get(name);
		} else {
			return null;
		}
	}
}
