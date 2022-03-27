package com.jarmark.gui;

import com.jarmark.entity.asset.Asset;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

/**
 * shows info about an asset
 */
public class AssetPanel {
    @FXML
    LineChart<String, Number> priceChart;
    @FXML
    Label assetName;
    @FXML
    Label assetDescription;

    protected void showAsset(Asset asset) {
        assetName.setText(asset.getName());
        assetDescription.setText(asset.description());
    }

    protected void plotPrice(Asset asset) throws InterruptedException {
        showAsset(asset);
        priceChart.getData().add(asset.priceHistory);
    }
}
