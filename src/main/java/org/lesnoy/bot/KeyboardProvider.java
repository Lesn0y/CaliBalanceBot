package org.lesnoy.bot;

import org.jetbrains.annotations.NotNull;
import org.lesnoy.product.Product;
import org.lesnoy.product.ProductOption;
import org.lesnoy.product.ProductService;
import org.lesnoy.product.ProductType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class KeyboardProvider {

    private static final String buttons = "buttons";

    public static ReplyKeyboardMarkup getDefaultKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(ResourceBundle.getBundle(buttons).getString("main_menu_1"));
        keyboardRows.add(keyboardRow1);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(ResourceBundle.getBundle(buttons).getString("main_menu_2"));
        keyboardRows.add(keyboardRow2);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(ResourceBundle.getBundle(buttons).getString("main_menu_3"));
        keyboardRows.add(keyboardRow3);

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(ResourceBundle.getBundle(buttons).getString("main_menu_4"));
        keyboardRows.add(keyboardRow4);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    public static InlineKeyboardMarkup getInlineKeyboardWithProductMenu() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton(ResourceBundle.getBundle(buttons).getString("product_menu_1"));
        button1.setCallbackData(ProductOption.ADD_PRODUCT.name());
        InlineKeyboardButton button2 = new InlineKeyboardButton(ResourceBundle.getBundle(buttons).getString("product_menu_2"));
        button2.setCallbackData(ProductOption.OWN_PRODUCTS.name());
        InlineKeyboardButton button3 = new InlineKeyboardButton(ResourceBundle.getBundle(buttons).getString("product_menu_3"));
        button3.setCallbackData(ProductOption.ALL_PRODUCTS.name());
        InlineKeyboardButton button4 = new InlineKeyboardButton(ResourceBundle.getBundle(buttons).getString("menu_back"));
        button4.setCallbackData(ProductOption.EXIT.name());

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    @NotNull
    public static ReplyKeyboardMarkup getReplyKeyboardWithButtons(String... buttons) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.addAll(List.of(buttons));

        keyboardRows.add(keyboardRow);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    public static InlineKeyboardMarkup getProductTypeInlineKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (ProductType type : ProductType.values()) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(type.getName());
            button.setCallbackData(String.valueOf(type.ordinal()));
            row.add(button);
            rows.add(row);
        }

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public static InlineKeyboardMarkup getInlineKeyboardWithProductsInfo(List<Product> products) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Product product : products) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(ProductService.getProductInfo(product));
            button.setCallbackData(String.valueOf(product.getId()));
            row.add(button);
            rows.add(row);
        }

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(ResourceBundle.getBundle(buttons).getString("menu_back"));
        button.setCallbackData(String.valueOf(-1));
        row.add(button);
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
