package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.jooq.tables.records.AddressRecord;
import com.github.drinking_buddies.jooq.tables.records.ReviewRecord;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

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
        try {
            conn = app.getConnection();
            DSLContext dsl = app.createDSLContext(conn);
            String url=name.toLowerCase().replace(" ", "_");
            Result<Record1<Integer>> r = dsl.select(BAR.ID).from(BAR).where(BAR.URL.eq(url)).fetch();
            if(r.size()!=0){
               url=url+(r.size()+1);
            }
            AddressRecord rr=dsl.insertInto(ADDRESS, ADDRESS.STREET, ADDRESS.NUMBER,
                    ADDRESS.ZIPCODE, ADDRESS.CITY, ADDRESS.COUNTRY)
                    .values( street, number, zipcode, city, country).returning()
                    .fetchOne();
            
            dsl.insertInto(BAR, BAR.ADDRESS_ID, BAR.NAME, BAR.WEBSITE,BAR.URL)
                    .values(rr.getId(), name, website,url).execute();
            conn.commit();
        } catch (Exception e) {
            app.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
}
