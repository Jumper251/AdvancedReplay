package me.jumper251.replay.filesystem.saving;


import me.jumper251.replay.database.DatabaseRegistry;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.replaysystem.data.ReplayInfo;
import me.jumper251.replay.utils.fetcher.Acceptor;
import me.jumper251.replay.utils.fetcher.Consumer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DatabaseReplaySaver implements IReplaySaver {

    public static Map<String, ReplayInfo> replayCache = new HashMap<>();

    public static ReplayInfo getInfo(String replay) {
        if (replayCache != null && replayCache.containsKey(replay)) return replayCache.get(replay);

        return null;
    }

    @Override
    public void saveReplay(Replay replay) {
        try {
            byte[] data;
            try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                 GZIPOutputStream gOut = new GZIPOutputStream(byteOut);
                 ObjectOutputStream objectOut = new ObjectOutputStream(gOut)) {

                objectOut.writeObject(replay.getData());
                objectOut.flush();
                gOut.finish();

                data = byteOut.toByteArray();
            }

            if (replay.getReplayInfo() == null)
                replay.setReplayInfo(new ReplayInfo(replay.getId(), null, System.currentTimeMillis(), replay.getData().getDuration()));
            DatabaseRegistry.getDatabase().getService().addReplay(replay.getId(), replay.getReplayInfo().getCreator(), replay.getReplayInfo().getDuration(), replay.getReplayInfo().getTime(), data);

            updateCache(replay.getId(), replay.getReplayInfo());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadReplay(String replayName, Consumer<Replay> consumer) {
        DatabaseRegistry.getDatabase().getService().getPool().execute(new Acceptor<Replay>(consumer) {

            @Override
            public Replay getValue() {
                byte[] data = DatabaseRegistry.getDatabase().getService().getReplayData(replayName);

                try (ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
                     GZIPInputStream gIn = new GZIPInputStream(byteIn);
                     ObjectInputStream objectIn = new ObjectInputStream(gIn)) {

                    ReplayData replayData = (ReplayData) objectIn.readObject();

                    return new Replay(replayName, replayData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        });

    }

    @Override
    public boolean replayExists(String replayName) {
        return replayCache.containsKey(replayName);
    }

    @Override
    public void deleteReplay(String replayName) {
        DatabaseRegistry.getDatabase().getService().deleteReplay(replayName);

        updateCache(replayName, null);
    }

    @Override
    public List<String> getReplays() {
        return new ArrayList<>(replayCache.keySet());
    }

    private void updateCache(String id, ReplayInfo info) {
        if (info != null && id != null) {
            replayCache.put(id, info);
        } else if (replayCache.containsKey(id)) {
            replayCache.remove(id);
        }
    }
}
