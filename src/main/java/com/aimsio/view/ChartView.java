package com.aimsio.view;

import com.aimsio.utils.ChartUtils;
import com.aimsio.utils.ComboBoxUtils;
import com.aimsio.utils.DBUtils;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.data.HasValue;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import java.sql.*;
import java.util.List;
import java.util.Optional;


public class ChartView extends VerticalLayout {
    private Chart timeline;
    private ComboBox<String> assetUNComboBox;
    private ComboBox<String> statusComboBox;

    public ChartView() {
        List<String> assetUNList = null;
        try {
            assetUNList = ComboBoxUtils.getAssetUN();

            assetUNComboBox = new ComboBox<>("AssetUN:", assetUNList);
            assetUNComboBox.setPlaceholder("All assetUNs");

            List<String> statusList = ComboBoxUtils.getStatus();
            statusComboBox = new ComboBox<>("Status:", statusList);
            statusComboBox.setPlaceholder("All status");

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.addComponent(assetUNComboBox);
            horizontalLayout.addComponent(statusComboBox);
            this.addComponent(horizontalLayout);
            timeline = ChartUtils.chartBuilder();
            this.addComponent(timeline);
        } catch (SQLException e) {
            e.printStackTrace();
            Notification.show("Can not init the page.",
                    Notification.Type.TRAY_NOTIFICATION);
        }
        assetUNComboBox.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
            String assetUN = "";
            String status = "";
            if (valueChangeEvent.getValue() != null) {
                assetUN = valueChangeEvent.getValue();
            }
            if (statusComboBox.getValue() != null) {
                status = statusComboBox.getSelectedItem().get();
            }

            try {
                timeline = ChartUtils.rebuild(timeline, assetUN, status);
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.show("Rebuilding the chart fails.",
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });

        statusComboBox.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
            String assetUN = "";
            String status = "";
            if (valueChangeEvent.getValue() != null) {
                status = valueChangeEvent.getValue();
            }
            if (this.assetUNComboBox.getValue() != null) {
                assetUN = assetUNComboBox.getSelectedItem().get();
            }
            try {
                timeline = ChartUtils.rebuild(timeline, assetUN, status);
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.show("Rebuilding the chart fails.",
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });
    }
}
