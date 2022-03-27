package com.jarmark.entity.market;

import com.jarmark.entity.asset.Asset;
import com.jarmark.entity.asset.Currency;
import com.jarmark.entity.creator.EntityCreator;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * class for a currency market, where stocks can be traded
 */
public class CurrencyMarket extends Market<Currency> {
    private String baseCurrency;

    public CurrencyMarket(EntityCreator ec, String name, String country, String city, String address, float percentMargin, String baseCurrency) throws FileNotFoundException {
        super(ec, name, country, city, address, percentMargin);
        this.baseCurrency = baseCurrency;
        this.createCurrencies();
    }

    public Asset addAsset() throws FileNotFoundException {
        List<String> info = getEc().readFromCsv("currency_data.csv");
        Currency currency = new Currency(info.get(0), info.get(1), Float.parseFloat(info.get(2)), Float.parseFloat(info.get(3)), info.get(4));
        Thread thread = new Thread(currency);
        thread.start();
        this.getAssets().put(currency.getId(), currency);
        return currency;//todo
    }

    /**
     * creates initial currencies (3) and add them to the market
     */
    public void createCurrencies() throws FileNotFoundException {
        for (int i = 0; i < 3; i ++) {
            addAsset();
        }
    }

    @Override
    public void printOut() {
        System.out.println("Currencies available on the market " + this.getName() + ":");
        for (String id : this.getAssets().keySet()) {
            this.getAssets().get(id).printOut();
        }
    }

    @Override
    public String description() {
        return "country:\t" + getCountry() +
                "\ncity:\t" + getCity() +
                "\naddres:\t" + getAddress() +
                "\n% margin:\t" + getPercentMargin() +
                "\nbase currency:\t" + baseCurrency;
    }
}
