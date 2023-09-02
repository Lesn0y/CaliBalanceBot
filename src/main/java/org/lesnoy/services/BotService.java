package org.lesnoy.services;

import org.apache.shiro.session.Session;
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
        System.out.println(userDTO);
        if (userDTO.getSex() == null) {
            if (session.getAttribute("sex") != null &&
                    (request.equals("Мужской") || request.equals("Женский"))) {
                userDTO.setSex(request.equals("Мужской") ? "MAN" : "WOMAN");
                session.setAttribute("user", userDTO);
                session.removeAttribute("sex");
            } else {
                String message = "Выберите ваш пол:";
                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

                keyboard.setResizeKeyboard(true);
                keyboard.setOneTimeKeyboard(true);

                ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow = new KeyboardRow();

                keyboardRow.add("Мужской");
                keyboardRow.add("Женский");

                keyboardRows.add(keyboardRow);

                keyboard.setKeyboard(keyboardRows);

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
                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

                keyboard.setResizeKeyboard(true);
                keyboard.setOneTimeKeyboard(true);

                ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow = new KeyboardRow();

                keyboardRow.add("Накачаться");
                keyboardRow.add("Похудеть");
                keyboardRow.add("Поддерживать форму");

                keyboardRows.add(keyboardRow);

                keyboard.setKeyboard(keyboardRows);

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
                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

                keyboard.setResizeKeyboard(true);
                keyboard.setOneTimeKeyboard(true);

                ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow = new KeyboardRow();

                keyboardRow.add("Минимальная");
                keyboardRow.add("Средняя");
                keyboardRow.add("Ежедневные тренировки");
                keyboardRow.add("Профессиональный спортсмен");

                keyboardRows.add(keyboardRow);

                keyboard.setKeyboard(keyboardRows);

                session.setAttribute("activity", new Object());
                return new TgResponse(message, keyboard);
            }
        }

        try {
            webService.registerUser(userDTO);
            return new TgResponse(userDTO.getLogin(), null);
        } catch (Exception e) {
            return new TgResponse("Произошла ошибка при сохранении", null);
        }
    }

    private TgResponse greeting() {
        session.setAttribute("user", new UserDTO(user.getUserName()));
        String message = "Я помогу вам расчитать опитмальную диету для вашей цели. Для начала работы нужно заполнить анкету";
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("Заполнить данные");

        keyboardRows.add(keyboardRow);

        keyboard.setKeyboard(keyboardRows);
        return new TgResponse(message, keyboard);
    }
}
