package com.jarmark.entity.market;

import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.asset.Company;
import com.jarmark.entity.asset.StockMarketIndex;
import com.jarmark.entity.creator.EntityCreator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * class for a stock market, where stocks (companies shares) can be traded
 */
public class StockMarket extends Market<Company> {
    private String tradingCurrency;
    private final ArrayList<StockMarketIndex> stockMarketIndices;

    public StockMarket(EntityCreator ec, String name, String country, String city, String address, float percentMargin, String tradingCurrency) throws FileNotFoundException {
        super(ec, name, country, city, address, percentMargin);
        this.tradingCurrency = tradingCurrency;
        this.stockMarketIndices = new ArrayList<>();
        this.createIndices();
        this.createCompanies();
    }

    /**
     * create indices from data file
     * @throws FileNotFoundException
     */
    private void createIndices() throws FileNotFoundException {
        for (int i = 0; i < 3; i ++) {
            List<String> info = getEc().readFromCsv("stock_market_indices_data.csv");
            StockMarketIndex smi = new StockMarketIndex(info.get(0), Float.parseFloat(info.get(1)), Float.parseFloat(info.get(2)));
            stockMarketIndices.add(smi);
        }
    }

    /**
     * check to which indices newly created company belongs and add it
     * (company to index and index to company)
     * @param company company to add to market's stock market indices
     */
    private void addToIndex(Company company) {
        for (StockMarketIndex smi : stockMarketIndices) {
            if (smi.checkThreshold(company.getIpoShareValue())) {
                smi.add(company);
                company.addStockMarketIndex(smi);
            }
        }
    }

    @Override
    public Asset addAsset() throws FileNotFoundException {
        List<String> info = getEc().readFromCsv("company_data.csv");
        Company company = new Company(info.get(0), info.get(1), Float.parseFloat(info.get(2)), Float.parseFloat(info.get(3)),
                Float.parseFloat(info.get(4)), info.get(5), Float.parseFloat(info.get(6)), Float.parseFloat(info.get(7)), Float.parseFloat(info.get(8)),
                Float.parseFloat(info.get(9)), Float.parseFloat(info.get(10)), (int) Float.parseFloat(info.get(11)), Float.parseFloat(info.get(12)));
        Thread thread = new Thread(company);
        thread.start();
        this.getAssets().put(company.getId(), company);
        addToIndex(company); //todo!
        return company;
    }

    private void createCompanies() throws FileNotFoundException {
        for (int i = 0; i < 3; i ++) {
            addAsset();
        }
    }

    @Override
    public void printOut(){
        System.out.println("Companies available on the market " + this.getName() + ":");
        for (String id : this.getAssets().keySet()) {
            this.getAssets().get(id).printOut();
        }
    }

    @Override
    public String description() {
        return "country:\t" + getCountry() +
                "\ncity:\t" + getCity() +
                "\naddres:\t" + getAddress() +
                "\n% margin:\t" + getPercentMargin() +
                "\ntrading currency:\t" + tradingCurrency;
    }
}
