package com.github.drinking_buddies.config;

import java.util.Properties;

public class Wt {
    private String locale;
    private Properties authProperties;
    
    public Wt() {
        
    }
    
    public Properties getAuthProperties() {
        return authProperties;
    }

    public String getLocale() {
        return locale;
    }
}
