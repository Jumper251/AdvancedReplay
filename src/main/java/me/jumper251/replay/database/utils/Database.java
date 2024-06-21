package me.jumper251.replay.database.utils;

public abstract class Database {

    protected String host;
    protected int port;
    protected String database;
    protected String user;
    protected String password;
    
    public Database(String host, int port, String database, String user, String password) {
    		this.host = host;
            this.port = port;
    		this.database = database;
    		this.user = user;
    		this.password = password;
    }
    
    public abstract void connect(); 
    
    public abstract void disconnect();
    
    public abstract DatabaseService getService();

    public abstract String getDataSourceName();
}
