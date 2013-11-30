package com.github.drinking_buddies.ui;

import static com.github.drinking_buddies.jooq.Tables.BEER2_BEER_TAG;
import static com.github.drinking_buddies.jooq.Tables.BEER_TAG;

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectConditionStep;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.jooq.tables.records.BeerTagRecord;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLineEdit;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WTemplate;

public class AddTagDialog extends WDialog {
    enum Type {
        AddNew ("add-tag-dialog.add-new"),
        SelectExisting ("add-tag-dialog.select-existing");
        
        private String key;
        
        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
    
    private Signal1<Tag> tagAdded = new Signal1<Tag>();
    
    private WLineEdit tagName;
    private WComboBox tagList;
    
    public AddTagDialog(final Beer beer, WObject parent) {
        super(tr("add-tag-dialog.title").arg(beer.getName()), parent);
        
        final WTemplate main = new WTemplate(tr("add-tag-dialog"), this.getContents());
        
        final WComboBox typeCombo = new WComboBox();
        typeCombo.addItem(tr(Type.AddNew.key));
        if (showTagList(beer))
            typeCombo.addItem(tr(Type.SelectExisting.key));
        main.bindWidget("type-combo", typeCombo);
        
        typeCombo.changed().addListener(this, new Signal.Listener(){
            public void trigger() {
                Type type = getType(typeCombo.getCurrentText().getKey());
                setVariableField(type, beer, main);
            }
        });
        
        WPushButton ok = new WPushButton(tr("add-tag-dialog.ok"));
        main.bindWidget("ok", ok);
        ok.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddTagDialog.this.accept();
                Type type = getType(typeCombo.getCurrentText().getKey());
                Tag tag = save(type, beer);
                tagAdded.trigger(tag);
            }
        });
        
        WPushButton cancel = new WPushButton(tr("add-tag-dialog.cancel"));
        main.bindWidget("cancel", cancel);
        cancel.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                AddTagDialog.this.reject();
            }
        });
        
        Type type = getType(typeCombo.getCurrentText().getKey());
        setVariableField(type, beer, main);
    }
    
    private Type getType(String key) {
        for (Type t : Type.values()) {
            if (t.key.equals(key))
                return t;
        }
        return null;
    }
    
    private boolean showTagList(Beer beer) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
 
            return createTagListQuery(dsl, beer).fetchCount() > 0;         
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
    
    private void setVariableField(Type type, Beer beer, final WTemplate t) {
        if (type == Type.AddNew) {
            tagName = new WLineEdit();
            tagName.setPlaceholderText(tr("add-tag-dialog.provide-new-tag-name"));
            t.bindWidget("variable-field", tagName);
        } else if (type == Type.SelectExisting) {
            tagList = new WComboBox();
                
            Application app = Application.getInstance();
            Connection conn = null;
            try {
                conn = app.getConnection();
                DSLContext dsl = DBUtils.createDSLContext(conn);
                //fetch all tags in the database, 
                //except the ones that are already connected to this beer
                Result<Record1<String>> results 
                    = createTagListQuery(dsl, beer).fetch();
                
                for (Record1<String> r : results)
                    tagList.addItem(r.value1());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                app.closeConnection(conn);
            }
            t.bindWidget("variable-field", tagList);
        }
    } 
    
    private SelectConditionStep<Record1<String>> createTagListQuery(DSLContext dsl, Beer beer) {
       return dsl
                    .select(BEER_TAG.NAME)
                    .from(BEER_TAG)
                    .where(BEER_TAG.ID.notIn(dsl
                            .select(BEER2_BEER_TAG.BEER_TAG_ID)
                            .from(BEER2_BEER_TAG)
                            .where(BEER2_BEER_TAG.BEER_ID.equal(beer.getId()))));
    }

    private Tag save(Type type, Beer beer) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            
            if (type == Type.AddNew) {
                String name = tagName.getText();
                
                DSLContext dsl = DBUtils.createDSLContext(conn);
                BeerTagRecord r 
                    = dsl
                        .insertInto(BEER_TAG, BEER_TAG.NAME)
                        .values(name)
                        .returning().fetchOne();
                
                dsl
                    .insertInto(BEER2_BEER_TAG, BEER2_BEER_TAG.BEER_TAG_ID, BEER2_BEER_TAG.BEER_ID)
                    .values(r.getId(), beer.getId())
                    .execute();
                
                conn.commit();
                
                return new Tag(r.getId(), name);
            } else if (type == Type.SelectExisting) {
                String name = tagList.getValueText();
                
                DSLContext dsl = DBUtils.createDSLContext(conn);
                Record1<Integer> r 
                    = dsl
                        .select(BEER_TAG.ID)
                        .from(BEER_TAG)
                        .where(BEER_TAG.NAME.equal(name))
                        .fetchOne();
                
                dsl
                    .insertInto(BEER2_BEER_TAG, BEER2_BEER_TAG.BEER_TAG_ID, BEER2_BEER_TAG.BEER_ID)
                    .values(r.value1(), beer.getId())
                    .execute();
                
                conn.commit();
                
                return new Tag(r.value1(), name);
            }
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
        
        return null;
    }
    
    public Signal1<Tag> tagAdded() {
        return tagAdded;
    }
}
