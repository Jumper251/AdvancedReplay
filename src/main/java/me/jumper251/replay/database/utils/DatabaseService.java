package me.jumper251.replay.database.utils;

import java.sql.SQLException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import me.jumper251.replay.dev.mrflyn.extended.worldmanagers.RawWorld;
import me.jumper251.replay.replaysystem.data.ReplayInfo;



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

	public abstract boolean hasWorld(String hashcode);

	public abstract List<ReplayInfo> getReplays();

	public abstract void setWorld(String hashcode, String name, byte[] data, String type);

	public abstract RawWorld getWorld(String hashcode);

	public abstract RawWorld getWorldFromName(String name);

	public ExecutorService getPool() {
		return pool;
	}
}
