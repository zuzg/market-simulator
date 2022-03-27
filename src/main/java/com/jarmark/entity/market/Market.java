package com.jarmark.entity.market;

import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.creator.EntityCreator;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * abstract class to gather all the markets
 * @param <T> type of asset: currency, commodity or company (stock)
 */
public abstract class Market<T extends Asset> {
    private EntityCreator ec;
    private String name;
    private String country;
    private String city;
    private String address;
    private float percentMargin;
    private HashMap<String, T> assets;

    public Market(EntityCreator ec, String name, String country, String city, String address, float percentMargin) {
        this.ec = ec;
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.percentMargin = percentMargin;
        this.assets = new HashMap<>();
    }

    public HashMap<String, T> getAssets() {
        return assets;
    }
    public EntityCreator getEc() {
        return ec;
    }
    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
    public String getAddress() {
        return address;
    }
    public float getPercentMargin() {
        return percentMargin;
    }
    public void printOut(){
    };

    /**
     * gives a detailed description of a market
     * @return string of description
     */
    public abstract String description();
    public abstract Asset addAsset() throws InterruptedException, FileNotFoundException;
}
