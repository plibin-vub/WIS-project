package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BEER;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.ui.autocompletion.AutocompleteDatabaseModel;

//The database model for the Beer autocomplete widget.
public class BeerAutocompleteDatabaseModel extends AutocompleteDatabaseModel {
    public BeerAutocompleteDatabaseModel(int limit) {
        super(limit);
    }

    @Override
    //We query for all beers that start with the 's'.
    //'s' is the string typed by the user in the auto-complete line edit.
    //We store all the results in the specified list.
    public void filterQuery(String s, int limit, ArrayList<String> list) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            Result<Record1<String>> results = 
                    dsl
                        .select(BEER.NAME)
                        .from(BEER)
                        .where(BEER.NAME.startsWith(s))
                        .orderBy(BEER.NAME, BEER.ID)
                        .limit(limit)
                        .fetch();
            
            for (Record r : results) {
                list.add(r.getValue(BEER.NAME));
            }
        } finally {
            app.closeConnection(conn);
        }
    }
}
