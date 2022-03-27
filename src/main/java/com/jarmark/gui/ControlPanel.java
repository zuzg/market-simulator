package com.jarmark.gui;

import com.jarmark.MainApplication;
import com.jarmark.control.World;
import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.investor.InvestmentFund;
import com.jarmark.entity.investor.PrivateInvestor;
import com.jarmark.entity.market.CommodityMarket;
import com.jarmark.entity.market.CurrencyMarket;
import com.jarmark.entity.market.Market;
import com.jarmark.entity.market.StockMarket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * supreme panel to rule them all!
 * a class which holds together all the panels and is responsible for control panel
 */
public class ControlPanel implements Initializable {
    @FXML
    private Slider ratioSlider;
    @FXML
    private Slider speedSlider;
    @FXML
    private Label valueLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private MenuButton entitiesMenu;
    @FXML
    private Button multiPlotButton;
    @FXML
    private Button refreshButton;
    @FXML
    private ListView<String> marketsNamesList;
    @FXML
    private ListView<String> investorsNamesList;
    @FXML
    private ListView<String> assetsIdsList;
    @FXML
    private ListView<String> transactionsList;

    private ListView<String> companiesNames = new ListView<>();
    private World world = new World(60, 50);

    private void checkRatio() throws FileNotFoundException {
        Random random = new Random();
        if(world.investors.size() < 0.4 * world.allAssets.size()) {
            if(random.nextInt() % 3 == 0 || random.nextInt() % 3 == 1) {
                PrivateInvestor investor = world.createPrivateInvestor();
                investorsNamesList.getItems().add(investor.getUniqueName());
            } else {
                InvestmentFund investor = world.createInvestmentFund();
                investorsNamesList.getItems().add(investor.getUniqueName());
            }
        }
    }

    private void addMarket(Market market) throws FileNotFoundException {
        marketsNamesList.getItems().add(market.getName());
        for(Object asset : market.getAssets().values()) {
            assetsIdsList.getItems().add(((Asset)asset).getId());
        }
        checkRatio();
    }

    @FXML
    private void onCreateCurrencyMarketClick() throws InterruptedException, FileNotFoundException {
        CurrencyMarket market = world.createCurrencyMarket();
        addMarket(market);
    }//todo generalize creation
    @FXML
    private void onCreateCommodityMarketClick() throws FileNotFoundException {
        CommodityMarket market = world.createCommodityMarket();
        addMarket(market);
    }
    @FXML
    private void onCreateStockMarketClick() throws FileNotFoundException {
        StockMarket market = world.createStockMarket();
        addMarket(market);
        for(Asset asset : market.getAssets().values()) {
            companiesNames.getItems().add(asset.getId());
        }
    }
    @FXML
    private void onCreatePrivateInvestorClick() throws FileNotFoundException {
        PrivateInvestor investor = world.createPrivateInvestor();
        investorsNamesList.getItems().add(investor.getUniqueName());
    }
    @FXML
    private void onCreateInvestmentFundClick() throws FileNotFoundException {
        InvestmentFund investor = world.createInvestmentFund();
        investorsNamesList.getItems().add(investor.getUniqueName());
    }
    @FXML
    private void onCreateAssetClick() throws InterruptedException, FileNotFoundException {
        Random random = new Random();
        if (world.markets.size() != 0) {
            int i = random.nextInt(world.markets.size());
            String marketName = marketsNamesList.getItems().get(i);

            Asset newAsset = world.markets.get(marketName).addAsset();
            world.allAssets.put(newAsset.getId(), newAsset);
            assetsIdsList.getItems().add(newAsset.getId());
            checkRatio();
        }
    }
    @FXML
    private void onStartButtonClick() throws FileNotFoundException {
        if (world.markets.size() != 0) {
            world.run();
        }
    }
    @FXML
    private void refreshPriceHistory() {
        transactionsList.getItems().clear();
        transactionsList.getItems().setAll(world.transactions);
/*        for (String log : world.transactions)
            transactionsList.getItems().add(log);*/
    }
    /**
     * manage sliders
     * @param url needed to initialize
     * @param resourceBundle also needed
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        world.setBearBullRatio((int) (ratioSlider.getValue()));
        valueLabel.setText((int) world.getBearBullRatio() + "%");

        ratioSlider.valueProperty().addListener((observableValue, number, t1) -> {
            world.setBearBullRatio((int) (ratioSlider.getValue()));
            valueLabel.setText((int) world.getBearBullRatio() + "%");
            // TODO delete labels under sliders? noo
        });

        world.setTransactionsPerMin((int) (speedSlider.getValue()));
        speedLabel.setText(world.getTransactionsPerMin() + "");

        speedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            world.setTransactionsPerMin((int) (speedSlider.getValue()));
            speedLabel.setText(String.valueOf(world.getTransactionsPerMin()));
        });
    }
    /**
     * helper function to create new window (stage)
     * @param title title of the new window
     * @param root root of the new stage
     */
    private void newStage(String title, Parent root) {
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void showMarketInfo() {
        String chosenMarketName = marketsNamesList.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("market-panel.fxml"));
            Parent root = loader.load();
            MarketPanel marketPanel = loader.getController();
            marketPanel.showMarket(world.markets.get(chosenMarketName));
            newStage("info", root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showAssetInfo() {
        String chosenAssetName = assetsIdsList.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("asset-panel.fxml"));
            Parent root = loader.load();

            AssetPanel assetPanel = loader.getController();
            assetPanel.plotPrice(world.allAssets.get(chosenAssetName));
            newStage(chosenAssetName, root);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showInvestorInfo() {
        String chosenInvestorName = investorsNamesList.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("investor-panel.fxml"));
            Parent root = loader.load();
            InvestorPanel investorPanel = loader.getController();
            investorPanel.showInvestor(world.investors.get(chosenInvestorName));
            newStage(chosenInvestorName, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void plotMultipleAssets() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("multi-plot.fxml"));
        Parent root = loader.load();
        MultiPlot multiPlot = loader.getController();
        multiPlot.plotMultipleAssets(assetsIdsList, world.allAssets);
        newStage("multi", root);
    }
    @FXML
    private void performBuyOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("buyout-panel.fxml"));
        Parent root = loader.load();
        BuyoutPanel buyoutPanel = loader.getController();
        buyoutPanel.buyOutClicked(companiesNames, world.allAssets);
        newStage("perform buy out", root);
    }
}
