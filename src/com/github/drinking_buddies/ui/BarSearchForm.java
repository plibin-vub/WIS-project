package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.jooq.utils.SearchUtils;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class BarSearchForm extends WContainerWidget {
    public BarSearchForm() {
        this(null, null);
    }
    
    public BarSearchForm(WContainerWidget parent) {
        this(null, parent);
    }
    
    public BarSearchForm(String needle) {
        this(needle, null);
    }
    
    public BarSearchForm(String needle, WContainerWidget parent) {
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
                        Application.getInstance().internalRedirect("/" + Application.BARS_URL + "/" + url);
                    } else {
                        showResults(barSearch.getText(), main);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        if (Application.getInstance().getLoggedInUser() != null) {
            WPushButton addBarButton = new WPushButton(tr("bar-search.add-bar"));
            main.bindWidget("add-bar", addBarButton);
            addBarButton.clicked().addListener(this, new Signal.Listener() {
                public void trigger() {
                    AddBarDialog dialog = new AddBarDialog(BarSearchForm.this);
                    dialog.show();
                }
            });
        } else {
            main.bindWidget("add-bar", null);
        }
        
        if (needle != null) {
            try {
                barSearch.setText(needle);
                showResults(needle, main);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void showResults(String needle, WTemplate main) throws SQLException {
        List<BarUrl> bars = findMatchingBars(needle);
        
        if (bars.size() > 0) {
            WTemplate results = new WTemplate(tr("bar-search-results"));
            TemplateUtils.configureDefault(Application.getInstance(), results);
            main.bindWidget("results", results);
            WContainerWidget resultEntries = new WContainerWidget();
            results.bindWidget("result-entries", resultEntries);
            for (BarUrl bu : bars) {
                WTemplate entry = new WTemplate(tr("bar-search-result"), resultEntries);
                TemplateUtils.configureDefault(Application.getInstance(), entry);
                WAnchor anchor=new WAnchor(new WLink(WLink.Type.InternalPath, "/"+Application.BARS_URL+"/"+bu.url),bu.name);
                entry.bindWidget("name", anchor);
            }
        } else {
            main.bindWidget("results", new WTemplate(tr("no-bar-search-results")));
        }
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
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);       
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
        } finally {
            app.closeConnection(conn);
        }
    }
}
