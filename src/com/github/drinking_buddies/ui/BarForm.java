package com.github.drinking_buddies.ui;



import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

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
import com.github.drinking_buddies.jooq.tables.records.BarScoreRecord;
import com.github.drinking_buddies.ui.comments.BarCommentsWidget;
import com.github.drinking_buddies.ui.utils.DateUtils;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDoubleSpinBox;
import eu.webtoolkit.jwt.WFileUpload;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WMemoryResource;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTable;
import eu.webtoolkit.jwt.WTableView;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.chart.ChartType;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public class BarForm extends WContainerWidget {
    private WTemplate main;
    
    private Bar bar;
    private Application app;
    private int row;
    public BarForm(final Bar bar, List<Comment> comments, List<Beer> beers) {
        this.bar = bar;
        
        // the main template for the user form
        app=Application.getInstance();
        // (a WTemplate constructor accepts the template text and its parent)
        main = new WTemplate(tr("bar-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        
        showPhoto(bar.getPicture());
        
        // we bind to some of the template's variables
        main.bindString("website", bar.getWebsite());
        main.bindString("facebook","http://www.facebook.com/plugins/like.php?href=https%3A%2F%2Fdrinking_buddies.github.com%2Fdb%2Fbars%2F"+bar.getUrl()+"&width&layout=standard&action=like&show_faces=true&share=true&height=80&appId=620305514675365");
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
        sb.addStyleClass("input-mini");
        sb.setValue(bar.getScore());
        sb.setSingleStep(1);
        main.bindWidget("score", sb);
        final WPushButton changeScore = new WPushButton(tr("bar-form.change-score"));
        main.bindWidget("change-score", changeScore);
        changeScore.hide();
        sb.changed().addListener(this, new Signal.Listener() {
            public void trigger() {
                changeScore.show();
            }
        });
        final WCartesianChart chart = getChart(bar.getId());
        main.bindWidget("score-chart", chart);
        chart.resize(350, 150);
        changeScore.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                changeScore.hide();
                setScore(sb.getValue(),bar.getId());
                getModel(bar.getId());
            }       
        });
        final WTable beerList = new WTable();
        row=0;
        for (Beer beer : beers) {
            BeerListItemWidget beerListItem = new BeerListItemWidget(beer);
            beerList.getElementAt(row, 0).addWidget(beerListItem);
            row++;
        }
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
        main.bindWidget("beer-list", beerList);
        main.bindWidget("comments", new BarCommentsWidget(bar,comments, app.getLoggedInUser(),null));
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
        main.bindWidget("upload", fu);
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
            Result<Record2<BigDecimal, String>> r=dsl.select(BAR_SCORE.SCORE.avg(),BAR_SCORE.POST_TIME.substring(0,17))
                    .from(BAR_SCORE)
                    .join(BAR2_BAR_SCORE)
                    .on(BAR2_BAR_SCORE.BAR_SCORE_ID.equal(BAR_SCORE.ID))
                    .where(BAR2_BAR_SCORE.BAR_ID.eq(id))
                    .groupBy(BAR_SCORE.POST_TIME.substring(0,17)).orderBy(BAR_SCORE.POST_TIME.substring(0,17).asc()).fetch();
            BigDecimal sum=new BigDecimal(0);
            int index=0;
            for (int i = 0; i < r.size(); i++) {
                sum=sum.add(r.get(i).getValue(BAR_SCORE.SCORE.avg()));
                
               
                if(i>r.size()-10){
                    model.setData(index,0, index);//r.get(i).getValue(BAR_SCORE.POST_TIME.substring(0,17)));
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
            .values(DateUtils.javaDateToSqliteFormat(new Date()),(new Double(value)).intValue(),1).returning().fetchOne();
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
        WTableView table = new WTableView(container);
        table.setModel(getModel(id));
        WCartesianChart chart = new WCartesianChart(container);
        chart.setModel(getModel(id));
        chart.setXSeriesColumn(0);
        chart.setType(ChartType.ScatterPlot);
        WDataSeries s = new WDataSeries(1, SeriesType.CurveSeries);
        chart.addSeries(s);
        //chart.resize(new WLength(350), new WLength(100));
        return chart;
    }
}
