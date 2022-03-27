package com.jarmark.entity.investor;

import com.jarmark.control.Trader;
import com.jarmark.control.World;
import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.market.Market;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

/**
 * private investor (i.e. one person) class
 */
public class PrivateInvestor extends Investor {

    public PrivateInvestor(String firstName, String lastName, int tradingID, float investmentBudget) {
        super(firstName, lastName, tradingID, investmentBudget);
    }

    /**
     * perform buy transaction through an investment fund
     * @param asset asset to buy
     * @return transaction log
     */
    public String buyAssetThrough(Asset asset, InvestmentFund fund) {
        Random random = new Random();
        String assetId = asset.getId();
        float toSpend = random.nextInt((int) Math.min(this.getInvestmentBudget(), asset.availableToBuy())); // if toSpend is more than on market, buy everything

        float quantityBought = fund.buyForInvestor(toSpend, asset);
        setInvestmentBudget(-toSpend);
        if (getWallet().containsKey(assetId)) {
            getWallet().put(assetId, getWallet().get(assetId) + quantityBought);
        } else {
            getWallet().put(assetId, quantityBought);
        }
        return String.format("%-20s%-20s%-25s%-25s", LocalTime.now().truncatedTo(ChronoUnit.MILLIS), asset.getId(), asset.getBuyRate(), "+"+quantityBought);
    }

    /**
     * perform sell transaction of an asset
     * @param asset asset to sell
     * @return transaction log
     */
    public String sellAssetThrough(Asset asset,  InvestmentFund fund) {
        String currencyID = asset.getId();
        float toSell = getWallet().get(currencyID);
        float toAdd = fund.sellForInvestor(toSell, asset);
        getWallet().put(currencyID, 0F);
        setInvestmentBudget(toAdd);
        return String.format("%-20s%-20s%-25s%-25s", LocalTime.now().truncatedTo(ChronoUnit.MILLIS), asset.getId(), asset.getSellRate(), "-"+toAdd);
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
            // investors are more likely to buy individually than through funds
            if (random.nextInt() % 3 == 0 && world.fundsNames.size() != 0 ) {
                index = random.nextInt(world.fundsNames.size());
                InvestmentFund randomFund = (InvestmentFund) world.investors.get(world.fundsNames.get(index));
                ArrayList<String> log = trader.trade(this, marketsList.get(index), world.getBearBullRatio(), randomFund);
                world.transactions.addAll(log);
            }
            else {
                ArrayList<String> log = trader.trade(this, marketsList.get(index), world.getBearBullRatio(), null);
                world.transactions.addAll(log);
            }
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
        return getUniqueName() + " has " + getInvestmentBudget() + " money";
    }

}
