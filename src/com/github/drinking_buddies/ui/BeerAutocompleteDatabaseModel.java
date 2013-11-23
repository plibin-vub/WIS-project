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
import com.github.drinking_buddies.ui.autocompletion.AutocompleteDatabaseModel;

public class BeerAutocompleteDatabaseModel extends AutocompleteDatabaseModel {
    public BeerAutocompleteDatabaseModel(int limit) {
        super(limit);
    }

    @Override
    public void filterQuery(String s, int limit, ArrayList<String> list) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = app.createDSLContext(conn);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            app.closeConnection(conn);
        }
    }
}
