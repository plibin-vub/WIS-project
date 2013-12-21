package com.github.drinking_buddies.config;

import java.io.File;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

//this class contains all the configuration settings of our application
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
    
    //we load the Configuration object from an XML file 
    //using XStream
    public static Configuration loadConfiguration() {
        XStream xstream = new XStream(new DomDriver()); 
        xstream.alias("configuration", Configuration.class);
        xstream.alias("database", Database.class);
        xstream.alias("wt", Wt.class);
        return (Configuration) xstream.fromXML(configFile());
    }
    
    private static File configFile() {
        final String fn = "config.xml";
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if(osName.startsWith("windows"))
            return new File("C:\\DrinkingBuddies\\" + fn);
        else
            return new File("/etc/drinking_buddies/" + fn); 
   }
}
