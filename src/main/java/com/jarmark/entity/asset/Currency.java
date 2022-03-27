package com.jarmark.entity.asset;

import javafx.scene.chart.XYChart;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;

/**
 * currency class
 */
public class Currency extends Asset implements Runnable{
    private float buyRate;
    private float sellRate;
    private String validCountries;

    public Currency(String name, String id, float buyRate, float sellRate, String validCountries) {
        super(name, id);
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.setInitialRate(sellRate);
        this.addQuantity(10000);
        this.validCountries = validCountries;
        priceHistory.setName(getName() + " price in time");
    }


    public float getBuyRate() {
        return buyRate;
    }
    public float getSellRate() {
        return sellRate;
    }
    @Override
    public void addVolume(float sale) {}
    @Override
    public float availableToBuy() {
        return getQuantity() * buyRate;
    }
    @Override
    public String printOut(){
        return getId() + " " + getQuantity() + " " + getName() + " valid in " + validCountries;
    }

    @Override
    public String description() {
        return "id:\t" + getId() +
                "\nbuy rate:\t" + buyRate +
                "\nsell rate:\t" + sellRate +
                "\nvalid in:\t" + validCountries;
    }

    private synchronized void fluctuate() throws InterruptedException {
        Random random = new Random();
        float min = 0.1F;
        float max = 0.2F;
        this.sellRate += min + random.nextFloat() * (max - min) * sellRate;
        this.buyRate += min + random.nextFloat() * (max - min) * buyRate;
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        this.priceHistory.getData().add(new XYChart.Data<>(String.valueOf(timeSeconds), this.sellRate));
        this.percentageHistory.getData().add(new XYChart.Data<>(String.valueOf(timeSeconds), this.sellRate/getInitialRate()));
        sleep(1000);
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
