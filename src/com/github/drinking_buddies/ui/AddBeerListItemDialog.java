package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BEER;
import static com.github.drinking_buddies.jooq.Tables.BEER2_BAR;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.exception.InvalidResultException;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.jooq.tables.records.Beer2BarRecord;
import com.github.drinking_buddies.ui.autocompletion.AutocompletePopup;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

//This dialog is used to add a beer to the beerlist of a bar
public class AddBeerListItemDialog extends WDialog{

    private Signal1<Beer> beerAdded = new Signal1<Beer>();
    
    public AddBeerListItemDialog(final int barId) {
        super(tr("add-beer-dialog.title"));
        
        final WTemplate main = new WTemplate(tr("add-beer-dialog"), this.getContents());
        final WLineEdit beerSearch = new WLineEdit();
        //the autocomplete for the beer input box
        final AutocompletePopup beerPopup = new AutocompletePopup(new BeerAutocompleteDatabaseModel(10), beerSearch, main);
        main.bindWidget("field", beerSearch);
        
        WPushButton beerFindButton = new WPushButton(tr("add-beer-dialog.add-beer"));
        main.bindWidget("add-beer", beerFindButton);
              
        beerFindButton.clicked().addListener(this, new Signal.Listener() {
            public void trigger() {
                
                       Beer beer =saveBeer( beerSearch.getText(),barId);
                       if(beer!=null){
                           beerAdded.trigger(beer);
                           AddBeerListItemDialog.this.accept();
                       }
                       
                       
                    }
                
            
        });
        WPushButton cancel = new WPushButton(tr("new-bar-form.cancel"));
        main.bindWidget("cancel", cancel);
        cancel.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddBeerListItemDialog.this.remove();
            }
        });
    }
    
    
    //This method will add the beer to the beerlist of the bar.
    protected Beer saveBeer(String beerName,int barId) {
        Application app = Application.getInstance();
        Connection conn = app.getConnection();
        DSLContext dsl = DBUtils.createDSLContext(conn);
        try {
            Record3<String, Integer, String> b = 
                    dsl
                        .select(BEER.URL,BEER.ID,BEER.NAME)
                        .from(BEER)
                        .where(BEER.NAME.equal(beerName))
                        .fetchOne();
            Beer2BarRecord rr = dsl
                    .insertInto(BEER2_BAR,BEER2_BAR.BEER_ID, BEER2_BAR.BAR_ID)
                    .values(b.getValue(BEER.ID),barId).returning()
                    .fetchOne();
            conn.commit();
            if (b == null || rr==null)
                return null;
            else
                return new Beer(b.getValue(BEER.ID), b.getValue(BEER.NAME), b.getValue(BEER.URL));
        } catch (InvalidResultException ire) {
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    //Signal that is triggered when the beer is added to the bar.
    //This allows for decoupling this dialog and the 
    //code that calls this dialog.
    public Signal1<Beer> beerAdded() {
        return beerAdded;
    }
}
