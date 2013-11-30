package com.github.drinking_buddies.ui.utils;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.User;

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
        WTemplate header = new WTemplate(WString.tr("header"));
        
        User u = app.getLoggedInUser();
        if (u != null) {
            WTemplate tt = new WTemplate(WString.tr("logged-in-user"));
            tt.addFunction("tr", WTemplate.Functions.tr);
            tt.bindString("first-name", u.getFirstName());
            tt.bindString("last-name", u.getLastName());
            header.bindWidget("logged-in-user", tt);
        } else {
            header.bindWidget("logged-in-user", null);
        }
        
        t.bindWidget("header", header);
    }
}
