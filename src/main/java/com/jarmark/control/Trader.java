package com.jarmark.control;

import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.investor.InvestmentFund;
import com.jarmark.entity.investor.Investor;
import com.jarmark.entity.market.Market;
import java.util.ArrayList;
import java.util.Random;

/**
 * class which controls trade operations, intermediary between investor and market
 */
public class Trader<T extends Investor> {

    /**
     * helper function to check whether necessary conditions
     * to buy an asset are met
     * @param investor investor
     * @param market market
     * @param asset aset
     * @param ratio ratio
     * @return true or false
     */
    private boolean checkBuy(T investor, Market<Asset> market, Asset asset, float ratio) {
        Random random = new Random();
        return (investor.getInvestmentBudget() > market.getPercentMargin()) && (random.nextInt(100) < ratio)
                && asset.availableToBuy() > 0;
    }

    /**
     * helper function to check whether necessary conditions
     * to sell an asset are met
     * @param investor investor
     * @param market market
     * @param ratio ratio
     * @param id id
     * @return true or false
     */
    private boolean checkSell(T investor, Market<Asset> market, float ratio, String id) {
        Random random = new Random();
        return ((investor.getInvestmentBudget() > market.getPercentMargin()) && investor.getWallet().containsKey(id))
                && ((investor.getWallet().get(id) > 0) && (random.nextInt(100) > ratio));
    }

    /**
     * perform trade operation of one investor(private or fund) on one market
     * @param investor investor trading
     * @param market market involved in trading
     * @param bearBullRatio ratio of buying and selling assets
     * @param fund fund through which private investor trades, null otherwise
     * @return transaction log
     */
    public ArrayList<String> trade(T investor, Market<Asset> market, float bearBullRatio, InvestmentFund fund) {
        ArrayList<String> transactionHistory = new ArrayList<>();

        for (String id : market.getAssets().keySet()) {
            Asset currentAsset = market.getAssets().get(id);
            // buy currency - add to wallet and remove from market
            if (checkBuy(investor, market, currentAsset, bearBullRatio)) {
                investor.setInvestmentBudget(-market.getPercentMargin());
                String log;
                synchronized (market) {
                    if (fund == null) {
                        log = investor.buyAsset(currentAsset, null);
                    } else {
                        log = investor.buyAssetThrough(currentAsset, fund);
                    }
                }
                if (!log.isEmpty()) {
                    transactionHistory.add(0, log);
                }
            }
            // sell currency - remove from wallet, add to market
            if (checkSell(investor, market, bearBullRatio, id)) {
                investor.setInvestmentBudget(-market.getPercentMargin());
                String log;
                synchronized (market) {
                    if (fund == null) {
                        log = investor.sellAsset(currentAsset, null);
                    } else {
                        log = investor.sellAssetThrough(currentAsset, fund);
                    }
                }
                if (!log.isEmpty()) {
                    transactionHistory.add(0, log);
                }
            }
        }
            return transactionHistory;
    }
}
