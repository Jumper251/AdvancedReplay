package me.jumper251.replay.filesystem.saving;

import me.jumper251.replay.replaysystem.Replay;

public interface IReplaySaver {

	void saveReplay(Replay replay);
	
	Replay loadReplay(String replayName);
	
	boolean replayExists(String replayName);
	
	void deleteReplay(String replayName);
}
