package com.aimsio.utils;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.ButtonTheme;
import com.vaadin.addon.charts.model.style.FontWeight;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;

import java.sql.SQLException;
import java.util.*;

import static com.vaadin.addon.charts.model.Compare.PERCENT;

public class ChartUtils {
    public static Chart ChartBuilder() throws SQLException {
        Chart timeline = new Chart();
        timeline.setSizeFull();

        Configuration configuration = timeline.getConfiguration();
        configuration.getTitle().setText("# of Signals over Time");

        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().getLabels().setEnabled(false);

        configuration.getyAxis().setMin(0);
        configuration.getyAxis().setTitle("# of Signals");
        configuration.getLegend().setEnabled(true);
        Map<String,DataSeries> map = createSeries();
        for (DataSeries value : map.values()) {
            configuration.addSeries(value);
        }

        Navigator navigator = configuration.getNavigator();
        navigator.setEnabled(true);
        navigator.setMargin(75);
        PlotOptionsSeries plotOptions=new PlotOptionsSeries();
        plotOptions.setColor(SolidColor.BROWN);
        navigator.setSeries(plotOptions);

        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(4);
        ButtonTheme theme = new ButtonTheme();
        Style style = new Style();
        style.setColor(new SolidColor("#0766d8"));
        style.setFontWeight(FontWeight.BOLD);
        theme.setStyle(style);
        rangeSelector.setButtonTheme(theme);
        timeline.setTimeline(true);
        configuration.setRangeSelector(rangeSelector);
        timeline.drawChart(configuration);
        return timeline;
    }
    private static Map<String,DataSeries> createSeries() throws SQLException {
        Map<String, DataSeries>  seriesMap = new HashMap<String,DataSeries>();

        List<Map<String,Object>> dataSet = DBUtils.executeQuery("SELECT STATUS AS 'status', COUNT(STATUS) AS number,entry_date FROM assets.signal GROUP BY entry_date,STATUS ORDER BY entry_date");

        ListIterator<Map<String, Object>> iterator =dataSet.listIterator();
        while (iterator.hasNext()){
            Map<String,Object> item = iterator.next();
            DataSeries dataSeries = seriesMap.get(item.get("status"));
            if(dataSeries==null){
                dataSeries =new DataSeries();
                dataSeries.setName(item.get("status").toString());
                seriesMap.put(item.get("status").toString(),dataSeries);
            }
            DataSeriesItem dataSeriesItem = new DataSeriesItem();
            dataSeriesItem.setX((Date) item.get("entry_date"));
            dataSeriesItem.setY((Number) item.get("number"));
            dataSeries.add(dataSeriesItem);
        }
        return seriesMap;
    }

}
