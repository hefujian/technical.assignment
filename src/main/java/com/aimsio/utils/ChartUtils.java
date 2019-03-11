package com.aimsio.utils;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.ButtonTheme;
import com.vaadin.addon.charts.model.style.FontWeight;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.*;


public class ChartUtils {
    public static Chart chartBuilder() throws SQLException {
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
    public static Chart rebuild(Chart chart,String assetUN, String status) throws SQLException {
        Map<String,DataSeries> map = createSeries(assetUN,status);
        Configuration configuration = chart.getConfiguration();
        List<Series> newSeries = new ArrayList<>();
        for (DataSeries value : map.values()) {
            newSeries.add(value);
        }
        configuration.setSeries(newSeries);
        chart.drawChart(configuration);
        return chart;
    }
    private static Map<String,DataSeries> createSeries() throws SQLException {
        return createSeries("","");
    }

    private static Map<String,DataSeries> createSeries(String assetUN, String status) throws SQLException {
        String sql = "SELECT STATUS AS 'status', COUNT(STATUS) AS number,entry_date FROM assets.signal";

        List<Map<String,Object>> dataSet = new ArrayList<>();
        if(assetUN!=null && assetUN!="" && status!=null && status!=""){
            sql += " WHERE assetun=? and status=?";
        }else if(assetUN!=null && assetUN!=""){
            sql += " WHERE assetun=?";
        }else if(status!=null && status!=""){
            sql += " WHERE status=?";
        }
        sql += "  GROUP BY entry_date,STATUS ORDER BY entry_date";

        Map<String, DataSeries>  seriesMap = new HashMap<String,DataSeries>();

        if(assetUN!=null && assetUN!="" && status!=null && status!=""){
            dataSet= DBUtils.executeQuery(sql,assetUN,status);
        }else if(assetUN!=null && assetUN!=""){
            dataSet= DBUtils.executeQuery(sql,assetUN);

        }else if(status!=null && status!=""){
            dataSet= DBUtils.executeQuery(sql,status);
        }else
            dataSet= DBUtils.executeQuery(sql);

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
