package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.math.BigDecimal;
import java.sql.Connection;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.geolocation.GeoLocation;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.google.Geocoding;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDoubleSpinBox;
import eu.webtoolkit.jwt.WGoogleMap;
import eu.webtoolkit.jwt.WGoogleMap.Coordinate;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class NearbyBarsForm extends WContainerWidget{

    
    public NearbyBarsForm() {
        // the main template for the user form
        // (a WTemplate constructor accepts the template text and its parent)
        WTemplate main = new WTemplate(tr("nearby-bars-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        // we bind to some of the template's variables
        double lat=50.921446;
        double lng=4.691416;
        String location=null;
        try {
            location = Geocoding.locationToAddress(lat, lng);
        } catch (RestException e) {
        }
        final WLineEdit edit = new WLineEdit();
        edit.setPlaceholderText("Enter location");
        if(location!=null){
            edit.setText(location);
        }
        main.bindWidget("location",edit);
        final WDoubleSpinBox radius = new WDoubleSpinBox();
        radius.setRange(0.1, 1000);
        radius.setValue(0.5);
        radius.setSingleStep(0.5);
        radius.setSuffix(" km");
        main.bindWidget("radius", radius);
        final WGoogleMap map = new WGoogleMap(WGoogleMap.ApiVersion.Version3);
        main.bindWidget("map",map);
        map.setHeight(new WLength(400));
        setMapCenter(map, lat, lng, radius.getValue());
        WPushButton search = new WPushButton(tr("nearby-bars-form.search"));
        main.bindWidget("search", search);
        search.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                findBars();
            }

            private void findBars() {
                String location=edit.getText();
                com.github.drinking_buddies.webservices.google.Geocoding.GoogleGeoCodeResponse.location coordinates;
                try {
                    coordinates = Geocoding.addressToLocation(location);
                } catch (RestException e) {
                    giveError();
                    return;
                }
                querryBars(Double.valueOf(coordinates.lat), Double.valueOf(coordinates.lng),radius.getValue());
                setMapCenter(map,  Double.valueOf(coordinates.lat), Double.valueOf(coordinates.lng), radius.getValue());
            }


            private void giveError() {
                // TODO Auto-generated method stub
                
            }   
        });
       
        
       
    }
    
    private void querryBars(double lat,double lng,double radius) {
        GeoLocation location=GeoLocation.fromDegrees(lat, lng);
        GeoLocation[] boundingCoordinates=location.boundingCoordinates(radius, GeoLocation.RADIUS_EARTH);
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = app.createDSLContext(conn);
//            "SELECT * FROM Places WHERE (Lat >= ? AND Lat <= ?) AND (Lon >= ? " +
//            (meridian180WithinDistance ? "OR" : "AND") + " Lon <= ?) AND " +
//            "acos(sin(?) * sin(Lat) + cos(?) * cos(Lat) * cos(Lon - ?)) <= ?");
            Condition condition1=BAR.LOCATION_X.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).and(BAR.LOCATION_X.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            Condition condition2;
            if(boundingCoordinates[0].getLongitudeInRadians() >
            boundingCoordinates[1].getLongitudeInRadians()){
                condition2=BAR.LOCATION_Y.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).or(BAR.LOCATION_Y.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            }else{
                condition2=BAR.LOCATION_Y.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).and(BAR.LOCATION_Y.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            }
            Condition condition3=BAR.LOCATION_X.sin().multiply(Math.sin(location.getLatitudeInRadians()))
                    .plus(BAR.LOCATION_X.cos().multiply(Math.cos(location.getLatitudeInRadians())).
                            multiply(BAR.LOCATION_Y.subtract(location.getLongitudeInRadians()).cos())).le(new BigDecimal(Math.cos(radius / GeoLocation.RADIUS_EARTH)));
            Result r=dsl.select(BAR.NAME,BAR.LOCATION_X,BAR.LOCATION_Y)
                    .from(BAR)
                    .where(condition1.and(condition2).and(condition3))
                    .fetch();
            System.out.println(dsl.toString());

           
            
        } catch (Exception e) {
            app.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
        
    }

    private void setMapCenter(WGoogleMap map,double lat,double lng, double radius) {
        if(lat!=0 && lng!=0){
            GeoLocation geoLocation=GeoLocation.fromDegrees(lat, lng);
            GeoLocation[] boundingBox = geoLocation.boundingCoordinates(radius, GeoLocation.RADIUS_EARTH);
            Coordinate center=new Coordinate(lat, lng);
            map.setCenter(center);
            Coordinate rightBottom=new Coordinate(boundingBox[1].getLatitudeInDegrees(),boundingBox[1].getLongitudeInDegrees());
            Coordinate topLeft=new Coordinate(boundingBox[0].getLatitudeInDegrees(),boundingBox[0].getLongitudeInDegrees());;
            map.zoomWindow(topLeft, rightBottom);
        }
        
    }
    
    
    
}