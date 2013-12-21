package com.github.drinking_buddies.config;

import java.util.Properties;

//class that represents the JWt configuration (which is defined in an XML config file)
public class Wt {
    private String locale;
    private Properties authProperties;
    
    public Wt() {
        
    }
    
    //OAuth properties
    public Properties getAuthProperties() {
        return authProperties;
    }
}
