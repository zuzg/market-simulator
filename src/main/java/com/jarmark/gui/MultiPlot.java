package com.jarmark.gui;

import com.jarmark.entity.asset.Asset;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ListView;
import java.util.HashMap;

/**
 * class to plot multiple prices on one chart, related to multi-plot stage
 */
public class MultiPlot {
    @FXML
    private ListView<String> assetsToPlotList;
    @FXML
    private LineChart<String, Number> multipleAssetsPlot;

    protected void plotMultipleAssets(ListView<String> assetsIds, HashMap<String, Asset> assetsList){
        assetsToPlotList.setItems(assetsIds.getItems());
        assetsToPlotList.setOnMouseClicked(mouseEvent -> {
            String chosenAssetName = assetsToPlotList.getSelectionModel().getSelectedItem();
            multipleAssetsPlot.getData().add(assetsList.get(chosenAssetName).percentageHistory);
        });
    }
}
