package com.jarmark.entity.asset;

/**
 * parent class for non currency assets
 */
public abstract class NonCurrency extends Asset implements Runnable{
    private float currentPrice;
    private float minPrice;
    private float maxPrice;

    public NonCurrency(String name, String id, float currentPrice, float minPrice, float maxPrice) {
        super(name, id);
        this.currentPrice = currentPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }
    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }
    @Override
    public float availableToBuy() {
        return getQuantity() * getCurrentPrice();
    }
    @Override
    public float getBuyRate() {
        return getCurrentPrice();
    }
    @Override
    public float getSellRate() {
        return getCurrentPrice();
    }
    @Override
    public String printOut(){
        return null;
    }

    @Override
    public String description() {
        return "id:\t" + getId() +
                "\ncurrent price:\t" + currentPrice +
                "\nmin price:\t" + minPrice +
                "\nmax price:\t" + maxPrice;
    }
}
