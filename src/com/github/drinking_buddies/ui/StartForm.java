package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.Main;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.auth.FacebookService;
import eu.webtoolkit.jwt.auth.Identity;
import eu.webtoolkit.jwt.auth.OAuthProcess;

/**
 * Start form, where users can select a beer/bar and log on.
 */
public class StartForm extends WContainerWidget {
    public StartForm() {
        WTemplate main = new WTemplate(tr("start-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);

        WAnchor login = new WAnchor();
        login.setText(tr("start-form.login"));
        main.bindWidget("login", login);
        
        FacebookService fbs = Main.getFacebookService();
        
        OAuthProcess process = fbs.createProcess(fbs.getAuthenticationScope());        
        process.authenticated().addListener(this, new Signal1.Listener<Identity>() {
            public void trigger(Identity id) {
                authenticated(id);
            }
        });
        process.connectStartAuthenticate(login.clicked());
    }
    
    private void authenticated(Identity id) {
        //TODO
        //add to database if new user
        //import friends
        //login on the application
        System.err.println(id.getId() + " - " + id.getProvider());
    }
}
