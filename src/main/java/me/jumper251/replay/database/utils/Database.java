package me.jumper251.replay.database.utils;

public abstract class Database {

    protected String host;
    protected int port;
    protected String database;
    protected String user;
    protected String password;
    protected int poolSize;
    protected int maxLifetime;
    protected long timeout;
    protected boolean ssl;
    protected boolean certificateverification;
    public Database(String host, int port, String database, String user, String password, int poolSize, int maxLifetime, boolean ssl, boolean certificateverification, long timeout) {
    		this.host = host;
            this.port = port;
    		this.database = database;
    		this.user = user;
    		this.password = password;
    		this.poolSize = poolSize;
            this.maxLifetime = maxLifetime;
            this.ssl = ssl;
            this.certificateverification = certificateverification;
    		connect();
    }
    
    public abstract void connect(); 
    
    public abstract void disconnect();
    
    public abstract DatabaseService getService();

    public abstract String getDataSourceName();
}
