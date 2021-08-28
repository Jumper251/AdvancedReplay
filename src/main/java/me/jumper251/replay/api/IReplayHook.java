package me.jumper251.replay.api;

import me.jumper251.replay.replaysystem.data.ActionData;
import me.jumper251.replay.replaysystem.data.types.PacketData;
import me.jumper251.replay.replaysystem.replaying.Replayer;

import java.util.List;

public interface IReplayHook {

    List<PacketData> onRecord(String playerName);

    void onPlay(ActionData data, Replayer replayer);
}
