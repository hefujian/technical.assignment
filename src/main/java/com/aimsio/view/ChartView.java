package com.aimsio.view;

import com.aimsio.utils.ChartUtils;
import com.aimsio.utils.ComboBoxUtils;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import java.sql.*;


public class ChartView extends VerticalLayout {
    public ChartView() throws SQLException {

        ComboBox<String> assetUNComboBox = new ComboBox<>("AssetUN:", ComboBoxUtils.getAllAssetUN());
        ComboBox<String> stateComboBox = new ComboBox<>("State:", ComboBoxUtils.getStatus());
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(assetUNComboBox);
        horizontalLayout.addComponent(stateComboBox);
        this.addComponent(horizontalLayout);

        this.addComponent(ChartUtils.ChartBuilder());

        assetUNComboBox.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                Notification.show("Value changed:",
                        String.valueOf(valueChangeEvent.getValue()),
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });

        stateComboBox.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                Notification.show("Value changed:",
                        String.valueOf(valueChangeEvent.getValue()),
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });
    }
}
