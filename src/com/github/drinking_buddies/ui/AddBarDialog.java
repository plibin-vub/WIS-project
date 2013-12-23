package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.sql.Connection;
import java.util.EnumSet;

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

import eu.webtoolkit.jwt.Icon;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.StandardButton;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMessageBox;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WRegExpValidator;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WValidator;
import eu.webtoolkit.jwt.WValidator.State;


//This dialog is used to add a new bar.
//It contains a from where the name and address of the bar can be added.
//Using the address and Google's geocoding web sevice we get the coordinates of the bar.
public class AddBarDialog extends WDialog {
    public AddBarDialog(WObject parent) {
        super(tr("new-bar-form.title").arg("Add new Bar"), parent);
        
        resize(new WLength(400), WLength.Auto);
        
        final WTemplate main = new WTemplate(tr("new-bar-form"),
                this.getContents());
        TemplateUtils.configureDefault(Application.getInstance(), main);
        // we bind to some of the template's variables
        final WLineEdit name = new WLineEdit();
        main.bindWidget("bar", name);
        name.setId("inputName");
        name.setValidator(createManditoryValidator());
        final WLineEdit street = new WLineEdit();
        main.bindWidget("street", street);
        street.setId("inputStreet");
        final WLineEdit number = new WLineEdit();
        main.bindWidget("number", number);
        number.setId("inputNumber");
        final WLineEdit zipcode = new WLineEdit();
        main.bindWidget("zipcode", zipcode);
        zipcode.setId("inputZipcode");
        final WLineEdit city = new WLineEdit();
        main.bindWidget("city", city);
        city.setId("inputCity");
        final WLineEdit country = new WLineEdit();
        main.bindWidget("country", country);
        country.setId("inputCountry");

        WPushButton ok = new WPushButton(tr("new-bar-form.ok"));
        main.bindWidget("ok", ok);
        WPushButton cancel = new WPushButton(tr("new-bar-form.cancel"));
        main.bindWidget("cancel", cancel);
        ok.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                if(name.validate()!= State.Valid){
                    showError("Invalid Name");
                }
                if(zipcode.validate()!= State.Valid){
                    showError("Invalid zipcode");
                }
                if(number.validate()!= State.Valid){
                    showError("Invalid zipcode");
                }
                String url = saveBar(street.getText(), number.getText(),
                        zipcode.getText(), city.getText(), country.getText(),
                        name.getText(), null);
                if(url!=null){
                    Application app = Application.getInstance();
                    app.internalRedirect(url);
                }
                AddBarDialog.this.accept();
            }
        });
        cancel.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddBarDialog.this.remove();
            }
        });

    }
    
    //This validator is used for the manditory fields.
    private WValidator createManditoryValidator() {
        WValidator v = new WValidator();
        v.setMandatory(true);
        return v;
    }
    
    
    //This method will save the bar to the database.
    private String saveBar(String street, String number, String zipcode,
            String city, String country, String name, String website) {
        Application app = Application.getInstance();
        Connection conn = null;
        location location;
        try {
            //Here Google's geocoding web sevice is used to get the coordinates of the bar.
            //We need the coordinates to be able to search for the bar on location.
            location = Geocoding.addressToLocation(new Address(0, street,
                    number, zipcode, city, country));
        } catch (RestException e1) {
            showError("Unable to locate address");
            e1.printStackTrace();
            return null;
        }
        if(location==null){
            showError("Unable to locate address");
            return null;
        }
        //we store the coordinates as radians in the database
        GeoLocation geoLocation = GeoLocation.fromDegrees(
                Double.parseDouble(location.lat),
                Double.parseDouble(location.lng));
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            String url = name.toLowerCase().replace(" ", "_");
            Result<Record1<Integer>> r = dsl.select(BAR.ID).from(BAR)
                    .where(BAR.URL.eq(url)).fetch();
            if (r.size() != 0) {
                url = url + (r.size() + 1);
            }
            //first create the address
            AddressRecord rr = dsl
                    .insertInto(ADDRESS, ADDRESS.STREET, ADDRESS.NUMBER,
                            ADDRESS.ZIPCODE, ADDRESS.CITY, ADDRESS.COUNTRY)
                    .values(street, number, zipcode, city, country).returning()
                    .fetchOne();
            //then the bar can be added
            dsl.insertInto(BAR, BAR.ADDRESS_ID, BAR.NAME, BAR.WEBSITE, BAR.URL,
                    BAR.LOCATION_X, BAR.LOCATION_Y)
                    .values(rr.getId(), name, website, url,
                            new Float(geoLocation.getLatitudeInRadians()),
                            new Float(geoLocation.getLongitudeInRadians()))
                    .execute();
            conn.commit();
            return "/bars/"+url;
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }

    //Show a popup with an error message.
    private void showError(String string) {
        final WMessageBox messageBox = new WMessageBox("Error",string,Icon.Warning, EnumSet.of(StandardButton.Ok));
                messageBox.setModal(false);
                messageBox.buttonClicked().addListener(this,
                        new Signal.Listener() {
                    public void trigger() {
                        if (messageBox != null)
                            messageBox.remove();
                    }
                    
                });
        messageBox.show();
        
    }
}
