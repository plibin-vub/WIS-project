package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BAR;

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
import com.github.drinking_buddies.jooq.utils.SearchUtils;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class BarSearchForm extends WContainerWidget {
    public BarSearchForm() {
        this(null, null);
    }
    
    public BarSearchForm(WContainerWidget parent) {
        this(null, parent);
    }
    
    public BarSearchForm(String searchString, WContainerWidget parent) {
        super(parent);
        
        final WTemplate main = new WTemplate(tr("bar-search-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);

        final WLineEdit barSearch = new WLineEdit();
        final AutocompletePopup barPopup = new AutocompletePopup(new BarAutocompleteDatabaseModel(10), barSearch, main);
        main.bindWidget("field", barSearch);
        WPushButton barFindButton = new WPushButton(tr("bar-search.find-bar"));
        main.bindWidget("find", barFindButton);
        
        main.bindWidget("results", null);
        
        barFindButton.clicked().addListener(this, new Signal.Listener() {
            public void trigger() {
                try {
                    String url = SearchUtils.getBarURL(barSearch.getText());
                    if (url != null) {
                        Application.getInstance().internalRedirect(Application.BARS_URL + "/" + url);
                    } else {
                        List<BarUrl> bars = findMatchingBars(barSearch.getText());
                        
                        WTemplate results = new WTemplate(tr("bar-search-results"));
                        TemplateUtils.configureDefault(Application.getInstance(), results);
                        main.bindWidget("results", results);
                        WContainerWidget resultEntries = new WContainerWidget();
                        results.bindWidget("result-entries", resultEntries);
                        for (BarUrl bu : bars) {
                            WTemplate entry = new WTemplate(tr("bar-search-result"), resultEntries);
                            TemplateUtils.configureDefault(Application.getInstance(), entry);
                            entry.bindString("relative-url", Application.BARS_URL + "/" + bu.url);
                            entry.bindString("name", bu.name);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        WPushButton addBarButton = new WPushButton(tr("bar-search.add-bar"));
        main.bindWidget("add-bar", addBarButton);
        addBarButton.clicked().addListener(this, new Signal.Listener() {
            public void trigger() {
                AddBarDialog dialog = new AddBarDialog(BarSearchForm.this);
                dialog.show();
            }
        });
    }
    
    class BarUrl {
        public BarUrl(Record r) {
            this.name = r.getValue(BAR.NAME);
            this.url = r.getValue(BAR.URL);
        }
        String name;
        String url;
    }
    private List<BarUrl> findMatchingBars(String barName) throws SQLException {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = app.createDSLContext(conn);       
        List<BarUrl> bars = new ArrayList<BarUrl>();
        Result<Record> records =
                dsl
                    .select()
                    .from(BAR)
                    .where(BAR.NAME.like("%"+barName+"%"))
                    .orderBy(BAR.NAME, BAR.ID)
                    .fetch();
       for (Record r : records)
           bars.add(new BarUrl(r));
       return bars;
    }
}
