package com.github.drinking_buddies.ui.autocompletion;

import java.util.ArrayList;

import eu.webtoolkit.jwt.WAbstractTableModel;
import eu.webtoolkit.jwt.WModelIndex;

/**
 * Database model that can be filtered, 
 * and keeps a limited number of results (to be configured upon construction).
 * This model is to be used to implement autocompletion.
 * The implementor needs to override the filterQuery method.
 */
public abstract class AutocompleteDatabaseModel extends WAbstractTableModel {
    private final int limit;
    protected ArrayList<String> list;
    
    public AutocompleteDatabaseModel(final int limit) {
        this.limit = limit;
        this.list = new ArrayList<String>(limit);
    }
    
    @Override
    public int getColumnCount(WModelIndex mi) {
        return 1;
    }

    @Override
    public int getRowCount(WModelIndex mi) {
        return this.list.size();
    }
    
    @Override
    public Object getData(WModelIndex mi, int role) {
        return list.get(mi.getRow());
    }
    
    public void filter(String s) {
        this.list.clear();
        if (s.length() > 0)
            filterQuery(s, this.limit, this.list);
        
        layoutChanged().trigger();
    }
    
    /**
     * The query needs to fill in the ArrayList passed as argument,
     * this ArrayList is empty.
     */
    public abstract void filterQuery(String s, final int limit, ArrayList<String> list);
}
