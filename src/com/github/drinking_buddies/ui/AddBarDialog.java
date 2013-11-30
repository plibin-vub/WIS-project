package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.geolocation.GeoLocation;
import com.github.drinking_buddies.jooq.tables.records.AddressRecord;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.google.Geocoding;
import com.github.drinking_buddies.webservices.google.Geocoding.GoogleGeoCodeResponse.location;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class AddBarDialog extends WDialog {
    public AddBarDialog(WObject parent) {
        super(tr("new-bar-form.title").arg("Add new Bar"), parent);
        final WTemplate main = new WTemplate(tr("new-bar-form"), this.getContents());
        TemplateUtils.configureDefault(Application.getInstance(), main);
        // we bind to some of the template's variables
        final WLineEdit name = new WLineEdit();
        main.bindWidget("bar", name);
        name.setId("inputName");
        final WLineEdit website = new WLineEdit();
        main.bindWidget("website", website);
        website.setId("inputWebsite");
        final WLineEdit street = new WLineEdit();
        main.bindWidget("street", street);
        final WLineEdit number = new WLineEdit();
        main.bindWidget("number", number);
        final WLineEdit zipcode = new WLineEdit();
        main.bindWidget("zipcode", zipcode);
        final WLineEdit city = new WLineEdit();
        main.bindWidget("city", city);
        final WLineEdit country = new WLineEdit();
        main.bindWidget("country", country);
        final WLineEdit score = new WLineEdit();
        main.bindWidget("score", score);

        WPushButton ok = new WPushButton(tr("new-bar-form.ok"));
        main.bindWidget("ok", ok);
        ok.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddBarDialog.this.accept();
                saveBar(street.getText(), number.getText(), zipcode.getText(),
                        city.getText(), country.getText(), name.getText(),
                        website.getText());
                Application app = Application.getInstance();
                //app.redirect(url);
            }
        });
    }
    
    private void saveBar(String street, String number, String zipcode,
            String city, String country, String name, String website) {
        Application app = Application.getInstance();
        Connection conn = null;
        location location;
        try {
            location = Geocoding.addressToLocation(new Address(0, street, number, zipcode, city, country));
        } catch (RestException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
        GeoLocation geoLocation=GeoLocation.fromDegrees(Double.parseDouble(location.lat),Double.parseDouble(location.lng));
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            String url=name.toLowerCase().replace(" ", "_");
            Result<Record1<Integer>> r = dsl.select(BAR.ID).from(BAR).where(BAR.URL.eq(url)).fetch();
            if(r.size()!=0){
               url=url+(r.size()+1);
            }
            AddressRecord rr=dsl.insertInto(ADDRESS, ADDRESS.STREET, ADDRESS.NUMBER,
                    ADDRESS.ZIPCODE, ADDRESS.CITY, ADDRESS.COUNTRY)
                    .values( street, number, zipcode, city, country).returning()
                    .fetchOne();
            
            dsl.insertInto(BAR, BAR.ADDRESS_ID, BAR.NAME, BAR.WEBSITE,BAR.URL,BAR.LOCATION_X,BAR.LOCATION_Y)
                    .values(rr.getId(), name, website,url,new Float(geoLocation.getLatitudeInRadians()),new Float(geoLocation.getLongitudeInRadians())).execute();
            conn.commit();
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
}
