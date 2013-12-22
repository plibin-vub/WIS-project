package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.sql.Connection;
import java.util.ArrayList;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.ui.autocompletion.AutocompleteDatabaseModel;

//The database model for the Bar autocomplete widget.
public class BarAutocompleteDatabaseModel extends AutocompleteDatabaseModel {
    public BarAutocompleteDatabaseModel(int limit) {
        super(limit);
    }

    @Override
    //We query for all bars that start with the 's'.
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
                        .select(BAR.NAME)
                        .from(BAR)
                        .where(BAR.NAME.startsWith(s))
                        .orderBy(BAR.NAME, BAR.ID)
                        .limit(limit)
                        .fetch();
            
            for (Record r : results) {
                list.add(r.getValue(BAR.NAME));
            }
        } finally {
            app.closeConnection(conn);
        }
    }
}
