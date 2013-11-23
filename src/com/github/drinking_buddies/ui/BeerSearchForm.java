package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BEER;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.exception.InvalidResultException;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class BeerSearchForm extends WContainerWidget {
    public BeerSearchForm() {
        this(null, null);
    }
    
    public BeerSearchForm(WContainerWidget parent) {
        this(null, parent);
    }
    
    public BeerSearchForm(String searchString, WContainerWidget parent) {
        super(parent);
        
        final WTemplate main = new WTemplate(tr("beer-search-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);

        final WLineEdit beerSearch = new WLineEdit();
        final AutocompletePopup beerPopup = new AutocompletePopup(new BeerAutocompleteDatabaseModel(10), beerSearch, main);
        main.bindWidget("field", beerSearch);
        WPushButton beerFindButton = new WPushButton(tr("beer-search.find-beer"));
        main.bindWidget("find", beerFindButton);
        
        main.bindWidget("results", null);
        
        beerFindButton.clicked().addListener(this, new Signal.Listener() {
            public void trigger() {
                try {
                    //will be null if:
                    // - the beer does not exists
                    // - the beer name is not complete
                    String url = getBeerURL(beerSearch.getText());
                    if (url != null) {
                        Application.getInstance().internalRedirect(Application.BEERS_URL + "/" + url);
                    } else {
                        List<BeerUrl> beers = findMatchingBeers(beerSearch.getText());
                        
                        WTemplate results = new WTemplate(tr("beer-search-results"));
                        TemplateUtils.configureDefault(Application.getInstance(), results);
                        main.bindWidget("results", results);
                        WContainerWidget resultEntries = new WContainerWidget();
                        results.bindWidget("result-entries", resultEntries);
                        for (BeerUrl bu : beers) {
                            WTemplate entry = new WTemplate(tr("beer-search-result"), resultEntries);
                            TemplateUtils.configureDefault(Application.getInstance(), entry);
                            entry.bindString("relative-url", Application.BEERS_URL + "/" + bu.url);
                            entry.bindString("name", bu.name);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    class BeerUrl {
        public BeerUrl(Record r) {
            this.name = r.getValue(BEER.NAME);
            this.url = r.getValue(BEER.URL);
        }
        String name;
        String url;
    }
    private List<BeerUrl> findMatchingBeers(String beerName) throws SQLException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = app.createDSLContext(conn);       
        List<BeerUrl> beers = new ArrayList<BeerUrl>();
        Result<Record> records =
                dsl
                    .select()
                    .from(BEER)
                    .where(BEER.NAME.like("%"+beerName+"%"))
                    .orderBy(BEER.NAME, BEER.ID)
                    .fetch();
       for (Record r : records)
           beers.add(new BeerUrl(r));
       return beers;
    }
    
    private String getBeerURL(String beerName) throws SQLException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = app.createDSLContext(conn);
        try {
            Record1<String> b = 
                    dsl
                        .select(BEER.URL)
                        .from(BEER)
                        .where(BEER.NAME.equal(beerName))
                        .fetchOne();
            if (b == null)
                return null;
            else
                return b.value1();
        } catch (InvalidResultException ire) {
            return null;
        }
    }
}
