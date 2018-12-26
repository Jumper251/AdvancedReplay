package me.jumper251.replay.utils;

import java.util.HashMap;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.replay.ReplayCommand;
import me.jumper251.replay.listener.ReplayListener;
import me.jumper251.replay.replaysystem.Replay;

public class ReplayManager {

	public static HashMap<String, Replay> activeReplays = new HashMap<String, Replay>();
	
	public static void register() {
		registerEvents();
		registerCommands();
	}
	
	private static void registerEvents() {
		new ReplayListener().register();
	}
	
	private static void registerCommands() {
		ReplaySystem.getInstance().getCommand("replay").setExecutor(new ReplayCommand());;
	}
	
	public static void addMetrics() {
		
	}

}
