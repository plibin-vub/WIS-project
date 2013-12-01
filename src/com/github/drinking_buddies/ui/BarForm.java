package com.github.drinking_buddies.ui;



import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.util.derby.sys.Sys;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.Tag;
import com.github.drinking_buddies.jooq.tables.records.BarScoreRecord;
import com.github.drinking_buddies.ui.comments.BarCommentsWidget;
import com.github.drinking_buddies.ui.comments.CommentsWidget;
import com.github.drinking_buddies.ui.comments.ReviewCommentsWidget;
import com.github.drinking_buddies.ui.utils.DateUtils;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAbstractItemModel;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDoubleSpinBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WObject;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTableView;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.chart.ChartType;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;
import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BAR_SCORE;

public class BarForm extends WContainerWidget {
    
    private Application app;
    public BarForm(final Bar bar, List<Tag> tags, List<Comment> comments) {
        // the main template for the user form
        app=Application.getInstance();
        // (a WTemplate constructor accepts the template text and its parent)
        WTemplate main = new WTemplate(tr("bar-form"), this);
        TemplateUtils.configureDefault(Application.getInstance(), main);
        // we bind to some of the template's variables
        main.bindString("website", bar.getWebsite());
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
        sb.setValue(bar.getScore());
        sb.setSingleStep(1);
        main.bindWidget("score", sb);
        final WCartesianChart chart = getChart(bar.getId());
        main.bindWidget("score-chart", chart);
        WPushButton changeScore = new WPushButton(tr("bar-form.change-score"));
        main.bindWidget("change-score", changeScore);
        changeScore.clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
            public void trigger(WMouseEvent arg) {
                setScore(sb.getValue(),bar.getId());
                getModel(bar.getId());
            }       
        });
        main.bindWidget("comments", new BarCommentsWidget(comments, app.getLoggedInUser(),null));
    }
    
    private WStandardItemModel getModel(int id ) {
        WStandardItemModel model = new WStandardItemModel(10,2);
        model.setHeaderData(0, new WString("Date"));
        model.setHeaderData(1, new WString("Score"));
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = app.createDSLContext(conn);
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
            app.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            app.closeConnection(conn);
        }
        
        return model;
    }
    
    
    private void setScore(double value,int id) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            DSLContext dsl = app.createDSLContext(conn);
            BarScoreRecord r = dsl.insertInto(BAR_SCORE,BAR_SCORE.POST_TIME,BAR_SCORE.SCORE,BAR_SCORE.USER_ID)
            .values(DateUtils.javaDateToSqliteFormat(new Date()),(new Double(value)).intValue(),1).returning().fetchOne();
            dsl.insertInto(BAR2_BAR_SCORE,BAR2_BAR_SCORE.BAR_ID,BAR2_BAR_SCORE.BAR_SCORE_ID)
            .values(id,r.getId()).execute();
            conn.commit();
        } catch (Exception e) {
            app.rollback(conn);
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
        chart.resize(new WLength(350), new WLength(100));
        return chart;
    }
}
