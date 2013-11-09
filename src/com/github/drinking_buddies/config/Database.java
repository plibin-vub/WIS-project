package com.github.drinking_buddies.config;

public class Database {
    private String jdbcUrl;
    private String userName;
    private String password;
    
    public Database(String jdbcUrl, String userName, String password) {
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
}
