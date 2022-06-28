package me.jumper251.replay.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.database.utils.DatabaseService;
import me.jumper251.replay.dev.mrflyn.extended.worldmanagers.RawWorld;
import me.jumper251.replay.replaysystem.data.ReplayInfo;


public class MySQLService extends DatabaseService {

	private MySQLDatabase database;
	
	private String parentTable = "replays";
	private String worldTable = "_worlds";
	

	public MySQLService(MySQLDatabase database, String prefix) {
		if (prefix != null && prefix.length() > 0) {
			this.parentTable = prefix + this.parentTable;
			this.worldTable= parentTable+worldTable;
		}

		this.database = database;
	}
	
	@Override
	public void createReplayTable() {
		try(PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + this.parentTable + " (id VARCHAR(40) PRIMARY KEY UNIQUE, creator VARCHAR(30), duration INT(255), time BIGINT(255), data LONGBLOB);")){
			pst.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
		try(PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + this.worldTable
				+ " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, hashcode VARCHAR(200), world_name VARCHAR(200), data LONGBLOB, type VARCHAR(200));"))
		{
			pst.executeUpdate();
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void addReplay(String id, String creator, int duration, Long time, byte[] data) throws SQLException {
		try(PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("INSERT INTO " + this.parentTable + " (id, creator, duration, time, data) VALUES (?,?,?,?,?)")) {
			pst.setString(1, id);
			pst.setString(2, creator);
			pst.setInt(3, duration);
			pst.setLong(4, time);
			pst.setBytes(5, data);
			pst.executeUpdate();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] getReplayData(String id) {
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("SELECT data FROM " + this.parentTable + " WHERE id = ?"))
		{
			pst.setString(1, id);
			ResultSet rs = pst.executeQuery();
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
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("DELETE FROM " + this.parentTable + " WHERE id = ?")){
			pst.setString(1, id);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean exists(String id) {
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("SELECT COUNT(1) FROM " + this.parentTable + " WHERE id = ?"))
			{
			pst.setString(1, id);
			ResultSet rs = pst.executeQuery();
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
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("SELECT id,creator,duration,time FROM " + this.parentTable))
		{
			ResultSet rs = pst.executeQuery();
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

	@Override
	public boolean hasWorld(String hashcode){
		ReplaySystem.getInstance().getLogger().info("hasWorld: "+hashcode);
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("SELECT hashcode FROM " + this.worldTable+" WHERE hashcode=?;"))
		{
			pst.setString(1, hashcode);
			ResultSet rs = pst.executeQuery();
			if (rs.next()){
				ReplaySystem.getInstance().getLogger().info("hasWorld: "+hashcode+ " Done!");
				return rs.getString("hashcode").equals(hashcode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public RawWorld getWorld(String hashcode) {
		ReplaySystem.getInstance().getLogger().info("getWorld: "+hashcode);
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("SELECT * FROM " + this.worldTable + " WHERE hashcode=?;"))
		{
			pst.setString(1, hashcode);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				ReplaySystem.getInstance().getLogger().info("getWorld: "+hashcode+ " Done!");
				return new RawWorld(rs.getString("world_name"), hashcode ,rs.getBytes("data"), rs.getString("type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public RawWorld getWorldFromName(String name) {
		try (PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("SELECT * FROM " + this.worldTable + " WHERE world_name=?;"))
		{
			pst.setString(1, name);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				new RawWorld(name, rs.getString("hashcode") ,rs.getBytes("data"), rs.getString("type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setWorld(String hashcode, String name, byte[] data, String type) {
		ReplaySystem.getInstance().getLogger().info("setWorld: "+hashcode+ " "+name);
		try(PreparedStatement pst = database.getDataSource().getConnection().prepareStatement("INSERT INTO " + this.worldTable + " (hashcode, world_name, data, type) VALUES (?, ?, ?, ?);"))
		{
			pst.setString(1, hashcode);
			pst.setString(2, name);
			pst.setBytes(3, data);
			pst.setString(4, type);
			pst.executeUpdate();
			ReplaySystem.getInstance().getLogger().info("setWorld: "+hashcode+ " "+name+ " Done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
