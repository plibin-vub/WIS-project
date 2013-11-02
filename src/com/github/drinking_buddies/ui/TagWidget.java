package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WTemplate;

/**
 *  A widget that represent a tag.
 */
public class TagWidget extends WTemplate {
    public TagWidget(CharSequence text, WContainerWidget root) {
        super(tr("tag"), root);
        TemplateUtils.configureDefault(Application.getInstance(), this);
        
        this.bindString("text", text);
    }
    
    public TagWidget(CharSequence cs) {
        this(cs, null);
    }
}
