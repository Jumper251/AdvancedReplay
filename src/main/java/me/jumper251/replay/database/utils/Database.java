package me.jumper251.replay.database.utils;

public abstract class Database {

    protected String host;
    protected String database;
    protected String user;
    protected String password;

    public Database(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;

        connect();
    }

    public abstract void connect();

    public abstract void disconnect();

    public abstract DatabaseService getService();
}
