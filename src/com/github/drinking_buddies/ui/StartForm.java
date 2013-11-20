package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.USER;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.Main;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.facebook.Facebook;
import com.github.drinking_buddies.webservices.facebook.Person;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.auth.FacebookService;
import eu.webtoolkit.jwt.auth.Identity;
import eu.webtoolkit.jwt.auth.OAuthAccessToken;
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
        
        final OAuthProcess process = fbs.createProcess(fbs.getAuthenticationScope());        
        process.authenticated().addListener(this, new Signal1.Listener<Identity>() {
            public void trigger(Identity id) {
                try {
                    authenticated(id, process.getToken());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (RestException e) {
                    e.printStackTrace();
                }
            }
        });
        process.connectStartAuthenticate(login.clicked());
        
        final WLineEdit beerSearch = new WLineEdit();
        final AutocompletePopup beerPopup = new AutocompletePopup(new BeerAutocompleteDatabaseModel(10), beerSearch, main);
        main.bindWidget("beer-search-text", beerSearch);
        WPushButton beerSearchButton = new WPushButton(tr("start-form.search-beer"));
        main.bindWidget("beer-search-button", beerSearchButton);
    }
    
    private void authenticated(Identity id, OAuthAccessToken token) throws SQLException, RestException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = app.createDSLContext(conn);
        
        Record r 
            = dsl
                .select()
                .from(USER)
                .where(USER.OAUTH_NAME.eq(id.getId()))
                .and(USER.OAUTH_PROVIDER.eq(id.getProvider()))
                .fetchOne();
        
        try {
            if (r == null) {
               //new user, add him/her to the database
               Person p = new Facebook(token.getValue()).getUser("me");
               
               dsl
                   .insertInto(USER,
                               USER.OAUTH_NAME,
                               USER.OAUTH_PROVIDER,
                               USER.FIRST_NAME,
                               USER.LAST_NAME)
                    .values(id.getId(),
                            id.getProvider(),
                            p.getFirst_name(),
                            p.getLast_name())
                    .execute();
               
                r = dsl
                        .select()
                        .from(USER)
                        .where(USER.OAUTH_NAME.eq(id.getId()))
                        .and(USER.OAUTH_PROVIDER.eq(id.getProvider()))
                        .fetchOne();
            }
            
            if (r != null) {
                app.login(new User(r));
            } else {
                throw new RuntimeException("Error occured when logging in!");
            }
        } finally {
            conn.commit();
        }
    }
}
