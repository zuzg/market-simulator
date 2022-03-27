package com.jarmark.gui;

import com.jarmark.entity.market.Market;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * related to market information window
 */
public class MarketPanel {
    @FXML
    Label marketName;
    @FXML
    Label marketDescription;

    protected void showMarket(Market market) {
        marketName.setText("Welcome to " + market.getName());
        marketDescription.setText(market.description());
    }
}
