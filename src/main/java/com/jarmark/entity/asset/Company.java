package com.jarmark.entity.asset;

import javafx.scene.chart.XYChart;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;

/**
 * company class
 */
public class Company extends NonCurrency implements Runnable{
    private String ipoDate;
    private float ipoShareValue;
    private float openingPrice;
    private float profit;
    private float revenue;
    private float capital;
    private int tradingVolume;
    private float totalSales;
    private boolean exit = false;
    private ArrayList<String> stockMarketIndices;

    public Company(String name, String id, float currentPrice, float minPrice, float maxPrice, String ipoDate, float ipoShareValue,
                   float openingPrice, float profit, float revenue, float capital, int tradingVolume, float totalSales) {
        super(name, id, currentPrice, minPrice, maxPrice);
        this.ipoDate = ipoDate;
        this.ipoShareValue = ipoShareValue;
        this.openingPrice = openingPrice;
        this.profit = profit;
        this.revenue = revenue;
        this.capital = capital;
        this.tradingVolume = tradingVolume;
        this.totalSales = totalSales;
        this.stockMarketIndices = new ArrayList<>();
    }

    public float getIpoShareValue() {
        return ipoShareValue;
    }
    public void addStockMarketIndex(StockMarketIndex smi) {
        this.stockMarketIndices.add(smi.getName());
    }
    @Override
    public float availableToBuy() {
        return super.availableToBuy();
    }
    @Override
    public float getBuyRate() {
        return super.getBuyRate();
    }
    @Override
    public float getSellRate() {
        return super.getSellRate();
    }
    @Override
    public void addVolume(float sale) {
        this.tradingVolume += 1;
        this.totalSales += sale;
    }

    private void randomlyIncreaseShares(){
        Random random = new Random();
        if(random.nextInt(1000) == 23) {
            this.addQuantity(100);
        }
    }

    private synchronized void fluctuate() throws InterruptedException {
        Random random = new Random();
        float min = -2F;
        float max = 0.1F;
        this.revenue += min + random.nextFloat() * (max - min) * revenue;
        this.profit = revenue * 0.8F; // after expanses etc
        this.setCurrentPrice(min + random.nextFloat() * (max - min) * getCurrentPrice() + getCurrentPrice());
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        this.priceHistory.getData().add(new XYChart.Data<>(String.valueOf(timeSeconds), this.getCurrentPrice()));
        this.percentageHistory.getData().add(new XYChart.Data<>(String.valueOf(timeSeconds), this.getCurrentPrice()/openingPrice));
        sleep(1000);

    }

    /**
     * generate profit and revenue in time intervals, fluctuate (price)
     */
    @Override
    public synchronized void run() {
        while(!exit) {
            try {
                fluctuate();
                randomlyIncreaseShares();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String printOut(){
        return getId() + " " + getQuantity() + " " + getName()+ " " + stockMarketIndices.toString();
    }
    @Override
    public String description() {
        return super.description() +
                "\nIPO share value:\t" + ipoShareValue +
                "\nIPO date:\t" + ipoDate +
                "\nopening price:\t" + openingPrice +
                "\nrevenue:\t" + revenue +
                "\nprofit:\t" + profit +
                "\ncapital:\t" + capital +
                "\ntrading volume:\t" + tradingVolume +
                "\ntotal sales:\t" + totalSales +
                "\nstock quantity:\t" + getQuantity() +
                "\nstock market indices:\t" + stockMarketIndices.toString();
    }
}
