package com.jarmark.entity.asset;

import javafx.scene.chart.XYChart;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;

/**
 * Commodity class
 */
public class Commodity extends NonCurrency implements Runnable{
    private String tradingUnit;
    private String tradingCurrency;

    public Commodity(String name, String id, float currentPrice, float minPrice, float maxPrice, String tradingUnit, String tradingCurrency) {
        super(name, id, currentPrice, minPrice, maxPrice);
        this.tradingUnit = tradingUnit;
        this.tradingCurrency = tradingCurrency;
        priceHistory.setName(getName() + " price in time");
        setInitialRate(currentPrice);
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
    }

    @Override
    public String description() {
        return super.description() +
                "\ntrading unit:\t" + tradingUnit +
                "\ntrading currency:\t" + tradingCurrency;
    }

    @Override
    public String printOut(){
        return getId() + " " + getQuantity() + " " + getName();
    }

    public synchronized void fluctuate() throws InterruptedException {
        this.setCurrentPrice((float) (this.getCurrentPrice() + 0.01 * this.getCurrentPrice()));
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        this.priceHistory.getData().add(new XYChart.Data<>(String.valueOf(timeSeconds), this.getCurrentPrice()));
        this.percentageHistory.getData().add(new XYChart.Data<>(String.valueOf(timeSeconds), this.getCurrentPrice()/getInitialRate()));
        sleep(1000); //todo getWaitingTime()
    }

    @Override
    public synchronized void run() {
        while(true) {
            try {
                fluctuate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
