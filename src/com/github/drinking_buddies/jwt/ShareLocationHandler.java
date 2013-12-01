package com.github.drinking_buddies.jwt;

import eu.webtoolkit.jwt.JSignal2;
import eu.webtoolkit.jwt.JSlot;
import eu.webtoolkit.jwt.WInteractWidget;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.utils.JarUtils;

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
