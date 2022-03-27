package com.jarmark.entity.asset;

import java.util.ArrayList;

/**
 * class for stock market index
 */
public class StockMarketIndex {
    private String name;
    private float indexValue; //sum of share values of all the companies listed in a given index IPO? OK
    private float lowerThreshold;
    private float upperThreshold;
    private ArrayList<String> companiesNames;

    public StockMarketIndex(String name, float lowerThreshold, float upperThreshold) {
        this.name = name;
        this.indexValue = 0;
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
        this.companiesNames = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean checkThreshold(float shareValue) {
        return shareValue > lowerThreshold && shareValue < upperThreshold;
    }

    public void add(Company company) {
        companiesNames.add(company.getName());
        indexValue += company.getIpoShareValue();
    }
}
