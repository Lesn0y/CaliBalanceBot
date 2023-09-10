package org.lesnoy.bot;

import org.apache.shiro.session.Session;
import org.jetbrains.annotations.NotNull;
import org.lesnoy.dto.ProductDTO;
import org.lesnoy.dto.ProductType;
import org.lesnoy.dto.UserDTO;
import org.lesnoy.web.WebService;
import org.lesnoy.web.exceptions.WebApiExeption;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotService {

    private final String request;
    private final User user;
    private final Session session;
    private final WebService webService = new WebService();
    private final MessageService mesService = new MessageService();
    private final ReplyKeyboardMarkup defaultKeyboard = initDefaultKeyboard();

    public BotService(String request, User user, Session session) {
        this.request = request;
        this.user = user;
        this.session = session;
    }

    public TgResponse getResponse() {

        if (session.getAttribute("command") != null) {
            return switch ((SessionAttribute) session.getAttribute("command")) {
                case MENU -> getMenu();
                case NEW_USER -> register();
                default -> throw new IllegalStateException("Unexpected value: " + session.getAttribute("command"));
            };
        }

        return switch (this.request) {
            case "/start" -> greeting();
            case "Суточное КБЖУ" -> {
                try {
                    UserDTO userStats = webService.getUserStats(user.getUserName());
                    yield new TgResponse(mesService.getUserInfo(userStats), defaultKeyboard);
                } catch (WebApiExeption e) {
                    yield new TgResponse(e.getMessage(), defaultKeyboard);
                }
            }
            case "Меню продуктов" -> {
                session.setAttribute("command", SessionAttribute.MENU);
                yield new TgResponse("Меню продуктов:", getKeyboardWithButtons(
                        "Добавить новый продукт",
                        "Посмотреть список своих продуктов",
                        "Просмотреть список всех продуктов",
                        "Назад"
                ));
            }
            case "test" -> {
                yield new TgResponse("test", getKeyboardWithButtons("test"));
            }
            default -> new TgResponse("Данная команда неизвестна", defaultKeyboard);
        };
    }

    private TgResponse getMenu() {
        if (session.getAttribute("user_name") == null) {
            return switch (this.request) {
                case "Добавить новый продукт" -> new TgResponse("Добавлен", getKeyboardWithButtons("Назад"));
                case "Посмотреть список своих продуктов" -> {
                    session.setAttribute("user_name", user.getUserName());
                    yield new TgResponse("Выберите тип продукта",
                            getKeyboardWithButtons(ProductType.getAllNames()));
                }
                case "Просмотреть список всех продуктов" -> {
                    session.setAttribute("user_name", "");
                    yield new TgResponse("Выберите тип продукта",
                            getKeyboardWithButtons(ProductType.getAllNames()));
                }
                case "Назад" -> {
                    session.removeAttribute("command");
                    yield new TgResponse("Назад", defaultKeyboard);
                }
                default -> new TgResponse("Данная команда неизвестна", null);
            };
        }
        try {
            if (session.getAttribute("user_name").equals("")) {
                session.removeAttribute("user_name");

                String message = mesService.getProductsInfo(
                        webService.findAllProductsByType(
                                ProductType.getTypeByName(this.request).ordinal()
                        ));
                return new TgResponse(message.isEmpty() ? "Подходящих продуктов нет" : message,
                        getKeyboardWithButtons(
                                "Добавить новый продукт",
                                "Посмотреть список своих продуктов",
                                "Просмотреть список всех продуктов",
                                "Назад"
                        ));
            } else {
                session.removeAttribute("user_name");
                String message = mesService.getProductsInfo(
                        webService.findAllProductsByOwnerAndType(
                                user.getUserName(),
                                ProductType.getTypeByName(this.request).ordinal()
                        ));
                return new TgResponse(message.isEmpty() ? "Подходящих продуктов нет" : message,
                        getKeyboardWithButtons(
                                "Добавить новый продукт",
                                "Посмотреть список своих продуктов",
                                "Просмотреть список всех продуктов",
                                "Назад"
                        ));
            }
        } catch (WebApiExeption e) {
            return new TgResponse(e.getMessage(), defaultKeyboard);
        } catch (Exception e) {
            return new TgResponse(e.getMessage(), defaultKeyboard);
        }
    }

    private TgResponse register() {
        UserDTO userDTO = (UserDTO) session.getAttribute("new_user");

        if (userDTO.getSex() == null) {
            if (session.getAttribute("sex") != null &&
                    (request.equals("Мужской") || request.equals("Женский"))) {
                userDTO.setSex(request.equals("Мужской") ? "MAN" : "WOMAN");
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("sex");
            } else {
                String message = "Выберите ваш пол:";
                ReplyKeyboardMarkup keyboard = getKeyboardWithButtons("Мужской", "Женский");

                session.setAttribute("sex", new Object());
                return new TgResponse(message, keyboard);
            }
        }
        if (userDTO.getAge() == 0) {
            if (session.getAttribute("age") != null && Integer.parseInt(request) > 0) {
                userDTO.setAge(Integer.parseInt(request));
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("age");
            } else {
                String message = "Сколько вам полных лет?";
                session.setAttribute("age", new Object());
                return new TgResponse(message, null);
            }
        }
        if (userDTO.getWeight() == 0) {
            if (session.getAttribute("weight") != null && Float.parseFloat(request) > 0) {
                userDTO.setWeight(Float.parseFloat(request));
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("weight");
            } else {
                String message = "Ваш вес: \n(В формате - 75.3)";
                session.setAttribute("weight", new Object());
                return new TgResponse(message, null);
            }
        }
        if (userDTO.getHeight() == 0) {
            if (session.getAttribute("height") != null && Float.parseFloat(request) > 0) {
                userDTO.setHeight(Float.parseFloat(request));
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("height");
            } else {
                String message = "Ваш рост: \n(В формате - 170.5)";
                session.setAttribute("height", new Object());
                return new TgResponse(message, null);
            }
        }

        if (userDTO.getGoal() == null) {
            if (session.getAttribute("goal") != null &&
                    (request.equals("Накачаться") || request.equals("Похудеть") || request.equals("Поддерживать форму"))) {
                userDTO.setGoal(request.equals("Накачаться") ? "PUMP_UP" : request.equals("Похудеть") ? "SLIM" : "KEEP_FIT");
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("goal");
            } else {
                String message = "Выберите вашу цель:";
                ReplyKeyboardMarkup keyboard = getKeyboardWithButtons(
                        "Накачаться", "Похудеть", "Поддерживать форму");

                session.setAttribute("goal", new Object());
                return new TgResponse(message, keyboard);
            }
        }

        if (userDTO.getActivity() == null) {
            if (session.getAttribute("activity") != null &&
                    (request.equals("Минимальная") || request.equals("Средняя")
                            || request.equals("Ежедневные тренировки") || request.equals("Профессиональный спортсмен"))) {
                userDTO.setActivity(request.equals("Минимальная") ? "MINIMUM" : request.equals("Средняя") ? "MIDDLE" : request.equals("Ежедневные тренировки") ? "EVERYDAY" : "MAXIMUM");
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("activity");
            } else {
                String message = "Выберите вашу недельную активность:";
                ReplyKeyboardMarkup keyboard = getKeyboardWithButtons(
                        "Минимальная", "Средняя", "Ежедневные тренировки", "Профессиональный спортсмен");

                session.setAttribute("activity", new Object());
                return new TgResponse(message, keyboard);
            }
        }

        try {
            session.removeAttribute("command");

            UserDTO registeredUser = webService.registerUser(userDTO);

            return new TgResponse(mesService.getUserInfo(registeredUser), defaultKeyboard);
        } catch (WebApiExeption e) {
            return new TgResponse(e.getMessage(), defaultKeyboard);
        }
    }

    private TgResponse greeting() {
        session.setAttribute("command", SessionAttribute.NEW_USER);
        session.setAttribute("new_user", new UserDTO(user.getUserName()));
        String message = "Я помогу вам расчитать опитмальную диету для вашей цели. Для начала работы нужно заполнить анкету";
        ReplyKeyboardMarkup keyboard = getKeyboardWithButtons("Заполнить данные");
        return new TgResponse(message, keyboard);
    }

    @NotNull
    private ReplyKeyboardMarkup getKeyboardWithButtons(String... buttons) {
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
