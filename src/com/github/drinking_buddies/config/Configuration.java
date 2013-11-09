package com.github.drinking_buddies.config;

public class Configuration {
    private Database database;

    public Configuration(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
