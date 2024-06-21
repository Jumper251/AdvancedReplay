package me.jumper251.replay.database;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.common.collect.Lists;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.database.utils.AutoReconnector;
import me.jumper251.replay.database.utils.Database;
import me.jumper251.replay.database.utils.DatabaseService;
import me.jumper251.replay.utils.LogUtils;



public class MySQLDatabase extends Database {

	private Connection connection;
	private MySQLService service;
	private List<String> properties;

	public static final List<String> DEFAULT_PROPERTIES = Lists.newArrayList("useSSL=false", "characterEncoding=latin1");
	
	public MySQLDatabase(String host, int port, String database, String user, String password, String prefix, List<String> properties) {
		super(host, port, database, user, password);

		this.service = new MySQLService(this, prefix);
		this.properties = properties;
		new AutoReconnector(ReplaySystem.instance);

		connect();

	}

	@Override
	public String getDataSourceName() {
		if (properties != null && !properties.isEmpty()) {
			return String.format("jdbc:mysql://%s:%d/%s?%s", this.host, this.port, this.database, String.join("&", properties));
		}
		return String.format("jdbc:mysql://%s:%d/%s", this.host, this.port, this.database);
	}

	@Override
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String dsn = this.getDataSourceName();
			this.connection = DriverManager.getConnection(dsn, this.user, this.password);
			LogUtils.log("Successfully conntected to MySQL database");
		} catch (SQLException | ClassNotFoundException e) {
			LogUtils.log("Unable to connect to MySQL database: " + e.getMessage());
		}
	}

	@Override
	public void disconnect() {
		try {
			if(this.connection != null) {
				this.connection.close();
				LogUtils.log("Connection closed");
			}
		} catch (SQLException e) {
			LogUtils.log("Error while closing the connection: " + e.getMessage());

		}

	}

	@Override
	public DatabaseService getService() {
		return this.service;
	}

	public void update(PreparedStatement pst) {
		try {
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}
	
	public void update(String qry) {
		try {
			Statement st = this.connection.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}

	public ResultSet query(PreparedStatement pst) {
		ResultSet rs = null;

		try {
			rs = pst.executeQuery();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
		return rs;
	}
	public boolean hasConnection(){
		try{
			return this.connection != null || this.connection.isValid(1);
		}catch(SQLException e){
			return false;
		}
	}
	public String getDatabase() {
		return database;
	}

	public Connection getConnection() {
		return connection;
	}

	
	public void closeRessources(ResultSet rs , PreparedStatement st){
		if(rs != null){
			try{
				rs.close();
			}catch(SQLException e){

			}
		}
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
