package org.lesnoy.bot;

import org.apache.shiro.session.Session;
import org.lesnoy.entry.EntryService;
import org.lesnoy.exeptions.WebApiExeption;
import org.lesnoy.product.Product;
import org.lesnoy.product.ProductOption;
import org.lesnoy.product.ProductService;
import org.lesnoy.user.User;
import org.lesnoy.user.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.ResourceBundle;

import static org.lesnoy.bot.KeyboardProvider.*;

public class RequestHandler {

    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final EntryService entryService = new EntryService();
    private final String messages = "messages";

    public SendMessage handleMessage(String username, String request, Session session) {
        SendMessage response = new SendMessage();
        Object sessionCommand = session.getAttribute("command");
        if (sessionCommand == null) {
            MenuButton button;
            try {
                button = MenuButton.parseString(request);
            } catch (Exception e) {
                response.setText(ResourceBundle.getBundle(messages).getString("unexpected"));
                response.setReplyMarkup(getDefaultKeyboard());
                return response;
            }
            switch (button) {
                case START -> {
                    session.setAttribute("command", MenuButton.START);
                    session.setAttribute("new_user", new User(username));
                    response.setText(ResourceBundle.getBundle(messages).getString("greeting"));
                    response.setReplyMarkup(getReplyKeyboardWithButtons(ResourceBundle.getBundle(messages).getString("continue")));
                }
                case PRODUCT_MENU -> {
                    session.setAttribute("command", MenuButton.PRODUCT_MENU);
                    response.setText(ResourceBundle.getBundle(messages).getString("productMenu_btn"));
                    response.setReplyMarkup(getInlineKeyboardWithProductMenu());
                }
                case ADD_DISHES -> {
                    session.setAttribute("command", MenuButton.ADD_DISHES);
                    response.setText(ResourceBundle.getBundle(messages).getString("select_pr_or_type"));
                    response.setReplyMarkup(getProductTypeInlineKeyboard());
                }
                case CALORIES_LEFT -> response.setText(userService.getActualUserCaloriesInfo(username));
                case CALORIES_DAILY -> response.setText(userService.getUserCaloriesInfo(username));
            }
        } else {
            switch (MenuButton.valueOf(String.valueOf(sessionCommand))) {
                case START -> response = userService.register(request, session);
                case PRODUCT_MENU -> {
                    if (session.getAttribute("productMenu_btn").toString().equals(ProductOption.ADD_PRODUCT.name())) {
                        try {
                            response = productService.getResponseByAttribute(username, request, session);
                        } catch (WebApiExeption e) {
                            session.removeAttribute("productMenu_btn");
                            response.setText(ResourceBundle.getBundle(messages).getString("not_found_products"));
                            response.setReplyMarkup(getDefaultKeyboard());
                        }
                    }
                }
                case ADD_DISHES -> {
                    if (session.getAttribute("product") != null) {
                        try {
                            response = entryService.saveEntryToUser(
                                    Integer.parseInt(String.valueOf(session.getAttribute("product"))),
                                    Integer.parseInt(request),
                                    username);
                        } catch (WebApiExeption e) {
                            response.setText(ResourceBundle.getBundle(messages).getString("write_error"));
                            response.setReplyMarkup(getDefaultKeyboard());
                        } finally {
                            session.removeAttribute("product");
                            session.removeAttribute("command");
                        }
                    } else {
                        try {
                            List<Product> products = productService.findAdminProductByName(request);
                            response.setText(ResourceBundle.getBundle(messages).getString("found_products"));
                            response.setReplyMarkup(getInlineKeyboardWithProductsInfo(products));
                            session.setAttribute("product", new Object());
                        } catch (WebApiExeption e) {
                            session.removeAttribute("command");
                            response.setText(ResourceBundle.getBundle(messages).getString("not_found_products"));
                            response.setReplyMarkup(getDefaultKeyboard());
                        }
                    }
                }
            }
        }
        return response;
    }

    public SendMessage handleCallback(String username, String request, Session session) {
        SendMessage response = new SendMessage();
        Object sessionCommand = session.getAttribute("command");
        switch (MenuButton.valueOf(String.valueOf(sessionCommand))) {
            case ADD_DISHES -> {
                if (session.getAttribute("product") != null) {
                    session.setAttribute("product", request);
                    response.setText(ResourceBundle.getBundle(messages).getString("print_weight"));
                    response.setReplyMarkup(null);
                } else {
                    session.setAttribute("product", request);
                    try {
                        List<Product> products = productService.findProductsByOwnerAndType(username, Integer.parseInt(request));
                        response.setText(ResourceBundle.getBundle(messages).getString("select_product"));
                        response.setReplyMarkup(getInlineKeyboardWithProductsInfo(products));
                    } catch (WebApiExeption e) {
                        response.setText(ResourceBundle.getBundle(messages).getString("product_read_error"));
                        response.setReplyMarkup(getDefaultKeyboard());
                        session.removeAttribute("product");
                        session.removeAttribute("command");
                    }
                }
            }
            case PRODUCT_MENU -> {
                if (session.getAttribute("productMenu_btn") == null) {
                    if (request.equals(ProductOption.EXIT.name())) {
                        session.removeAttribute("command");
                        response.setReplyMarkup(getDefaultKeyboard());
                        response.setText(ResourceBundle.getBundle(messages).getString("main_menu"));
                    } else {
                        response = productService.getResponse(request, session);
                    }
                } else {
                    if (Integer.parseInt(request) == -1) {
                        session.removeAttribute("command");
                        session.removeAttribute("product_id");
                        session.removeAttribute("productMenu_btn");
                        response.setReplyMarkup(getDefaultKeyboard());
                        response.setText(ResourceBundle.getBundle(messages).getString("main_menu"));
                    } else {
                        try {
                            response = productService.getResponseByAttribute(username, request, session);
                        } catch (WebApiExeption e) {
                            response.setText(e.getMessage());
                            response.setReplyMarkup(getDefaultKeyboard());
                            session.removeAttribute("command");
                            session.removeAttribute("product_id");
                            session.removeAttribute("productMenu_btn");
                        }
                    }
                }
            }
        }
        return response;
    }
}
