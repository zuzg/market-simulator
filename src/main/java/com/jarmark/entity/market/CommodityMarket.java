package com.jarmark.entity.market;

import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.asset.Commodity;
import com.jarmark.entity.creator.EntityCreator;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * class for a commodity market, where commodities can be traded
 */
public class CommodityMarket extends Market<Commodity> {

    public CommodityMarket(EntityCreator ec, String name, String country, String city, String address, float percentMargin) throws FileNotFoundException {
        super(ec, name, country, city, address, percentMargin);
        this.createCommodities();
    }

    @Override
    public String description() {
        return "country:\t" + getCountry() +
                "\ncity:\t" + getCity() +
                "\naddress:\t" + getAddress() +
                "\n% margin:\t" + getPercentMargin();
    }

    @Override
    public Asset addAsset() throws FileNotFoundException {
        List<String> info = getEc().readFromCsv("commodity_data.csv");
        Commodity commodity = new Commodity(info.get(0), info.get(0).substring(0,3), Float.parseFloat(info.get(1)), Float.parseFloat(info.get(2)),
                Float.parseFloat(info.get(3)), info.get(4), info.get(5));
        Thread thread = new Thread(commodity);
        thread.start();
        this.getAssets().put(commodity.getId(), commodity);
        return commodity;
    }

    /**
     * create commodities and add them to market
     */
    public void createCommodities() throws FileNotFoundException {
        for (int i = 0; i < 3; i ++) {
            addAsset();
        }
    }

    @Override
    public void printOut(){
        System.out.println("Commodities available on the market " + this.getName() + ":");
        for (String id : this.getAssets().keySet()) {
            System.out.println(this.getAssets().get(id).printOut());
        }
    }

}
