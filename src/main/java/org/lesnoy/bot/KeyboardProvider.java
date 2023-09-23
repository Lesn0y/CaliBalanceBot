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

public class KeyboardProvider {
    
    public static ReplyKeyboardMarkup getDefaultKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Остаток КБЖУ");
        keyboardRows.add(keyboardRow1);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("Меню продуктов");
        keyboardRows.add(keyboardRow2);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add("Добавить прием пищи");
        keyboardRows.add(keyboardRow3);

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add("Суточное КБЖУ");
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

        InlineKeyboardButton button1 = new InlineKeyboardButton("Добавить новый продукт");
        button1.setCallbackData(ProductOption.ADD_PRODUCT.name());
        InlineKeyboardButton button2 = new InlineKeyboardButton("Посмотреть список своих продуктов");
        button2.setCallbackData(ProductOption.OWN_PRODUCTS.name());
        InlineKeyboardButton button3 = new InlineKeyboardButton("Просмотреть список всех продуктов");
        button3.setCallbackData(ProductOption.ALL_PRODUCTS.name());
        InlineKeyboardButton button4 = new InlineKeyboardButton("Назад");
        button4.setCallbackData("exit");

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

        for (String button : buttons) {
            keyboardRow.add(button);
        }

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
        button.setText("Назад");
        button.setCallbackData("menu");
        row.add(button);
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
