package me.jumper251.replay.replaysystem.recording;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.types.MovingData;
import me.jumper251.replay.replaysystem.utils.ReplayQuality;

public class ReplayOptimizer {

	private MovingData lastMovement;
	
	private int playerCount;

	public boolean shouldRecordPlayerMovement(MovingData data) {
		if (this.lastMovement == null) {
			this.lastMovement = data;
			return true;
		}
				
		this.playerCount = this.playerCount >= countToRemove() ? 0 : this.playerCount + 1;
		
		return calculateDifference(data) > requiredDifference() && (ConfigManager.QUALITY == ReplayQuality.HIGH || this.playerCount != countToRemove());
	}
	
	public int countToRemove() {
		if (ConfigManager.QUALITY == ReplayQuality.LOW) return 1;
		if (ConfigManager.QUALITY == ReplayQuality.MEDIUM) return 4;

		return -1;
	}
	
	public double requiredDifference() {
		if (ConfigManager.QUALITY == ReplayQuality.LOW) return 0.06D;
		if (ConfigManager.QUALITY == ReplayQuality.MEDIUM) return 0.05D;
		
		return 0;
	}
	
	
	public double calculateDifference(MovingData data) {
		double locationDiff = Math.abs(data.getX() - lastMovement.getX()) + Math.abs(data.getY() - lastMovement.getY()) + Math.abs(data.getZ() - lastMovement.getZ());
		double rotationDiff = Math.abs(data.getYaw() - lastMovement.getYaw()) + Math.abs(data.getPitch() - lastMovement.getPitch());
		
		this.lastMovement = data;
		return locationDiff + rotationDiff;
	}
	
	
	
	public static Map<Object, Long> analyzeReplay(Replay replay) {
		HashMap<Integer, List<ActionData>> data = replay.getData().getActions();
		
		List<ActionData> merged = new ArrayList<>();
		data.values().forEach(merged::addAll);
		merged = merged.stream()
				.filter(action -> action.getPacketData() != null)
				.collect(Collectors.toList());
	
		return merged.stream()
				.map(action -> action.getPacketData().getClass().getSimpleName())
				.collect(Collectors.groupingBy(type -> type, Collectors.counting()));
	}
	
	
}
