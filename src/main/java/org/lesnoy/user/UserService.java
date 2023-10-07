package org.lesnoy.user;

import org.apache.shiro.session.Session;
import org.lesnoy.entry.Entry;
import org.lesnoy.exeptions.WebApiExeption;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static org.lesnoy.bot.KeyboardProvider.getDefaultKeyboard;
import static org.lesnoy.bot.KeyboardProvider.getReplyKeyboardWithButtons;

public class UserService {
    private final UserWebService webService = new UserWebService();

    public String getActualUserCaloriesInfo(String userName) {
        try {
            Entry dailyUserStats = webService.getDailyUserStats(userName);
            if (dailyUserStats == null) {
                return "Похоже, сегодня вы еще не ели";
            }
            return dailyUserStats.getCaloriesInfo();
        } catch (WebApiExeption e) {
            return e.getMessage();
        }
    }

    public String getUserCaloriesInfo(String username) {
        try {
            return webService.getUserStats(username).getCaloriesInfo();
        } catch (WebApiExeption e) {
            return e.getMessage();
        }
    }

    public UserDTO saveUser(UserDTO userDTO) throws WebApiExeption {
        return webService.registerUser(userDTO);
    }

    public SendMessage register(String request, Session session) {
        SendMessage response = new SendMessage();

        UserDTO userDTO = (UserDTO) session.getAttribute("new_user");

        if (userDTO.getSex() == null) {
            if (session.getAttribute("sex") != null &&
                    (request.equals("Мужской") || request.equals("Женский"))) {
                userDTO.setSex(request.equals("Мужской") ? "MAN" : "WOMAN");
                session.setAttribute("new_user", userDTO);
                session.removeAttribute("sex");
            } else {
                String message = "Выберите ваш пол:";
                ReplyKeyboardMarkup keyboard = getReplyKeyboardWithButtons("Мужской", "Женский");

                session.setAttribute("sex", new Object());
                response.setText(message);
                response.setReplyMarkup(keyboard);
                return response;
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
                response.setText(message);
                return response;
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
                response.setText(message);
                return response;
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
                response.setText(message);
                return response;
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
                ReplyKeyboardMarkup keyboard = getReplyKeyboardWithButtons(
                        "Накачаться", "Похудеть", "Поддерживать форму");

                session.setAttribute("goal", new Object());
                response.setText(message);
                response.setReplyMarkup(keyboard);
                return response;
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
                ReplyKeyboardMarkup keyboard = getReplyKeyboardWithButtons(
                        "Минимальная", "Средняя", "Ежедневные тренировки", "Профессиональный спортсмен");

                session.setAttribute("activity", new Object());
                response.setText(message);
                response.setReplyMarkup(keyboard);
                return response;
            }
        }
        try {
            session.removeAttribute("command");
            session.removeAttribute("new_user");

            UserDTO registeredUser = saveUser(userDTO);

            response.setText(registeredUser.getCaloriesInfo());
            response.setReplyMarkup(getDefaultKeyboard());
            return response;
        } catch (WebApiExeption e) {
            response.setText("Произошла ошибка - " + e.getMessage());
            response.setReplyMarkup(getDefaultKeyboard());
            return response;
        }
    }
}
