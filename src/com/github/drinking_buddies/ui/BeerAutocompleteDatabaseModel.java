package com.github.drinking_buddies.ui;

import java.util.ArrayList;

import com.github.drinking_buddies.ui.autocompletion.AutocompleteDatabaseModel;

public class BeerAutocompleteDatabaseModel extends AutocompleteDatabaseModel {
    public BeerAutocompleteDatabaseModel(int limit) {
        super(limit);
    }

    static String [] lijst = {"duvel", "duvel tripel hop", "westmalle tripel", "westmalle dobbel", "leffe blond", "leffe bruin", "leffe radieus"};
    
    @Override
    public void filterQuery(String s, int limit, ArrayList<String> list) {
        for (String ss : lijst) {
            if (ss.startsWith(s))
                list.add(ss);
        }
    }
}
