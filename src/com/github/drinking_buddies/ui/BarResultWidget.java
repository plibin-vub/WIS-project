package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WTemplate;

public class BarResultWidget extends WTemplate {

    
    public BarResultWidget(final Bar bar, WContainerWidget root) {
        super(tr("bar-result"), root);
        TemplateUtils.configureDefault(Application.getInstance(), this);
        this.bindString("bar", bar.getName());
        String addressLine1 = bar.getAddress().getStreet() + " "
                + bar.getAddress().getNumber();
        this.bindString("address1", addressLine1);
        String addressLine2 = bar.getAddress().getZipCode() + " "
                + bar.getAddress().getCity();
        this.bindString("address2", addressLine2);
        String addressLine3 = bar.getAddress().getCountry();
        this.bindString("address3", addressLine3);
        this.bindString("website", bar.getWebsite());
        this.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                Application app = Application.getInstance();
                app.internalRedirect("/" + Application.BARS_URL + "/" + bar.getUrl());
            }
        });
    }
    
    public BarResultWidget(Bar bar) {
        this(bar, null);
    }
}
