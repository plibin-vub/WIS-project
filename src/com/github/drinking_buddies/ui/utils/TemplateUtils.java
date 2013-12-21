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

//some template utility functions
public class TemplateUtils {
    //default configuration of all templates:
    //expose the tr function in templates and make some variables available
    public static void configureDefault(Application app, WTemplate t) {
        t.addFunction("tr", WTemplate.Functions.tr);
        t.bindString("app.url", app.resolveRelativeUrl(app.getBookmarkUrl("/")));
        t.bindString("app.base.url", app.getEnvironment().getDeploymentPath());
        t.bindString("app.context", app.getServletContext().getContextPath());
        
        setHeader(app, t);
    }
    
    //configure the application's header:
    //- show the current user (can be clicked: navigates to the user's form)
    //- allow the header to be clicked
    public static void setHeader(final Application app, WTemplate t) {
        WTemplate userLoggedIn = new WTemplate(WString.tr("header-user"));
        WNavigationBar navigation = new WNavigationBar();
        WImage image = new WImage(new WLink("pics/header.png"));
        image.clicked().addListener(t, new Signal.Listener(){
                public void trigger() {
                    app.internalRedirect("/");
                }
        });
        navigation.addWidget(image);
        //navigation bar should not respond to screen size:
        //this is only necessary if you have menu-items in the bar
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
