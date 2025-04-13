package dev.march.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class FrequencyKeyboardMarkup {
    public static ReplyKeyboardMarkup getFrequencyKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("15 seconds"));
        row1.add(new KeyboardButton("30 seconds"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("1 minute"));
        row2.add(new KeyboardButton("5 minutes"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("15 minutes"));
        row3.add(new KeyboardButton("30 minutes"));

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton("60 minutes"));
        row4.add(new KeyboardButton("\uD83D\uDED1 Stop"));

        KeyboardRow row5 = new KeyboardRow();
        row5.add(new KeyboardButton("\uD83D\uDD19 Back"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true);

        return markup;
    }
}