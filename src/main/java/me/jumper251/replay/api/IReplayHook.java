package me.jumper251.replay.api;

import java.util.List;
import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.types.PacketData;
import me.jumper251.replay.replaysystem.replaying.Replayer;

public interface IReplayHook {

	List<PacketData> onRecord (String playerName);

	void onPlay (ActionData data, Replayer replayer);
}
