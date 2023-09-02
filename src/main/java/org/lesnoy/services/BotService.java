package org.lesnoy.services;

import org.apache.shiro.session.Session;
import org.jetbrains.annotations.NotNull;
import org.lesnoy.dto.UserDTO;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class BotService {

    private final String request;
    private final User user;
    private final Session session;
    private final WebService webService = new WebService();
    private final MessageService mesService = new MessageService();

    public BotService(String request, User user, Session session) {
        this.request = request;
        this.user = user;
        this.session = session;
    }

    public TgResponse getResponse() {
        if (session.getAttribute("user") != null) {
            return register();
        }
        return switch (this.request) {
            case "/start" -> greeting();
            default -> new TgResponse("Данная команда неизвестна", null);
        };
    }

    private TgResponse register() {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        if (userDTO.getSex() == null) {
            if (session.getAttribute("sex") != null &&
                    (request.equals("Мужской") || request.equals("Женский"))) {
                userDTO.setSex(request.equals("Мужской") ? "MAN" : "WOMAN");
                session.setAttribute("user", userDTO);
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
                session.setAttribute("user", userDTO);
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
                session.setAttribute("user", userDTO);
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
                session.setAttribute("user", userDTO);
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
                session.setAttribute("user", userDTO);
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
                session.setAttribute("user", userDTO);
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
            session.removeAttribute("user");

            UserDTO registeredUser = webService.registerUser(userDTO);

            return new TgResponse(mesService.getUserInfo(registeredUser), null);
        } catch (Exception e) {
            return new TgResponse("Произошла ошибка при сохранении", null);
        }
    }

    private TgResponse greeting() {
        session.setAttribute("user", new UserDTO(user.getUserName()));
        String message = "Я помогу вам расчитать опитмальную диету для вашей цели. Для начала работы нужно заполнить анкету";
        ReplyKeyboardMarkup keyboard = getKeyboardWithButtons("Заполнить данные");
        return new TgResponse(message, keyboard);
    }

    @NotNull
    private ReplyKeyboardMarkup getKeyboardWithButtons(String... buttons) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        for (String button : buttons) {
            keyboardRow.add(button);
        }

        keyboardRows.add(keyboardRow);

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }
}
