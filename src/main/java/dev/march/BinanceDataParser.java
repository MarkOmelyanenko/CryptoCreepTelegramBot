package dev.march;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BinanceDataParser {

    private Gson gson = new Gson();

    public Ticker[] parse(String json) {
        return gson.fromJson(json, Ticker[].class);
    }

    public String findPrice(String json, String symbol) {
        Type type = new TypeToken<List<Ticker>>(){}.getType();
        List<Ticker> tickers = gson.fromJson(json, type);

        String price = "";

        for (Ticker ticker : tickers) {
            if (ticker.symbol.equals(symbol)) {
                // System.out.println(ticker.symbol + " " + ticker.price);
                price = ticker.price;
            }
        }

        return price;
    }
}
