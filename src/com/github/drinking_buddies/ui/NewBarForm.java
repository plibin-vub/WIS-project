package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.sql.Connection;

import org.jooq.DSLContext;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class NewBarForm extends WDialog {

    
    public NewBarForm(WObject parent) {
        super(tr("new-bar-form.title").arg("Add new Bar"), parent);
        final WTemplate main = new WTemplate(tr("new-bar-form"), this.getContents());
        TemplateUtils.configureDefault(Application.getInstance(), main);
        // we bind to some of the template's variables
        final WLineEdit name = new WLineEdit();
        main.bindWidget("bar", name);
        final WLineEdit website = new WLineEdit();
        main.bindWidget("website", website);
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
                NewBarForm.this.accept();
                saveBar(street.getText(), number.getText(), zipcode.getText(),
                        city.getText(), country.getText(), name.getText(),
                        website.getText());
                
            }
        });
    }
    
    private void saveBar(String street, String number, String zipcode,
            String city, String country, String name, String website) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = app.createDSLContext(conn);
            Integer addressId = dsl.select(ADDRESS.ID).from(ADDRESS).orderBy(ADDRESS.ID.desc()).fetchAny().value1();
            dsl.insertInto(ADDRESS, ADDRESS.ID, ADDRESS.STREET, ADDRESS.NUMBER,
                    ADDRESS.ZIPCODE, ADDRESS.CITY, ADDRESS.COUNTRY)
                    .values(addressId, street, number, zipcode, city, country)
                    .execute();
            int barId = dsl.select(BAR.ID).from(BAR).orderBy(BAR.ID.desc()).fetchAny().value1();
            dsl.insertInto(BAR, BAR.ID, BAR.ADDRESS_ID, BAR.NAME, BAR.WEBSITE)
                    .values(barId, addressId, name, website).execute();
            conn.commit();
        } catch (Exception e) {
            app.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
}
