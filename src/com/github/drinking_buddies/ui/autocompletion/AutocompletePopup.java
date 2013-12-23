package com.github.drinking_buddies.ui.autocompletion;

import eu.webtoolkit.jwt.Key;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WKeyEvent;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WSuggestionPopup;
import eu.webtoolkit.jwt.WWebWidget;

//A popup that listens to a WLineEdit:
//on every change in the text of the WLineEdit, 
//it will ask the AutocompleteDatabaseModel to redo its filtering.
//The contents of this model are than propagated to this popup.
public class AutocompletePopup extends WSuggestionPopup {
    private String filter = null;
    
    public AutocompletePopup(final AutocompleteDatabaseModel model, final WLineEdit searchText, WObject parent) {
        super(createOptions(), parent);
        this.setModel(model);
        forEdit(searchText);
        searchText.keyWentUp().addListener(this, new Signal1.Listener<WKeyEvent>() {
            public void trigger(WKeyEvent ke) {
                if (filterKey(ke.getKey()))
                    filterModel().trigger(searchText.getText());
            }
        });
        
        this.filterModel().addListener(this, new Signal1.Listener<String>() {
            public void trigger(String f) {
                if (filter == null || !filter.equals(f)) {
                    filter = f;
                    model.filter(f);
                    if (model.getRowCount() > 0)
                        filter(f);
                }
            }
        });
    }
    
    private boolean filterKey(Key key) {
        return key != Key.Key_Enter && key !=Key.Key_Tab;
    }
    
    private void filter(String input) {
        //some JWt fiddling, 
        //since we update the model via a non-standard way,
        //we need to invoke some javascript to update the UI
        WApplication.getInstance().doJavaScript(
                "jQuery.data(" + this.getJsRef() + ", 'obj').filtered("
                                + WWebWidget.jsStringLiteral(input) + ","
                                + "1" + ");");
    }
    
    //the WSuggestionPopup's configuration
    private static Options createOptions() {
        Options o = new Options();
        o.highlightBeginTag = "<b>";
        o.highlightEndTag = "</b>";
        o.listSeparator = 0;
        o.appendReplacedText = "";
        o.whitespace = "";
        o.wordSeparators = "";
        o.wordStartRegexp = "";
        return o;
    }
}
