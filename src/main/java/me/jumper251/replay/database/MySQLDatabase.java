package me.jumper251.replay.database;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.database.utils.AutoReconnector;
import me.jumper251.replay.database.utils.Database;
import me.jumper251.replay.database.utils.DatabaseService;
import me.jumper251.replay.utils.LogUtils;



public class MySQLDatabase extends Database {
	
	private HikariDataSource dataSource;
	private MySQLService service;
	
	public MySQLDatabase(String host, int port, String database, String user, String password, String prefix, int poolSize, int maxLifetime, boolean ssl, boolean certificateverification, long timeout) {
		super(host, port, database, user, password, poolSize, maxLifetime, ssl, certificateverification, timeout);

		this.service = new MySQLService(this, prefix);
//		new AutoReconnector(ReplaySystem.instance);

	}

	@Override
	public String getDataSourceName() {
		return "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&allowMultiQueries=true";
	}

	@Override
	public void connect() {
		HikariConfig hikariConfig = new HikariConfig();

		hikariConfig.setPoolName("AdvancedReplayMySQLPool");

		hikariConfig.setMaximumPoolSize(poolSize);
		hikariConfig.setMaxLifetime(maxLifetime * 1000L);

		hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database+"?autoReconnect=true&allowMultiQueries=true");

		hikariConfig.setUsername(user);
		hikariConfig.setPassword(password);
		hikariConfig.setConnectionTimeout(timeout);

		hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
		if (!certificateverification) {
			hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
		}

//		hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
//		hikariConfig.addDataSourceProperty("encoding", "UTF-8");
//		hikariConfig.addDataSourceProperty("useUnicode", "true");
		hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
		hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
		hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
		hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
		hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
		hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
		hikariConfig.addDataSourceProperty("maintainTimeStats", "false");

		// Recover if connection gets interrupted
//		hikariConfig.addDataSourceProperty("socketTimeout", timeout);

		dataSource = new HikariDataSource(hikariConfig);

		try {
			dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		return;
	}

	@Override
	public void disconnect() {
		try {
			if(dataSource.getConnection() != null) {
				dataSource.close();
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
//			connect();
			System.err.println(e);
		}
	}
	
	public void update(String qry) {
		try {
			Statement st = dataSource.getConnection().createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
//			connect();
			System.err.println(e);
		}
	}

	public ResultSet query(PreparedStatement pst) {
		ResultSet rs = null;

		try {
			rs = pst.executeQuery();
		} catch (SQLException e) {
//			connect();
			System.err.println(e);
		}
		return rs;
	}
	public boolean hasConnection(){
		try{
			return dataSource.getConnection() != null || dataSource.getConnection().isValid(1);
		}catch(SQLException e){
			return false;
		}
	}
	public String getDatabase() {
		return database;
	}

	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public HikariDataSource getDataSource(){
		return this.dataSource;
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
