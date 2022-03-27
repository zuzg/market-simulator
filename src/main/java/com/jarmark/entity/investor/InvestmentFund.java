package com.jarmark.entity.investor;

import com.jarmark.control.Trader;
import com.jarmark.control.World;
import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.market.Market;
import java.util.ArrayList;
import java.util.Random;

/**
 * class for an investment fund, maintained by an owner
 */
public class InvestmentFund extends Investor {
    private String fundName;

    public InvestmentFund(String firstName, String lastName, int tradingID, int investmentBudget, String fundName) {
        super(firstName, lastName, tradingID, investmentBudget);
        this.fundName = fundName;
    }
    @Override
    public String getUniqueName() {
        return fundName;
    }

    protected float buyForInvestor(float toSpend, Asset asset) {
        float provision = 0.1F * toSpend;
        toSpend -= provision;
        this.setInvestmentBudget(provision);
        float toAdd = toSpend / asset.getBuyRate();
        asset.addQuantity(-toAdd);
        asset.addVolume(toSpend);
        return toAdd;
    }

    protected float sellForInvestor(float toSell, Asset asset) {
        float toAdd = toSell * asset.getSellRate();
        float provision = 0.1F * toAdd;
        toAdd -= provision;
        this.setInvestmentBudget(provision);
        asset.addQuantity(toSell);
        asset.addVolume(toAdd);
        return toAdd;
    }

    /**
     * repeatedly trade on a random market
     */
    @Override
    public void run() {
        Random random = new Random();
        World world = getWorld();
        Trader trader = new Trader();
        int index = random.nextInt(world.markets.size());
        ArrayList<Market> marketsList = new ArrayList<>(world.markets.values());
        while(this.getInvestmentBudget() > 10) {
            ArrayList<String> log = trader.trade(this, marketsList.get(index), world.getBearBullRatio(), null);
            world.transactions.addAll(log);
            this.randomlyIncreaseBudget();
            synchronized (this) {
                try {
                    wait(world.getWaitingTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String description() {
        return fundName + " owned by " +  getFirstName() + " " + getLastName() + " has " + getInvestmentBudget() + " money";
    }

    @Override
    public String buyAssetThrough(Asset currentAsset, InvestmentFund fund) {
        return null;
    }
    @Override
    public String sellAssetThrough(Asset currentAsset, InvestmentFund fund) {
        return null;
    }
}
