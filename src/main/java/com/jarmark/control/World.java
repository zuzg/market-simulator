package com.jarmark.control;

import com.jarmark.entity.asset.*;
import com.jarmark.entity.creator.EntityCreator;
import com.jarmark.entity.investor.Investor;
import com.jarmark.entity.investor.InvestmentFund;
import com.jarmark.entity.investor.PrivateInvestor;
import com.jarmark.entity.market.Market;
import com.jarmark.entity.market.CommodityMarket;
import com.jarmark.entity.market.CurrencyMarket;
import com.jarmark.entity.market.StockMarket;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * main class which gathers and connects all entity
 */
public class World {
    private int transactionsPerMin; // for each investor
    private int bearBullRatio;
    public HashMap<String, Investor> investors;
    public HashMap<String, Market> markets;
    public HashMap<String, Asset> allAssets;
    public ArrayList<String> transactions;
    public ArrayList<String> fundsNames;
    public EntityCreator ec;

    /**
     * constructor
     * @param transactionsPerMin number of transaction per one minute
     * @param bearBullRatio ratio of buy and sell transactions
     */
    public World(int transactionsPerMin, int bearBullRatio) {
        this.transactionsPerMin = transactionsPerMin;
        this.bearBullRatio = bearBullRatio;
        this.investors = new HashMap<>();
        this.markets = new HashMap<>();
        this.allAssets = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.fundsNames = new ArrayList<>();
        this.ec = new EntityCreator();
    }

    public float getBearBullRatio() {
        return bearBullRatio;
    }
    public void setBearBullRatio(int bearBullRatio) {
        this.bearBullRatio = bearBullRatio;
    }
    public int getTransactionsPerMin() {
        return transactionsPerMin;
    }
    public void setTransactionsPerMin(int transactionsPerMin) {
        this.transactionsPerMin = transactionsPerMin;
    }

    /**
     * calculates waiting time from transactionsPerMin
     * @return waiting time
     */
    public long getWaitingTime() {
        return 1 + this.transactionsPerMin / 60;
    }
    /**
     * creates private investor from csv file with data
     * @return new private investor
     * @throws FileNotFoundException when no specified file is found
     */
    public PrivateInvestor createPrivateInvestor() throws FileNotFoundException {
        List<String> info = ec.readFromCsv("private_investor_data.csv");
        PrivateInvestor investor = new PrivateInvestor(info.get(0), info.get(1), Integer.parseInt(info.get(2)), Integer.parseInt(info.get(3)));
        this.investors.put(investor.getUniqueName(), investor);
        return investor;
    }
    /**
     * creates investment fund from csv file with data
     * @return new investment fund
     * @throws FileNotFoundException when no specified file is found
     */
    public InvestmentFund createInvestmentFund() throws FileNotFoundException {
        List<String> info = ec.readFromCsv("investment_fund_data.csv");
        InvestmentFund investor = new InvestmentFund(info.get(0), info.get(1), Integer.parseInt(info.get(2)), Integer.parseInt(info.get(3)), info.get(4));
        this.investors.put(investor.getUniqueName(), investor);
        return investor;
    }
    /**
     * helper function to add created market to the world
     * @param market newly created market
     */
    private void addMarketToWorld(Market market) {
        this.markets.put(market.getName(), market);
        for (Object id : market.getAssets().keySet()) {
            allAssets.put((String)id, (Asset) market.getAssets().get(id));
        }
    }
    /**
     * creates random currency market
     * @return new currency market
     * @throws FileNotFoundException when no specified file is found
     */
    public CurrencyMarket createCurrencyMarket() throws FileNotFoundException {
        List<String> info = ec.readFromCsv("currency_market_data.csv");
        CurrencyMarket market = new CurrencyMarket(ec, info.get(0), info.get(1), info.get(2), info.get(3), Float.parseFloat(info.get(4)), info.get(5));
        addMarketToWorld(market);
        return market;
    }
    /**
     * creates random commodity market
     * @return new commodity market
     * @throws FileNotFoundException when no specified file is found
     */
    public CommodityMarket createCommodityMarket() throws FileNotFoundException {
        List<String> info = ec.readFromCsv("commodity_market_data.csv");
        CommodityMarket market = new CommodityMarket(ec, info.get(0), info.get(1), info.get(2), info.get(3), Float.parseFloat(info.get(4)));
        addMarketToWorld(market);
        return market;
    }
    /**
     * creates random stock market
     * @return new stock market
     * @throws FileNotFoundException when no specified file is found
     */
    public StockMarket createStockMarket() throws FileNotFoundException {
        List<String> info = ec.readFromCsv("stock_market_data.csv");
        StockMarket market = new StockMarket(ec, info.get(0), info.get(1), info.get(2), info.get(3), Float.parseFloat(info.get(4)), info.get(5));
        addMarketToWorld(market);
        return market;
    }
    /**
     * runs the whole simulation
     * @throws FileNotFoundException when no specified file is found
     */
    public synchronized void run() throws FileNotFoundException {
        for (Investor investor: this.investors.values()) {
            investor.setWorld(this);
            investor.start();
        }
    }
}
