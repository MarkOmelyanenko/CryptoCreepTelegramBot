package dev.march;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        botsApi.registerBot(new Bot());

        /*
        try {
            String response = connector.safeSendRequest("/api/v3/ticker/price");
            TickerPrice[] prices = parser.parse(response);

            for (TickerPrice price : prices) {
                System.out.printf("Symbol: %s, Price: %s%n", price.symbol, price.price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
}