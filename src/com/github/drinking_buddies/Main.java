package com.github.drinking_buddies;


import java.io.File;

import com.github.drinking_buddies.config.Configuration;
import com.github.drinking_buddies.config.Database;
import com.github.drinking_buddies.config.Wt;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;
import eu.webtoolkit.jwt.auth.AuthService;
import eu.webtoolkit.jwt.auth.FacebookService;
public class Main extends WtServlet {
    private static final long serialVersionUID = 1L;
    
    private static FacebookService facebookService;

    public Main() {
        super();
        
        Configuration conf = loadConfiguration();

        {
            eu.webtoolkit.jwt.Configuration c = new eu.webtoolkit.jwt.Configuration();
            final String endPoint = "facebook-oauth2-redirect-endpoint";
            c.getProperties().put(endPoint, conf.getWt().getAuthProperties().get(endPoint).toString());
            final String appId = "facebook-oauth2-app-id";
            c.getProperties().put(appId, conf.getWt().getAuthProperties().get(appId).toString());
            final String appSecret = "facebook-oauth2-app-secret";
            c.getProperties().put(appSecret, conf.getWt().getAuthProperties().get(appSecret).toString());
            this.setConfiguration(c);
        }
        
        if (!FacebookService.configured())
            throw new RuntimeException("The Facebook OAuth service was not correctly configured.");

        if (facebookService == null) 
            facebookService = new FacebookService(new AuthService());
    }
    
    public static Configuration loadConfiguration() {
        XStream xstream = new XStream(new DomDriver()); 
        xstream.alias("configuration", Configuration.class);
        xstream.alias("database", Database.class);
        xstream.alias("wt", Wt.class);
        //TODO: allow for the configuration of this file location
        return (Configuration) xstream.fromXML(new File("./drinking-buddies-config.xml"));
    }

    @Override
    public WApplication createApplication(WEnvironment env) {
        return new Application(env, this.getServletContext());
    }
    
    public static FacebookService getFacebookService() {
        return facebookService;
    }
}
