package me.jumper251.replay.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.jumper251.replay.database.utils.DatabaseService;
import me.jumper251.replay.replaysystem.data.ReplayInfo;

public class MySQLService extends DatabaseService {

	private MySQLDatabase database;

	private final String NAME = "replays";

	public MySQLService (MySQLDatabase database) {
		this.database = database;
	}

	@Override
	public void createReplayTable () {
		database.update ("CREATE TABLE IF NOT EXISTS " + NAME + " (id VARCHAR(40) PRIMARY KEY UNIQUE, creator VARCHAR(30), duration INT(255), time BIGINT(255), data LONGBLOB)");
	}

	@Override
	public void addReplay (String id, String creator, int duration, Long time, byte[] data) throws SQLException {
		PreparedStatement pst = database.getConnection ().prepareStatement ("INSERT INTO " + NAME + " (id, creator, duration, time, data) VALUES (?,?,?,?,?)");
		pst.setString (1, id);
		pst.setString (2, creator);
		pst.setInt (3, duration);
		pst.setLong (4, time);
		pst.setBytes (5, data);

		pool.execute (new Runnable () {
			@Override
			public void run () {
				database.update (pst);
			}
		});
	}

	@Override
	public byte[] getReplayData (String id) {
		try {

			PreparedStatement pst = database.getConnection ().prepareStatement ("SELECT data FROM " + NAME + " WHERE id = ?");
			pst.setString (1, id);

			ResultSet rs = database.query (pst);
			while (rs.next ()) {
				return rs.getBytes (1);
			}

			pst.close ();
		} catch (Exception e) {
			e.printStackTrace ();
		}

		return null;
	}

	@Override
	public void deleteReplay (String id) {
		try {

			PreparedStatement pst = database.getConnection ().prepareStatement ("DELETE FROM " + NAME + " WHERE id = ?");
			pst.setString (1, id);

			pool.execute (new Runnable () {
				@Override
				public void run () {
					database.update (pst);
				}
			});

		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

	@Override
	public boolean exists (String id) {
		try {

			PreparedStatement pst = database.getConnection ().prepareStatement ("SELECT COUNT(1) FROM " + NAME + " WHERE id = ?");
			pst.setString (1, id);

			ResultSet rs = database.query (pst);
			while (rs.next ()) {
				return rs.getInt (1) > 0;
			}

			pst.close ();
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return false;
	}

	@Override
	public List<ReplayInfo> getReplays () {
		List<ReplayInfo> replays = new ArrayList<> ();
		try {

			PreparedStatement pst = database.getConnection ().prepareStatement ("SELECT id,creator,duration,time FROM " + NAME);
			ResultSet rs          = database.query (pst);

			while (rs.next ()) {
				String id      = rs.getString ("id");
				String creator = rs.getString ("creator");
				int duration   = rs.getInt ("duration");
				long time      = rs.getLong ("time");

				replays.add (new ReplayInfo (id, creator, time, duration));
			}

			pst.close ();
		} catch (Exception e) {
			e.printStackTrace ();
		}

		return replays;
	}
}
