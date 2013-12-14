package com.github.drinking_buddies.ui.utils;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.User;

import eu.webtoolkit.jwt.AlignmentFlag;
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
    
    public static void setHeader(Application app, WTemplate t) {
        WTemplate userLoggedIn = new WTemplate(WString.tr("header-user"));
        WNavigationBar navigation = new WNavigationBar();
//        navigation.setTitle("Drinking Buddies", new WLink(
//                "#"));
        
        navigation.addWidget(new WImage(new WLink("pics/header.png")));
        navigation.setResponsive(false);
        navigation.addStyleClass("navbar-inverse");
        navigation.addStyleClass("navbar-fixed-top");
        User u = app.getLoggedInUser();
        if (u != null) {
            WTemplate tt = new WTemplate(WString.tr("logged-in-user"));
            tt.addFunction("tr", WTemplate.Functions.tr);
            tt.bindString("first-name", u.getFirstName());
            tt.bindString("last-name", u.getLastName());
            //WAnchor anchor=new WAnchor(new WLink(type, value));
            tt.bindString("url", app.makeAbsoluteUrl("db/users/"+u.getUrl()));
            userLoggedIn.bindWidget("logged-in-user", tt);
        } else {
            userLoggedIn.bindWidget("logged-in-user", null);
        }
        navigation.addWidget(userLoggedIn,AlignmentFlag.AlignRight);
        t.bindWidget("header", navigation);
    }
}
