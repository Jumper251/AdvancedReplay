package me.jumper251.replay.replaysystem.recording.optimization;


import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReplayOptimizer {

    private MovingData lastMovement;

    private int playerCount;

    private int entityMovementCount;

    private int velocityCount;

    public static ReplayStats analyzeReplay(Replay replay) {
        HashMap<Integer, List<ActionData>> data = replay.getData().getActions();

        List<ActionData> merged = new ArrayList<>();
        data.values().forEach(merged::addAll);

        long entityCount = merged.stream()
                .filter(action -> action.getPacketData() instanceof EntityData && ((EntityData) action.getPacketData()).getAction() == 0)
                .count();

        List<String> players = merged.stream()
                .map(action -> action.getName())
                .distinct()
                .collect(Collectors.toList());

        Map<Object, Long> actions = merged.stream()
                .filter(action -> action.getPacketData() != null)
                .map(action -> action.getPacketData().getClass().getSimpleName())
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        return new ReplayStats(actions, players, entityCount);

    }

    public boolean shouldRecord(PacketData data) {
        if (data instanceof MovingData) {
            return shouldRecordPlayerMovement((MovingData) data);
        } else if (data instanceof EntityMovingData || data instanceof VelocityData) {
            return shouldRecordEntityMovement(data);
        } else {
            return true;
        }
    }

    public boolean shouldRecordPlayerMovement(MovingData data) {
        if (this.lastMovement == null) {
            this.lastMovement = data;
            return true;
        }

        this.playerCount = this.playerCount >= countToRemove() ? 0 : this.playerCount + 1;

        return calculateDifference(data) > requiredDifference() && (ConfigManager.QUALITY == ReplayQuality.HIGH || this.playerCount != countToRemove());
    }

    public boolean shouldRecordEntityMovement(PacketData data) {
        if (ConfigManager.QUALITY == ReplayQuality.HIGH) return true;
        boolean isMovingData = data instanceof EntityMovingData;

        if (isMovingData) {
            this.entityMovementCount = this.entityMovementCount >= countToRemove() ? 0 : this.entityMovementCount + 1;
        } else {
            this.velocityCount = this.velocityCount >= countToRemove() ? 0 : this.velocityCount + 1;
        }

        return (this.entityMovementCount != countToRemove() && isMovingData) || (this.velocityCount != countToRemove() && !isMovingData);
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


}
