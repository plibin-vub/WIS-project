package com.github.drinking_buddies.ui;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.ui.utils.TemplateUtils;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WStandardItemModel;
import eu.webtoolkit.jwt.WString;
import eu.webtoolkit.jwt.WTemplate;
import eu.webtoolkit.jwt.chart.ChartType;
import eu.webtoolkit.jwt.chart.SeriesType;
import eu.webtoolkit.jwt.chart.WCartesianChart;
import eu.webtoolkit.jwt.chart.WDataSeries;

public class BarForm extends WContainerWidget {
    public BarForm(Bar bar) {
        // the main template for the user form
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
        main.bindString("score", String.valueOf(bar.getScore()));

        main.bindWidget("score-chart", getChart());

    }

   

   

    private WCartesianChart getChart() {
        WContainerWidget container = new WContainerWidget();
        WStandardItemModel model = new WStandardItemModel(10, 10, container);
        model.setHeaderData(0, new WString("Time"));
        model.setHeaderData(1, new WString("Score"));
        for (int i = 0; i < 40; ++i) {
            double x = ((double) i - 20) / 4;
            model.setData(i, 0, x);
            model.setData(i, 1, Math.sin(x));
        }
        WCartesianChart chart = new WCartesianChart(container);
        chart.setModel(model);
        chart.setXSeriesColumn(0);
        // chart.setLegendEnabled(true);
        chart.setType(ChartType.ScatterPlot);
        // chart.getAxis(Axis.XAxis).setLocation(AxisValue.ZeroValue);
        // chart.getAxis(Axis.YAxis).setLocation(AxisValue.ZeroValue);
        // chart.setPlotAreaPadding(80, EnumSet.of(Side.Left));
        // chart.setPlotAreaPadding(40, EnumSet.of(Side.Top, Side.Bottom));
        WDataSeries s = new WDataSeries(1, SeriesType.CurveSeries);
        // s.setShadow(new WShadow(3, 3, new WColor(0, 0, 0, 127), 3));
        chart.addSeries(s);
        chart.resize(new WLength(350), new WLength(100));
        // chart.setMargin(new WLength(10), EnumSet.of(Side.Top, Side.Bottom));
        // chart.setMargin(WLength.Auto, EnumSet.of(Side.Left, Side.Right));
        return chart;
    }
}
