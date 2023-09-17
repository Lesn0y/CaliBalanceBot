package org.lesnoy;

import org.apache.shiro.session.Session;
import org.jetbrains.annotations.NotNull;
import org.lesnoy.bot.SessionAttribute;
import org.lesnoy.bot.TgRequest;
import org.lesnoy.product.ProductOption;
import org.lesnoy.product.ProductService;
import org.lesnoy.user.UserDTO;
import org.lesnoy.user.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaliBalanceBot extends TelegramLongPollingSessionBot {

    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();


    public CaliBalanceBot(String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return "CaliBalanceBot";
    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optionalSession) {
        Session session = optionalSession.get();
        SendMessage response;

        if (update.hasMessage() && update.getMessage().hasText()) {

            String username = update.getMessage().getFrom().getUserName();
            String request = update.getMessage().getText();
            TgRequest tgRequest = new TgRequest(request, username, session);

            if (session.getAttribute("command") != null) {
                response = switch (SessionAttribute.valueOf(session.getAttribute("command").toString())) {
                    case NEW_USER -> userService.register(tgRequest);
                    case PRODUCT -> productService.getResponse(tgRequest);
                };
            } else {
                response = getDefaultMenuResponse(tgRequest);
            }

            response.setChatId(update.getMessage().getChatId().toString());
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            if (session.getAttribute("command") != null) {

                String username = update.getCallbackQuery().getMessage().getFrom().getUserName();
                String request = update.getCallbackQuery().getData();
                TgRequest tgRequest = new TgRequest(request, username, session);

                response = switch (ProductOption.valueOf(request)) {
                    case ALL_PRODUCTS -> productService.getResponse(tgRequest);
                    case OWN_PRODUCTS -> productService.getResponse(tgRequest);
                    case ADD_PRODUCT -> productService.getResponse(tgRequest);
                };
            } else {
                response = new SendMessage();
                response.setText("Непредвиденная ситуация 😄");
            }

            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage getDefaultMenuResponse(TgRequest tgRequest) {
        SendMessage response = new SendMessage();
        switch (tgRequest.request()) {
            case "/start" -> {
                tgRequest.session().setAttribute("new_user", new UserDTO(tgRequest.username()));
                tgRequest.session().setAttribute("command", SessionAttribute.NEW_USER);
                response.setText("Я помогу вам расчитать опитмальную диету для вашей цели. Для начала работы нужно заполнить анкету");
                response.setReplyMarkup(getReplyKeyboardWithButtons("Продолжить"));
            }
            case "Остаток КБЖУ" -> response.setText(userService.getUserCaloriesInfo(tgRequest.username()));
            case "Меню продуктов" -> {
                tgRequest.session().setAttribute("command", SessionAttribute.PRODUCT);
                response.setText("Меню продуктов:");
                response.setReplyMarkup(getInlineKeyboardWithProductMenu());
            }
            case "Суточное КБЖУ" -> response.setText(userService.getUserCaloriesInfo(tgRequest.username()));
            case "test" -> response.setText("Ещё в разработке");
            default -> response.setText("It is what it is");
        }
        return response;
    }

    private InlineKeyboardMarkup getInlineKeyboardWithProductMenu() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton("Добавить новый продукт");
        button1.setCallbackData(ProductOption.ADD_PRODUCT.name());
        InlineKeyboardButton button2 = new InlineKeyboardButton("Посмотреть список своих продуктов");
        button2.setCallbackData(ProductOption.OWN_PRODUCTS.name());
        InlineKeyboardButton button3 = new InlineKeyboardButton("Просмотреть список всех продуктов");
        button3.setCallbackData(ProductOption.ALL_PRODUCTS.name());

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);


        keyboard.setKeyboard(rows);
        return keyboard;
    }

    @NotNull
    private ReplyKeyboardMarkup getReplyKeyboardWithButtons(String... buttons) {
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

    private ReplyKeyboardMarkup initDefaultKeyboard() {
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

        KeyboardRow keyboardRow5 = new KeyboardRow();
        keyboardRow5.add("test");
        keyboardRows.add(keyboardRow4);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }
}
