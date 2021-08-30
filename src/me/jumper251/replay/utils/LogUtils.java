package me.jumper251.replay.utils;

import me.jumper251.replay.ReplaySystem;

public class LogUtils {

	public static void log(String message){
		ReplaySystem.getInstance().getLogger().info(message);
	}

}
