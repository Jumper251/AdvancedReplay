package me.jumper251.replay.replaysystem.recording.optimization;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReplayStats {

	private Map<Object, Long> actions;

	private List<String> players;

	private long entityCount;

	public ReplayStats (Map<Object, Long> actions, List<String> players, long entityCount) {
		this.actions     = actions;
		this.players     = players;
		this.entityCount = entityCount;
	}

	public long getActionCount () {
		return this.actions.values ().stream ().reduce ((long)0, Long::sum);
	}

	public Map<Object, Object> getSortedActions () {

		return this.actions.entrySet ().stream ().sorted ((Map.Entry.<Object, Long>comparingByValue ().reversed ())).collect (Collectors.toMap (Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
	}

	public Map<Object, Long> getActions () {
		return actions;
	}

	public List<String> getPlayers () {
		return players;
	}

	public long getEntityCount () {
		return entityCount;
	}
}
