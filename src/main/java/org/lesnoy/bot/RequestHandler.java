package org.lesnoy.bot;

import org.apache.shiro.session.Session;
import org.lesnoy.exeptions.WebApiExeption;
import org.lesnoy.product.ProductService;
import org.lesnoy.user.UserDTO;
import org.lesnoy.user.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.lesnoy.bot.KeyboardProvider.*;

public class RequestHandler {

    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    public SendMessage handleMessage(String username, String request, Session session) {
        SendMessage response;
        if (session.getAttribute("command") != null) {
            return switch (SessionAttribute.valueOf(session.getAttribute("command").toString())) {
                case NEW_USER -> userService.register(request, session);
                case PRODUCT -> {
                    if (session.getAttribute("productOption") != null) {
                        try {
                            yield productService.saveProduct(username, request, session);
                        } catch (WebApiExeption e) {
                            response = new SendMessage();
                            response.setText(e.getMessage());
                            yield response;
                        }
                    } else {
                        response = new SendMessage();
                        response.setText("Ты что жмав? А ну повтори");
                        yield response;
                    }
                }
            };
        } else {
            response = new SendMessage();
            switch (request) {
                case "/start" -> {
                    session.setAttribute("new_user", new UserDTO(username));
                    session.setAttribute("command", SessionAttribute.NEW_USER);
                    response.setText("Я помогу вам расчитать опитмальную диету для вашей цели. Для начала работы нужно заполнить анкету");
                    response.setReplyMarkup(getReplyKeyboardWithButtons("Продолжить"));
                }
                case "Меню продуктов" -> {
                    session.setAttribute("command", SessionAttribute.PRODUCT);
                    response.setText("Меню продуктов:");
                    response.setReplyMarkup(getInlineKeyboardWithProductMenu());
                }
                case "Остаток КБЖУ" -> response.setText(userService.getUserCaloriesInfo(username));
                case "Суточное КБЖУ" -> response.setText(userService.getActualUserCaloriesInfo(username));
                default -> {
                    response.setText("It is what it is");
                    response.setReplyMarkup(getDefaultKeyboard());
                }
            }
            return response;
        }
    }

    public SendMessage handleCallback(String username, String request, Session session) {
        SendMessage response;
        Object optionAttribute = session.getAttribute("productOption");
        if (optionAttribute != null) {
            try {
                response = productService.getResponseByAttribute(username, request, session, optionAttribute.toString());
            } catch (WebApiExeption e) {
                response = new SendMessage();
                response.setText(e.getMessage());
                response.setReplyMarkup(getInlineKeyboardWithProductMenu());
            }
        } else if (request.equals("exit")) {
            session.removeAttribute("command");
            response = new SendMessage();
            response.setText("Главное меню");
            response.setReplyMarkup(getDefaultKeyboard());
        }
        else if (request.equals("menu")) {
            response = new SendMessage();
            response.setText("Меню продуктов");
            response.setReplyMarkup(getInlineKeyboardWithProductMenu());
        } else {
            response = productService.getResponse(request, session);
        }
        return response;
    }
}
