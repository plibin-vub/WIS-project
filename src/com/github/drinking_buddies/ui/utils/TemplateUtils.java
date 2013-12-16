package com.github.drinking_buddies.ui.utils;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.User;

import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WNavigationBar;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTemplate;

public class TemplateUtils {
    public static void configureDefault(Application app, WTemplate t) {
        t.addFunction("tr", WTemplate.Functions.tr);
        t.bindString("app.url", app.resolveRelativeUrl(app.getBookmarkUrl("/")));
        t.bindString("app.base.url", app.getEnvironment().getDeploymentPath());
        t.bindString("app.context", app.getServletContext().getContextPath());
        
        setHeader(app, t);
    }
    
    public static void setHeader(final Application app, WTemplate t) {
        WTemplate userLoggedIn = new WTemplate(WString.tr("header-user"));
        WNavigationBar navigation = new WNavigationBar();
//        navigation.setTitle("Drinking Buddies", new WLink(
//                "#"));
        WImage image = new WImage(new WLink("pics/header.png"));
        image.clicked().addListener(t, new Signal.Listener(){
                public void trigger() {
                    app.internalRedirect("/");
                }
        });
        navigation.addWidget(image);
        navigation.setResponsive(false);
        navigation.addStyleClass("navbar-inverse");
        navigation.addStyleClass("navbar-fixed-top");
        User u = app.getLoggedInUser();
        if (u != null) {
            WTemplate tt = new WTemplate(WString.tr("logged-in-user"));
            tt.addFunction("tr", WTemplate.Functions.tr);
            WAnchor anchor=new WAnchor(new WLink(WLink.Type.InternalPath, "/users/"+u.getUrl()),u.getFirstName()+" "+u.getLastName());
            tt.bindWidget("url", anchor);
            userLoggedIn.bindWidget("logged-in-user", tt);
        } else {
            userLoggedIn.bindWidget("logged-in-user", null);
        }
        navigation.addWidget(userLoggedIn,AlignmentFlag.AlignRight);
        t.bindWidget("header", navigation);
    }
}
