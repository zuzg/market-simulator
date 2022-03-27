package com.jarmark.gui;

import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.asset.Company;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BuyoutPanel implements Initializable {
    @FXML
    private ListView<String> companiesList;
    @FXML
    private Slider percentageSlider;
    @FXML
    private Label percentageLabel;
    @FXML
    private Label boughtLabel;

    private int buyOutPercent;

    @FXML
    public void buyOutClicked(ListView<String> companiesNames, HashMap<String, Asset> companies) {
        companiesList.setItems(companiesNames.getItems());
        companiesList.setOnMouseClicked(mouseEvent -> {
            String chosenCompanyName = companiesList.getSelectionModel().getSelectedItem();
            Company company = (Company) companies.get(chosenCompanyName);
            float toBuy = 0.01F * buyOutPercent * company.getQuantity();
            company.addQuantity(-toBuy);
            boughtLabel.setText("Bought out " + toBuy + " shares of " + company.getName());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buyOutPercent = (int) (percentageSlider.getValue());
        percentageLabel.setText("50%");
        percentageSlider.valueProperty().addListener((observableValue, number, t1) -> {
            buyOutPercent = (int) percentageSlider.getValue();
            percentageLabel.setText(buyOutPercent + "%");
        });
    }


}
