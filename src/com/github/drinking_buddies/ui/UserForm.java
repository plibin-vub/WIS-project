package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;

/**
 * The user form UI component.
 * This form shows user information and location, favorite beers and favorite bars.
 */
public class UserForm extends WContainerWidget {
    public UserForm(User user) {
        //the main template for the user form 
        //(a WTemplate constructor accepts the template text and its parent)
        WTemplate main = new WTemplate(tr("user-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        //we bind to some of the template's variables
        main.bindString("user", user.getName());
        main.bindString("birthdate", user.getBirthdate());

        }
    }


