package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.WText;

public class BeerListItemWidget extends WTemplate{

    public BeerListItemWidget(final Beer beer) {
        super(tr("beer-list-item"), null);
        TemplateUtils.configureDefault(Application.getInstance(), this);
        WText name = new WText(beer.getName());
        this.bindWidget("name", name);
        name.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                Application app = Application.getInstance();
                app.internalRedirect("/" + Application.BEERS_URL + "/" + beer.getUrl());
            }
        });
    }

}
