package me.jumper251.replay.replaysystem.recording.optimization;


import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ReplayOptimizer {

    private Map<String, MovingData> lastMovements;

    private Map<String, Integer> playerMoveCounters;

    private Map<Integer, Integer> entityMovementCounters;

    private Map<Integer, Integer> velocityCounters;

    public ReplayOptimizer() {
        this.lastMovements = new ConcurrentHashMap<>();
        this.playerMoveCounters = new ConcurrentHashMap<>();
        this.entityMovementCounters = new ConcurrentHashMap<>();
        this.velocityCounters = new ConcurrentHashMap<>();
    }

    public static ReplayStats analyzeReplay(Replay replay) {
        HashMap<Integer, List<ActionData>> data = replay.getData().getActions();

        List<ActionData> merged = new ArrayList<>();
        data.values().forEach(merged::addAll);

        long entityCount = merged.stream()
                .filter(action -> action.getPacketData() instanceof EntityData && ((EntityData) action.getPacketData()).getAction() == 0)
                .count();

        List<String> players = merged.stream()
                .map(ActionData::getName)
                .distinct()
                .collect(Collectors.toList());

        Map<Object, Long> actions = merged.stream()
                .filter(action -> action.getPacketData() != null)
                .map(action -> action.getPacketData().getClass().getSimpleName())
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        return new ReplayStats(actions, players, entityCount);

    }

    public boolean shouldRecord(String playerName, PacketData data) {
        if (data instanceof MovingData) {
            return shouldRecordPlayerMovement(playerName, (MovingData) data);
        } else if (data instanceof EntityMovingData || data instanceof VelocityData) {
            return shouldRecordEntityMovement(data);
        } else {
            return true;
        }
    }

    public boolean shouldRecordPlayerMovement(String playerName, MovingData data) {
        MovingData lastMovement = this.lastMovements.get(playerName);
        if (lastMovement == null) {
            this.lastMovements.put(playerName, data);
            return true;
        }

        int playerCount = this.playerMoveCounters.compute(playerName,
                (key, count) -> (count == null || count >= countToRemove()) ? 0 : count + 1);

        boolean shouldRecord = calculateDifference(data, lastMovement) > requiredDifference()
                && (ConfigManager.QUALITY == ReplayQuality.HIGH || playerCount != countToRemove());

        if (shouldRecord) {
            this.lastMovements.put(playerName, data);
        }

        return shouldRecord;
    }

    public boolean shouldRecordEntityMovement(PacketData data) {
        if (ConfigManager.QUALITY == ReplayQuality.HIGH) return true;
        boolean isMovingData = data instanceof EntityMovingData;

        if (isMovingData) {
            EntityMovingData movingData = (EntityMovingData) data;
            int entityMovementCount = this.entityMovementCounters.compute(movingData.getId(),
                    (key, count) -> (count == null || count >= countToRemove()) ? 0 : count + 1);

            return entityMovementCount != countToRemove();
        } else {
            VelocityData velocityData = (VelocityData) data;
            int velocityCount = this.velocityCounters.compute(velocityData.getId(),
                    (key, count) -> (count == null || count >= countToRemove()) ? 0 : count + 1);

            return velocityCount != countToRemove();
        }

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

    public double calculateDifference(MovingData data, MovingData lastMovement) {
        double locationDiff = Math.abs(data.getX() - lastMovement.getX()) + Math.abs(data.getY() - lastMovement.getY()) + Math.abs(data.getZ() - lastMovement.getZ());
        double rotationDiff = Math.abs(data.getYaw() - lastMovement.getYaw()) + Math.abs(data.getPitch() - lastMovement.getPitch());

        return locationDiff + rotationDiff;
    }


}
