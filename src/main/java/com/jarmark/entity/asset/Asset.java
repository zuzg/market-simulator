package com.jarmark.entity.asset;

import javafx.scene.chart.XYChart;

/**
 * Asset class
 */
public abstract class Asset implements Runnable{
    private String name;
    private String id;
    private float quantity;
    private float initialRate;
    public XYChart.Series<String, Number> priceHistory = new XYChart.Series<>();
    public XYChart.Series<String, Number> percentageHistory = new XYChart.Series<>();

    public Asset(String name, String id) {
        this.name = name;
        this.id = id;
        this.quantity = 1000;
        percentageHistory.setName(getName());
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public float getQuantity() {
        return quantity;
    }
    public void addQuantity(float quantity) {
        this.quantity += quantity;
    }
    public float getInitialRate() {
        return initialRate;
    }
    public void setInitialRate(float initialRate) {
        this.initialRate = initialRate;
    }

    /**
     * returns value in base currency of given asset available to buy
     * @return value available to buy
     */
    public abstract float availableToBuy();

    /**
     * return current buy rate of an asset
     * @return buy rate
     */
    public abstract float getBuyRate();

    /**
     * return current sell rate of an asset
     * @return sell rate
     */
    public abstract float getSellRate();

    /**
     * add one to the number of times equity was bought or sold
     * and value of sale to total sales
     * @param sale value of sale
     */
    public abstract void addVolume(float sale);

    /**
     * gives description of an asset
     * @return string of description
     */
    public abstract String printOut();

    /**
     * gives description of an asset
     * @return string of description
     */
    public abstract String description();
}
