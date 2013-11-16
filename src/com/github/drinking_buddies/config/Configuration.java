package com.github.drinking_buddies.config;

public class Configuration {
    private Database database;
    private Wt wt;

    public Configuration(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
    
    public Wt getWt() {
        return wt;
    }
}
