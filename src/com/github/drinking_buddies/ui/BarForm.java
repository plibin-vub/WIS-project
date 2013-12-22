package com.github.drinking_buddies.ui;



import static com.github.drinking_buddies.jooq.Tables.BAR;
import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.FAVORITE_BAR;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Beer;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.Image;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.jooq.tables.records.BarScoreRecord;
import com.github.drinking_buddies.ui.comments.BarCommentsWidget;
import com.github.drinking_buddies.ui.utils.DateUtils;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Icon;
import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.StandardButton;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDate;
import eu.webtoolkit.jwt.WDialog.DialogCode;
import eu.webtoolkit.jwt.WDoubleSpinBox;
import eu.webtoolkit.jwt.WFileUpload;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WMemoryResource;
import eu.webtoolkit.jwt.WMessageBox;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WTableView;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.chart.Axis;
import eu.webtoolkit.jwt.chart.AxisScale;
import eu.webtoolkit.jwt.chart.ChartType;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public class BarForm extends WContainerWidget {
    private WTemplate main;
    
    private Bar bar;
    private Application app;
    private int row;

    private boolean showChart=true;
    public BarForm(final Bar bar, List<Comment> comments, List<Beer> beers) {
        this.bar = bar;
        
        // the main template for the user form
        app=Application.getInstance();
        // (a WTemplate constructor accepts the template text and its parent)
        main = new WTemplate(tr("bar-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        
        showPhoto(bar.getPicture());
        
        // we bind to some of the template's variables
        main.bindString("facebook","http://www.facebook.com/plugins/like.php?href=https%3A%2F%2Fdrinking_buddies.github.com%2Fdb%2Fbars%2F"+bar.getUrl()+"&width&layout=button_count&action=like&show_faces=true&share=true&height=80&appId=620305514675365");
        main.bindString("bar", bar.getName());
        String addressLine1 = bar.getAddress().getStreet() + " "
                + bar.getAddress().getNumber();
        main.bindString("address1", addressLine1);
        String addressLine2 = bar.getAddress().getZipCode() + " "
                + bar.getAddress().getCity();
        main.bindString("address2", addressLine2);
        String addressLine3 = bar.getAddress().getCountry();
        main.bindString("address3", addressLine3);
        main.bindString("favored-by", String.valueOf(bar.getFavoredBy()));
        main.bindString("url", "aa1");
        final WDoubleSpinBox sb = new WDoubleSpinBox();
        sb.setRange(0, 10);
        sb.setDecimals(2);
        sb.addStyleClass("input-mini");
        double score = Double.parseDouble(new DecimalFormat("#.##",DecimalFormatSymbols.getInstance(Locale.US)).format(bar.getScore()));
        sb.setValue(score);
        sb.setSingleStep(1);
        sb.setEnabled(Application.getInstance().getLoggedInUser() != null);
        main.bindWidget("score", sb);
        final WPushButton changeScore = new WPushButton(tr("bar-form.change-score"));
        main.bindWidget("change-score", changeScore);
        changeScore.hide();
        sb.changed().addListener(this, new Signal.Listener() {
            public void trigger() {
                changeScore.show();
            }
        });
        
        WCartesianChart chart = getChart(bar.getId());
        if(showChart){
            main.bindWidget("score-chart",chart );
        }else{
            main.bindEmpty("score-chart");
        }
       
        changeScore.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                changeScore.hide();
                setScore(sb.getValue(),bar.getId());
                
                getChart(bar.getId());
                if(showChart){
                    main.bindWidget("score-chart", getChart(bar.getId()));
                }
            }       
        });
        final WTable beerList = new WTable();
        row=0;
        for (Beer beer : beers) {
            BeerListItemWidget beerListItem = new BeerListItemWidget(beer);
            beerList.getElementAt(row, 0).addWidget(beerListItem);
            row++;
        }
        if (Application.getInstance().getLoggedInUser() != null) {
            final WPushButton addBeer = new WPushButton(tr("bar-form.add-beer"));
            beerList.getElementAt(row, 0).addWidget(addBeer);
            addBeer.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
                public void trigger(WMouseEvent arg) {
                    AddBeerListItemDialog dialog = new AddBeerListItemDialog(bar.getId());
                    dialog.beerAdded().addListener(BarForm.this, new Signal1.Listener<Beer>() {
                        public void trigger(Beer beer) {
                            BeerListItemWidget beerListItem = new BeerListItemWidget(beer);
                            beerList.getElementAt(row, 0).removeWidget(addBeer);
                            beerList.getElementAt(row, 0).refresh();
                            beerList.getElementAt(row, 0).addWidget(beerListItem);
                            row++;
                            beerList.getElementAt(row, 0).addWidget(addBeer);
                        }
                    });
                    dialog.show();
                }       
            });
        }
        main.bindWidget("beer-list", beerList);
        main.bindWidget("comments", new BarCommentsWidget(bar,comments, app.getLoggedInUser(),null));
        if (Application.getInstance().getLoggedInUser() != null) {
            WPushButton addToFavorites = new WPushButton(tr("bar-form.add-to-favorites"));
            //connect a listener to the button
            addToFavorites.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
                public void trigger(WMouseEvent arg) {
                    boolean added = addToFavorites(bar);
                    
                    String prefix = "bar-form.add-to-favorites.";
                    WString status = tr(prefix + "status");
                    
                    final WMessageBox mb;
                    if (added) {
                        mb = new WMessageBox(status, 
                                tr(prefix + "added").arg(bar.getName()), 
                                Icon.Information,
                                EnumSet.of(StandardButton.Ok));
                    } else {
                       mb = new WMessageBox(status, 
                                tr(prefix+"not-added").arg(bar.getName()),
                                Icon.Warning,
                                EnumSet.of(StandardButton.Ok));
                    }
                    mb.setModal(true);
                    mb.buttonClicked().addListener(BarForm.this, new Signal1.Listener<StandardButton>() {
                        public void trigger(StandardButton sb) {
                            mb.done(DialogCode.Accepted);
                        }
                    });
                    mb.show();
                }
            });
            main.bindWidget("add-to-favorites", addToFavorites);
        } else {
            main.bindWidget("add-to-favorites", null);
        }
    }
    
    private boolean addToFavorites(Bar bar) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            
            User user = app.getLoggedInUser();
            
            DSLContext dsl = DBUtils.createDSLContext(conn);
            long count =
                    dsl
                        .select()
                        .from(FAVORITE_BAR)
                        .where(FAVORITE_BAR.USER_ID.equal(user.getId()))
                        .and(FAVORITE_BAR.BAR_ID.equal(bar.getId()))
                        .fetchCount();
            
            if (count > 0) {
                //did not add the beer to the user's favorites,
                //since it already is a favorite
                return false;
            } else {
                dsl
                    .insertInto(FAVORITE_BAR,
                                FAVORITE_BAR.BAR_ID,
                                FAVORITE_BAR.USER_ID)
                    .values(bar.getId(),
                            user.getId())
                    .execute();
                conn.commit();
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
    }
    
    private void showPhoto(Image image) {
        if (image != null) {
            WImage i = new WImage();
            WMemoryResource mr = new WMemoryResource(image.getMimetype());
            mr.setData(image.getData());
            i.setResource(mr);
            main.bindWidget("photo", i);
        } else {
            main.bindWidget("photo", null);
        }
        
        if (Application.getInstance().getLoggedInUser() != null) {
            WTemplate upload = new WTemplate(tr("bar-form-upload"));
            TemplateUtils.configureDefault(Application.getInstance(), upload);
            
            final WFileUpload fu = new WFileUpload();
            fu.changed().addListener(this, new Signal.Listener() {
                public void trigger() {
                    fu.upload();
                }
            });
            fu.uploaded().addListener(this, new Signal.Listener() {
                @Override
                public void trigger() {
                    saveAndShowPhoto(new File(fu.getSpoolFileName()), fu.getClientFileName());
                }
            });
            upload.bindWidget("upload", fu);
            main.bindWidget("upload", upload);
        } else {
            main.bindWidget("upload", null);
        }
    }
    
    private void saveAndShowPhoto(File file, String clientFileName) {
        try {
            byte[] data = FileUtils.readFileToByteArray(file);
            String extension = clientFileName.substring(clientFileName.lastIndexOf('.')+1);
            Image i = new Image(data, "image/" + extension);
            updatePhoto(i, bar.getId());
            bar.setPicture(i);
            showPhoto(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WStandardItemModel getModel(int id ) {
        
        WStandardItemModel model = new WStandardItemModel(10,2);
        model.setHeaderData(0, new WString("Date"));
        model.setHeaderData(1, new WString("Score"));
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            Result<Record2<BigDecimal, String>> r=dsl.select(BAR_SCORE.SCORE.avg(),BAR_SCORE.POST_TIME.substring(0,8))
                    .from(BAR_SCORE)
                    .join(BAR2_BAR_SCORE)
                    .on(BAR2_BAR_SCORE.BAR_SCORE_ID.equal(BAR_SCORE.ID))
                    .where(BAR2_BAR_SCORE.BAR_ID.eq(id))
                    .groupBy(BAR_SCORE.POST_TIME.substring(0,8)).orderBy(BAR_SCORE.POST_TIME.substring(0,8).asc()).fetch();
            if(r.size()<=1){
                showChart=false;
            }else{
                showChart=true;
            }
            BigDecimal sum=new BigDecimal(0);
            int index=0;
            for (int i = 0; i < r.size(); i++) {
                sum=sum.add(r.get(i).getValue(BAR_SCORE.SCORE.avg()));
                
               
                if(i>r.size()-10){
                    WDate date=WDate.fromString(r.get(i).getValue(BAR_SCORE.POST_TIME.substring(0,8)),"yyyy-MM");
                    model.setData(index,0,  date);
                    model.setData(index,1, sum.divide(BigDecimal.valueOf(i+1),BigDecimal.ROUND_HALF_EVEN));
                    index++;
                }
                
            }
           
            
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
        
        return model;
    }
    
    private void updatePhoto(Image image, int id) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            dsl
                .update(BAR)
                .set(BAR.PHOTO, image.getData())
                .set(BAR.PHOTO_MIME_TYPE, image.getMimetype())
                .where(BAR.ID.eq(id))
                .execute();
            conn.commit();
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        } 
    }
    
    private void setScore(double value,int id) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            BarScoreRecord r = dsl.insertInto(BAR_SCORE,BAR_SCORE.POST_TIME,BAR_SCORE.SCORE,BAR_SCORE.USER_ID)
            .values(DateUtils.javaDateToSqliteFormat(new Date()),(float)value,1).returning().fetchOne();
            dsl.insertInto(BAR2_BAR_SCORE,BAR2_BAR_SCORE.BAR_ID,BAR2_BAR_SCORE.BAR_SCORE_ID)
            .values(id,r.getId()).execute();
            conn.commit();
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }  
        
    }
   

    private WCartesianChart getChart(int id) {
        WContainerWidget container = new WContainerWidget();
        WCartesianChart chart = new WCartesianChart(container);
        chart.setModel(getModel(id));
        chart.setXSeriesColumn(0);
        chart.setType(ChartType.ScatterPlot);
        WDataSeries s = new WDataSeries(1, SeriesType.CurveSeries);
        chart.addSeries(s);
        chart.getAxis(Axis.XAxis).setScale(AxisScale.DateScale);
        chart.resize(350, 150);
        //chart.resize(new WLength(350), new WLength(100));
        return chart;
    }
}
