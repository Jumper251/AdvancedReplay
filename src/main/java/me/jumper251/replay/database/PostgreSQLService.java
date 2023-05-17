package me.jumper251.replay.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.jumper251.replay.database.utils.DatabaseService;
import me.jumper251.replay.replaysystem.data.ReplayInfo;


public class PostgreSQLService extends DatabaseService {

	private PostgreSQLDatabase database;
	
	private String table = "replays";
	
	public PostgreSQLService(PostgreSQLDatabase database, String prefix) {
		if (prefix != null && prefix.length() > 0) {
			this.table = prefix + this.table;
		}
		
		this.database = database;
	}
	
	@Override
	public void createReplayTable() {
		database.update("CREATE TABLE IF NOT EXISTS " + this.table + " (id VARCHAR(40) PRIMARY KEY UNIQUE, creator VARCHAR(30), duration INT(255), time BIGINT(255), data LONGBLOB)");		
		
	}

	@Override
	public void addReplay(String id, String creator, int duration, Long time, byte[] data) throws SQLException {
		PreparedStatement pst = database.getConnection().prepareStatement("INSERT INTO " + this.table + " (id, creator, duration, time, data) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE creator = ?, duration = ?, time = ?, data = ?");
		pst.setString(1, id);
		pst.setString(2, creator);
		pst.setInt(3, duration);
		pst.setLong(4, time);
		pst.setBytes(5, data);

		pst.setString(6, creator);
		pst.setInt(7, duration);
		pst.setLong(8, time);
		pst.setBytes(9, data);
		
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				database.update(pst);
				
			}
		});
		
	}

	@Override
	public byte[] getReplayData(String id) {
		try {
			
			PreparedStatement pst = database.getConnection().prepareStatement("SELECT data FROM " + this.table + " WHERE id = ?");
			pst.setString(1, id);
			
			ResultSet rs = database.query(pst);
			while (rs.next()) {
				return rs.getBytes(1);
			}
			
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null; 
	}
	

	@Override
	public void deleteReplay(String id) {
		try {
			
			PreparedStatement pst = database.getConnection().prepareStatement("DELETE FROM " + this.table + " WHERE id = ?");
			pst.setString(1, id);
			
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					database.update(pst);
					
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean exists(String id) {
		try {
			
			PreparedStatement pst = database.getConnection().prepareStatement("SELECT COUNT(1) FROM " + this.table + " WHERE id = ?");
			pst.setString(1, id);
			
			ResultSet rs = database.query(pst);
			while (rs.next()) {
				return rs.getInt(1) > 0;
			}
			
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<ReplayInfo> getReplays() {
		List<ReplayInfo> replays = new ArrayList<>();
		try {
			
			PreparedStatement pst = database.getConnection().prepareStatement("SELECT id,creator,duration,time FROM " + this.table);
			ResultSet rs = database.query(pst);
			
			while (rs.next()) {
				String id = rs.getString("id");
				String creator = rs.getString("creator");
				int duration = rs.getInt("duration");
				long time = rs.getLong("time");
				
				replays.add(new ReplayInfo(id, creator, time, duration));
			}
			
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return replays;
	}

}
