package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.USER;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.Main;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.jooq.utils.SearchUtils;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;
import com.github.drinking_buddies.ui.utils.TemplateUtils;
import com.github.drinking_buddies.webservices.facebook.Facebook;
import com.github.drinking_buddies.webservices.facebook.Person;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

import eu.webtoolkit.jwt.Signal;
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
        final WTemplate main = new WTemplate(tr("start-form"), this);
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
                    //update the current user in the header
                    TemplateUtils.setHeader(Application.getInstance(), main);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (RestException e) {
                    e.printStackTrace();
                }
            }
        });
        process.connectStartAuthenticate(login.clicked());
        
        {
            final WLineEdit beerSearch = new WLineEdit();
            final AutocompletePopup beerPopup = new AutocompletePopup(new BeerAutocompleteDatabaseModel(10), beerSearch, main);
            main.bindWidget("beer-search-text", beerSearch);
            WPushButton beerSearchButton = new WPushButton(tr("start-form.search-beer"));
            main.bindWidget("beer-search-button", beerSearchButton);
            beerSearchButton.clicked().addListener(this, new Signal.Listener(){
                public void trigger() {
                    try {
                        String url = SearchUtils.getBeerURL(beerSearch.getText());
                        Application app = Application.getInstance();
                        if (url == null) {
                            app.searchBeers(beerSearch.getText());       
                        } else {
                           app.internalRedirect("/" + Application.BEERS_URL + "/" + url); 
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        {
            final WLineEdit barSearch = new WLineEdit();
            final AutocompletePopup barPopup = new AutocompletePopup(new BarAutocompleteDatabaseModel(10), barSearch, main);
            main.bindWidget("bar-search-text", barSearch);
            WPushButton barSearchButton = new WPushButton(tr("start-form.search-bar"));
            main.bindWidget("bar-search-button", barSearchButton);
            barSearchButton.clicked().addListener(this, new Signal.Listener(){
                public void trigger() {
                    try {
                        String url = SearchUtils.getBarURL(barSearch.getText());
                        Application app = Application.getInstance();
                        if (url == null) {
                            app.searchBeers(barSearch.getText());       
                        } else {
                           app.internalRedirect(Application.BARS_URL + "/" + url); 
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    private void authenticated(Identity id, OAuthAccessToken token) throws SQLException, RestException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        
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
                               USER.LAST_NAME,
                               USER.URL
                               )
                    .values(id.getId(),
                            id.getProvider(),
                            p.getFirst_name(),
                            p.getLast_name(),
                            createUserURL(p)
                            )
                    .execute();
               
                r = dsl
                        .select()
                        .from(USER)
                        .where(USER.OAUTH_NAME.eq(id.getId()))
                        .and(USER.OAUTH_PROVIDER.eq(id.getProvider()))
                        .fetchOne();
            }
            
            conn.commit();
            
            if (r != null) {
                app.login(new User(r));
            } else {
                throw new RuntimeException("Error occured when logging in!");
            }
        } finally {
            app.closeConnection(conn);
        }
    }
    
    private String createUserURL(Person p) {
        //in order to be complete, 
        //we should check that this URL is not in the database yet:
        //if that would be the case we could add an incrementing number to assure uniqueness
        return p.getFirst_name().toLowerCase() + "." + p.getLast_name().toLowerCase();
    }
}
