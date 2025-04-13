package dev.march;

import dev.march.keyboards.ActionsKeyboardMarkup;
import dev.march.keyboards.FrequencyKeyboardMarkup;
import dev.march.keyboards.PairKeyboardMarkup;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    String botUsername = System.getenv("BOT_NAME");
    String botToken = System.getenv("BOT_TOKEN");

    BinanceConnector connector = new BinanceConnector();
    BinanceDataParser parser = new BinanceDataParser();

    private final Map<Long, String> userState = new HashMap<>();
    private final Map<Long, String> userSelectedPair = new HashMap<>();

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chatId));

        switch (text) {
            case "/start":
            case "\uD83D\uDD19 Back":
                message.setText("What do you want to do?");
                message.setReplyMarkup(ActionsKeyboardMarkup.getActionsKeyboard());
                userState.remove(chatId);
                break;
            case "Get crypto price":
                userState.put(chatId, "ONCE");
                message.setText("Which pair?");
                message.setReplyMarkup(PairKeyboardMarkup.getPairKeyboard());
                break;
            case "Send every N seconds/minutes":
                userState.put(chatId, "PERIODIC");
                message.setText("Which pair?");
                message.setReplyMarkup(PairKeyboardMarkup.getPairKeyboard());
                break;
            case "ETH | USDC":
            case "BTC | USDC":
            case "ETH | BTC":
                if ("ONCE".equals(userState.get(chatId))) {
                    String price = parser.findPrice(
                            connector.safeSendRequest("/api/v3/ticker/price"),
                            text.replace(" | ", "")
                    );
                    message.setText("Price for " + text + ": " + price);
                } else if ("PERIODIC".equals(userState.get(chatId))) {
                    userSelectedPair.put(chatId, text);
                    message.setText("How often?");
                    message.setReplyMarkup(FrequencyKeyboardMarkup.getFrequencyKeyboard());
                } else {
                    message.setText("Please choose an action from the menu.");
                }
                break;
            case "15 seconds":
            case "30 seconds":
            case "1 minute":
            case "5 minutes":
            case "15 minutes":
            case "30 minutes":
            case "60 minutes":
                if ("PERIODIC".equals(userState.get(chatId))) {
                    long interval = switch (text) {
                        case "15 seconds" -> 15000;
                        case "30 seconds" -> 30000;
                        case "1 minute" -> 60000;
                        case "5 minutes" -> 300000;
                        case "15 minutes" -> 900000;
                        case "30 minutes" -> 1800000;
                        case "60 minutes" -> 3600000;
                        default -> 0;
                    };
                    startSendingPeriodically(chatId, userSelectedPair.get(chatId), interval);
                    message.setText("Started sending price every " + text);
                    message.setReplyMarkup(FrequencyKeyboardMarkup.getFrequencyKeyboard());

                    userState.remove(chatId);
                    userSelectedPair.remove(chatId);
                } else {
                    message.setText("Please first select a crypto pair.");
                }
                break;
            case "\uD83D\uDED1 Stop":
                stopSending(chatId);
                message.setText("Stopped.");
                message.setReplyMarkup(ActionsKeyboardMarkup.getActionsKeyboard());
                break;
            default:
                message.setText("Unknown command. Try /start");
        }

        sendMessage(message);
    }

    private final Map<Long, Thread> userThreads = new HashMap<>();

    private void startSendingPeriodically(long chatId, String symbol, long interval) {
        stopSending(chatId);

        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String price = parser.findPrice(
                            connector.safeSendRequest("/api/v3/ticker/price"), symbol.replace(" | ", "")
                    );
                    SendMessage message = new SendMessage();
                    message.setChatId(Long.toString(chatId));
                    message.setText("Price for " + symbol + ": " + price);
                    sendMessage(message);
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        userThreads.put(chatId, thread);
        thread.start();
    }

    public void stopSending(long chatId) {
        Thread thread = userThreads.get(chatId);
        if (thread != null) {
            thread.interrupt();
            userThreads.remove(chatId);
        }
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}