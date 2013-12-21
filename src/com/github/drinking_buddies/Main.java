package com.github.drinking_buddies;


import com.github.drinking_buddies.config.Configuration;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;
import eu.webtoolkit.jwt.auth.AuthService;
import eu.webtoolkit.jwt.auth.FacebookService;

//Servlet configuration class
public class Main extends WtServlet {
    private static final long serialVersionUID = 1L;
    
    private static FacebookService facebookService;

    public Main() {
        super();
        
        //load the XML configuration
        Configuration conf = Configuration.loadConfiguration();

        //handle Facebook OAuth authentication
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
    
    @Override
    public WApplication createApplication(WEnvironment env) {
        return new Application(env, this.getServletContext());
    }
    
    public static FacebookService getFacebookService() {
        return facebookService;
    }
}
