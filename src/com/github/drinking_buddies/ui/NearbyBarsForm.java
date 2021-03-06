package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BEER2_BAR;
import static com.github.drinking_buddies.jooq.Tables.BEER;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record10;
import org.jooq.Record9;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.geolocation.GeoLocation;
import com.github.drinking_buddies.jooq.utils.SearchUtils;
import com.github.drinking_buddies.jwt.DBGoogleMap;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.google.Geocoding;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Icon;
import eu.webtoolkit.jwt.JSignal;
import eu.webtoolkit.jwt.JSignal1;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.StandardButton;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDoubleSpinBox;
import eu.webtoolkit.jwt.WGoogleMap.ApiVersion;
import eu.webtoolkit.jwt.WGoogleMap.Coordinate;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMessageBox;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

//This form allows the user to search for nearby bars .These bars are shown on a map.
public class NearbyBarsForm extends WContainerWidget{

    private WContainerWidget ResultsContainer = new WContainerWidget();
    Application app = Application.getInstance();
    
    
    public NearbyBarsForm(String lat, String len,String beerName) {
        final WTemplate main = new WTemplate(tr("nearby-bars-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        // we bind to some of the template's variables
        
        String location=null;
        try {
            location = Geocoding.locationToAddress(lat, len);
        } catch (RestException e) {
        }
        final WLineEdit edit = new WLineEdit();
        edit.setPlaceholderText("Enter location");
        edit.setId("location");
        
        if(location!=null){
            edit.setText(location);
        }
        
        main.bindWidget("location",edit);
        final WDoubleSpinBox radius = new WDoubleSpinBox();
        radius.setRange(0.1, 1000);
        radius.setValue(0.5);
        radius.setSingleStep(0.5);
        radius.setSuffix(" km");
        main.bindString("warning", "");
        main.bindWidget("radius", radius);
        final DBGoogleMap map = new DBGoogleMap(ApiVersion.Version3);
        main.bindWidget("map",map);
        map.setHeight(new WLength(400));
        map.setWidth(new WLength(400));
        setMapCenter(map, Double.parseDouble(lat), Double.parseDouble(len), radius.getValue());
        WPushButton search = new WPushButton(tr("nearby-bars-form.search"));
        main.bindWidget("search", search);
        main.bindWidget("results", ResultsContainer);
        final WLineEdit beerSearch = new WLineEdit();
        if(beerName!=null){
            beerSearch.setText(beerName);
        }
        main.bindWidget("beer", beerSearch);
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
                if(coordinates==null){
                    giveError();
                    return;
                }
                map.clearOverlays();
                ResultsContainer.clear();
                Integer beerId = null;
                try {
                    if(beerSearch.getText()!=null)
                    beerId = SearchUtils.getBeerId(beerSearch.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(beerId==null && !"".equals(beerSearch.getText())){
                    main.bindString("warning", tr("nearby-bars-form.beer-not-found"));
                }else{
                    main.bindString("warning", "");
                }
                querryBars(map ,Double.valueOf(coordinates.lat), Double.valueOf(coordinates.lng),radius.getValue(),beerId);
            }

            //Show an error dialog
            private void giveError() {
                final WMessageBox messageBox = new WMessageBox(
                        "Error",
                        tr("nearby-bars-form.location-not-found"),
                        Icon.Warning, EnumSet.of(StandardButton.Ok));
                messageBox.setModal(false);
                messageBox.buttonClicked().addListener(main,
                        new Signal1.Listener<StandardButton>() {
                            @Override
                            public void trigger(StandardButton arg) {
                                if (messageBox != null)
                                    messageBox.remove();
                                
                            }
                        });
                messageBox.show();
                
            }   
        });
        
        
        
       
    }
    
    
  //This method will query for all the bars within the radius. That have the beer available.
    private void querryBars(DBGoogleMap map,double lat,double lng,double radius, Integer BeerId) {        
        GeoLocation location=GeoLocation.fromDegrees(lat, lng);
        //Calculate the bounding coordinates(the coordinates of the corners of the bounding box around the circle we want to search)
        GeoLocation[] boundingCoordinates=location.boundingCoordinates(radius, GeoLocation.RADIUS_EARTH);
        Coordinate center=new Coordinate(lat, lng);
        map.setCenter(center);
        Coordinate rightBottom=new Coordinate(boundingCoordinates[1].getLatitudeInDegrees(),boundingCoordinates[1].getLongitudeInDegrees());
        Coordinate topLeft=new Coordinate(boundingCoordinates[0].getLatitudeInDegrees(),boundingCoordinates[0].getLongitudeInDegrees());;
        map.zoomWindow(topLeft, rightBottom);
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            //Condition 1 and 2 check if the location is within the bounding box for this part of the query an index can be used
            Condition condition1=BAR.LOCATION_X.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).and(BAR.LOCATION_X.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            Condition condition2;
            if(boundingCoordinates[0].getLongitudeInRadians() >
            boundingCoordinates[1].getLongitudeInRadians()){
                condition2=BAR.LOCATION_Y.ge(new Float(boundingCoordinates[0].getLatitudeInRadians())).or(BAR.LOCATION_Y.le(new Float(boundingCoordinates[1].getLatitudeInRadians())));
            }else{
                condition2=BAR.LOCATION_Y.ge(new Float(boundingCoordinates[0].getLongitudeInRadians())).and(BAR.LOCATION_Y.le(new Float(boundingCoordinates[1].getLongitudeInRadians())));
            }
            //Condition 3 checks if the location is within the circle we want to query.
            Condition condition3=BAR.LOCATION_X.sin().multiply(Math.sin(location.getLatitudeInRadians()))
                    .plus(BAR.LOCATION_X.cos().multiply(Math.cos(location.getLatitudeInRadians())).
                            multiply(BAR.LOCATION_Y.subtract(location.getLongitudeInRadians()).cos())).le(new BigDecimal(Math.cos(radius / GeoLocation.RADIUS_EARTH)));
            Result<Record9<String, String, Float, Float, String, String, String, String, String>> r;
            if(BeerId!=null){
                //the query where we add conditions 1,2 and 3 and a condition that the beer is available
                r=dsl.select(BAR.NAME,BAR.URL,BAR.LOCATION_X,BAR.LOCATION_Y,ADDRESS.STREET,ADDRESS.NUMBER,ADDRESS.ZIPCODE,ADDRESS.CITY,ADDRESS.COUNTRY)
                        .from(BAR).join(ADDRESS).on(BAR.ADDRESS_ID.eq(ADDRESS.ID)).join(BEER2_BAR).on(BEER2_BAR.BAR_ID.eq(BAR.ID))
                        .where(condition1.and(condition2).and(condition3).and(BEER2_BAR.BEER_ID.eq(BeerId)))
                        .fetch();
            }
            else{
                r=dsl.select(BAR.NAME,BAR.URL,BAR.LOCATION_X,BAR.LOCATION_Y,ADDRESS.STREET,ADDRESS.NUMBER,ADDRESS.ZIPCODE,ADDRESS.CITY,ADDRESS.COUNTRY)
                        .from(BAR).join(ADDRESS).on(BAR.ADDRESS_ID.eq(ADDRESS.ID))
                        .where(condition1.and(condition2).and(condition3))
                        .fetch();
            }
            //Add the results to the page and show the markers
            for (Record record : r) {
                GeoLocation loc = GeoLocation.fromRadians(record.getValue(BAR.LOCATION_X),record.getValue(BAR.LOCATION_Y));
                addBarToMap(map,loc.getLatitudeInDegrees(),loc.getLongitudeInDegrees(),record.getValue(BAR.NAME));
                Address address=new Address(0, record.getValue(ADDRESS.STREET),  record.getValue(ADDRESS.NUMBER),  record.getValue(ADDRESS.ZIPCODE),  record.getValue(ADDRESS.CITY),  record.getValue(ADDRESS.COUNTRY));
                Bar bar = new Bar(0,record.getValue(BAR.NAME),0,0,null,null,address,record.getValue(BAR.URL));
                new BarResultWidget(bar, ResultsContainer);
            }
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
        
    }

    //Add a marker to the map
    private void addBarToMap(DBGoogleMap map, double lat,
            double lng,String barName) {
       map.addMarker(new Coordinate(lat, lng), "<b>"+barName+"</b>");
      
    }

    
    //center the map and set the correct zoom.
    private void setMapCenter(DBGoogleMap map,double lat,double lng, double radius) {
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