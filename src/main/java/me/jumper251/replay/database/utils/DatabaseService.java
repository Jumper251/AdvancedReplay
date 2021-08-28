package me.jumper251.replay.database.utils;

import me.jumper251.replay.replaysystem.data.ReplayInfo;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class DatabaseService {

    protected ExecutorService pool;

    public DatabaseService() {
        this.pool = Executors.newCachedThreadPool();
    }

    public abstract void createReplayTable();

    public abstract void addReplay(String id, String creator, int duration, Long time, byte[] data) throws SQLException;

    public abstract byte[] getReplayData(String id);

    public abstract void deleteReplay(String id);

    public abstract boolean exists(String id);

    public abstract List<ReplayInfo> getReplays();

    public ExecutorService getPool() {
        return pool;
    }

}
