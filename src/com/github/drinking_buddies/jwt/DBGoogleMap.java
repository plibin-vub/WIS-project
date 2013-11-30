package com.github.drinking_buddies.jwt;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WGoogleMap;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WWebWidget;

public class DBGoogleMap extends WGoogleMap {
    public DBGoogleMap() {
        super();
    }

    public DBGoogleMap(ApiVersion version, WContainerWidget parent) {
        super(version, parent);
    }

    public DBGoogleMap(ApiVersion version) {
        super(version);
    }

    public DBGoogleMap(WContainerWidget parent) {
        super(parent);
    }

    /**
     * Based on the original JWt code Adds a marker overlay to the map WITH a
     * popup text (that appears when clicked on the marker).
     */
    public void addMarker(WGoogleMap.Coordinate pos, CharSequence html) {
        String strm = null;

        String infoWindowJS = openInfoWindowJS(pos, html);
        
        if (getApiVersion() == ApiVersion.Version2) {
            throw new RuntimeException("Unsupported action");
        } else {
            strm = "var position = new google.maps.LatLng(" + pos.getLatitude()
                    + ", " + pos.getLongitude() + ");"
                    + "var marker = new google.maps.Marker({"
                    + "position: position," + "map: " + getJsRef() + ".map"
                    + "});" + getJsRef() + ".map.overlays.push(marker);";
            strm += 
                    "google.maps.event.addListener(marker, 'click', (function () {"
                    + infoWindowJS
                    + "}));";
        }
        
        doGmJavaScript(strm);
    }

    private String openInfoWindowJS(Coordinate pos, CharSequence myHtml) {
        String strm;
        strm = "var pos = new google.maps.LatLng(" + pos.getLatitude() + ", "
                + pos.getLongitude() + ");";

        if (getApiVersion() == ApiVersion.Version2) {
            strm += getJsRef() + ".map.openInfoWindow(pos, "
                    + WWebWidget.jsStringLiteral(myHtml.toString()) + ");";
        } else {
            strm += "var infowindow = new google.maps.InfoWindow({"
                    + "content: "
                    + WWebWidget.jsStringLiteral(myHtml.toString()) + ","
                    + "position: pos" + "});" + "infowindow.open(" + getJsRef()
                    + ".map);" + getJsRef()
                    + ".map.infowindows.push(infowindow);";
        }

        return strm;
    }
}
