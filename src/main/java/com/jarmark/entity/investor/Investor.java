package com.jarmark.entity.investor;

import com.jarmark.control.World;
import com.jarmark.entity.asset.Asset;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;

/**
 * abstract class for investors entity
 */
public abstract class Investor extends Thread {
    private String firstName;
    private String lastName;
    private int tradingID;
    private float investmentBudget; // in world-universal rate, other stored in wallet
    private HashMap<String, Float> wallet;
    private World world;

    public Investor(String firstName, String lastName, int tradingID, float investmentBudget) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tradingID = tradingID;
        this.investmentBudget = investmentBudget;
        this.wallet = new HashMap<>();
    }

    public void setInvestmentBudget(float toAdd) {
        this.investmentBudget += toAdd;
    }
    public float getInvestmentBudget() {
        return investmentBudget;
    }
    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUniqueName() {
        return firstName + " " + lastName;
    }
    public HashMap<String, Float> getWallet() {
        return wallet;
    }

    protected void randomlyIncreaseBudget() {
        Random random = new Random();
        if(random.nextInt(1000) == 999) {
            this.investmentBudget *= 100;
        }
    }

    /**
     * perform direct buy transaction of an asset
     * @param asset asset to buy
     * @param fund not taken into account, since it is direct operation
     * @return transaction log
     */
    public String buyAsset(Asset asset, InvestmentFund fund) {
        Random random = new Random();
        String assetId = asset.getId();
        float toSpend = random.nextInt((int) Math.min(this.investmentBudget, asset.availableToBuy())); // if toSpend is more than on market, buy everything
        float toAdd = toSpend / asset.getBuyRate();
        if (wallet.containsKey(assetId)) {
            wallet.put(assetId, wallet.get(assetId) + toAdd);
        } else {
            wallet.put(assetId, toAdd);
        }
        asset.addQuantity(-toAdd);
        asset.addVolume(toSpend); //increase number of transactions and total sales
        setInvestmentBudget(-toSpend);
        return String.format("%-20s%-20s%-25s%-25s", LocalTime.now().truncatedTo(ChronoUnit.MILLIS), asset.getId(), asset.getBuyRate(), "+"+toAdd);
    }

    /**
     * perform direct sell transaction of an asset
     * @param asset asset to sell
     * @param fund not taken into account, since it is direct operation
     * @return transaction log
     */
    public String sellAsset(Asset asset, InvestmentFund fund) {
        String currencyID = asset.getId();
        float toSell = wallet.get(currencyID);
        float toAdd = toSell * asset.getSellRate();
        asset.addQuantity(toSell);
        wallet.put(currencyID, 0F);
        asset.addVolume(toAdd);
        setInvestmentBudget(toAdd);
        return String.format("%-20s%-20s%-25s%-25s", LocalTime.now().truncatedTo(ChronoUnit.MILLIS), asset.getId(), asset.getSellRate(), "-"+toAdd);
    }

    /**
     * give description of an investor
     * @return string of information
     */
    public abstract String description();
    public abstract String buyAssetThrough(Asset currentAsset, InvestmentFund fund);
    public abstract String sellAssetThrough(Asset currentAsset, InvestmentFund fund);
    @Override
    public abstract void run();
    public void printOut(){
        System.out.println("Investor " + firstName + " " + lastName + " has:");
        System.out.println(investmentBudget + " base rate");
        for (String i : wallet.keySet()) {
            System.out.println(wallet.get(i) + i);
        }
    }
}
