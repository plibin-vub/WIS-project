package com.github.drinking_buddies.jwt;

import eu.webtoolkit.jwt.JSignal2;
import eu.webtoolkit.jwt.JSlot;
import eu.webtoolkit.jwt.WInteractWidget;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.utils.JarUtils;

//This Handler listens to the clicked signal of an WInteractWidget,
//when this clicked signal is triggered, it will notify the browser 
//that it should ask the user to share it's location (using HTML5 location sharing JavaScript).
//The JavaScript to do this, can be found in /com/github/drinking_buddies/jwt/share-location-handler.js 
//When the browser executes this location sharing it will send the obtained coordinate (longitude and latitude)
//to the JWt server using JWt's JSignal infrastructure. 

public class ShareLocationHandler {
    private static String getGeoLocationJS;
    static {
            getGeoLocationJS = JarUtils.getInstance().readTextFromJar("/com/github/drinking_buddies/jwt/share-location-handler.js");
    }
    
    public ShareLocationHandler(WObject parent, WInteractWidget clickWidget) {
        this.locationShared = new JSignal2<String, String>(parent, "location") {
        };
        
        slot.setJavaScript(getGeoLocationJS.replace("${wt-call}", locationShared.createCall("lat", "lon")));
        
        clickWidget.clicked().addListener(slot);
    }
    
    private JSlot slot = new JSlot();
    
    private JSignal2<String, String> locationShared; 
    
    public JSignal2<String, String> locationShared() {
        return locationShared;
    }
}
