package com.github.drinking_buddies.config;

public class Configuration {
    private Database database;
    private Wt wt;

    public Configuration(String locale, Database database, Wt wt) {
        this.database = database;
        this.wt = wt;
    }

    public Database getDatabase() {
        return database;
    }
    
    public Wt getWt() {
        return wt;
    }
}
