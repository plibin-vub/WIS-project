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
        //TODO: allow for the configuration of this file location
        return (Configuration) xstream.fromXML(new File("./drinking-buddies-config.xml"));
    }
}
