package com.jarmark.gui;

import com.jarmark.entity.investor.Investor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * shows info about investor
 */
public class InvestorPanel {
    @FXML
    Label investorName;
    @FXML
    Label investorDescription;

    protected void showInvestor(Investor investor) {
        investorName.setText("Hi, this is " + investor.getUniqueName());
        investorDescription.setText(investor.description());
    }
}
