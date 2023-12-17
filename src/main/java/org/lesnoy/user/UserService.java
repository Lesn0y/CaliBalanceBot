package org.lesnoy.user;

import org.apache.shiro.session.Session;
import org.lesnoy.bot.MessageProvider;
import org.lesnoy.entry.EntryService;
import org.lesnoy.exeptions.WebApiExeption;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ResourceBundle;

import static org.lesnoy.bot.KeyboardProvider.getDefaultKeyboard;
import static org.lesnoy.bot.KeyboardProvider.getReplyKeyboardWithButtons;

public class UserService {

    private final String btn = "buttons";
    private final UserWebService userWebService = new UserWebService();

    public SendMessage updateUserInfo(String username, String request) {
        SendMessage response = new SendMessage();
        String[] stats = request.split("/");
        if (stats.length != 4) {
            response.setText("Данные введены некорректно, повторите попытку ввода");
            return response;
        }

        UserCallInfoDTO userCallInfo = new UserCallInfoDTO();
        userCallInfo.setCal(Float.parseFloat(stats[0]));
        userCallInfo.setProt(Float.parseFloat(stats[1]));
        userCallInfo.setFats(Float.parseFloat(stats[2]));
        userCallInfo.setCarbs(Float.parseFloat(stats[3]));
        try {
            User user = userWebService.updateUserInfo(username, userCallInfo);
            response.setText(MessageProvider.convertUserInfoToMessage(new UserEntryDTO(user, null)));
            response.setReplyMarkup(null);
            return response;
        } catch (WebApiExeption e) {
            response.setText(e.getMessage());
            return response;
        }
    }

    public SendMessage register(String request, Session session) {
        SendMessage response = new SendMessage();

        User user = (User) session.getAttribute("new_user");

        if (user.getSex() == null) {
            if (session.getAttribute("sex") != null &&
                    (request.equals(ResourceBundle.getBundle(btn).getString("sex_man")) ||
                            request.equals(ResourceBundle.getBundle(btn).getString("sex_woman")))) {
                user.setSex(request.equals(ResourceBundle.getBundle(btn).getString("sex_man")) ? "MAN" : "WOMAN");
                session.setAttribute("new_user", user);
                session.removeAttribute("sex");
            } else {
                String message = ResourceBundle.getBundle(btn).getString("sex_input");
                ReplyKeyboardMarkup keyboard = getReplyKeyboardWithButtons(
                        ResourceBundle.getBundle(btn).getString("sex_man"),
                        ResourceBundle.getBundle(btn).getString("sex_woman"));

                session.setAttribute("sex", new Object());
                response.setText(message);
                response.setReplyMarkup(keyboard);
                return response;
            }
        }
        if (user.getAge() == 0) {
            if (session.getAttribute("age") != null && Integer.parseInt(request) > 0) {
                user.setAge(Integer.parseInt(request));
                session.setAttribute("new_user", user);
                session.removeAttribute("age");
            } else {
                String message = ResourceBundle.getBundle(btn).getString("age_input");
                session.setAttribute("age", new Object());
                response.setText(message);
                return response;
            }
        }
        if (user.getWeight() == 0) {
            if (session.getAttribute("weight") != null && Float.parseFloat(request) > 0) {
                user.setWeight(Float.parseFloat(request));
                session.setAttribute("new_user", user);
                session.removeAttribute("weight");
            } else {
                String message = ResourceBundle.getBundle(btn).getString("weight_input");
                session.setAttribute("weight", new Object());
                response.setText(message);
                return response;
            }
        }
        if (user.getHeight() == 0) {
            if (session.getAttribute("height") != null && Float.parseFloat(request) > 0) {
                user.setHeight(Float.parseFloat(request));
                session.setAttribute("new_user", user);
                session.removeAttribute("height");
            } else {
                String message = ResourceBundle.getBundle(btn).getString("height_input");
                session.setAttribute("height", new Object());
                response.setText(message);
                return response;
            }
        }
        if (user.getGoal() == null) {
            if (session.getAttribute("goal") != null &&
                    (request.equals(ResourceBundle.getBundle(btn).getString("goal_1")) ||
                     request.equals(ResourceBundle.getBundle(btn).getString("goal_2")) ||
                     request.equals(ResourceBundle.getBundle(btn).getString("goal_3")))) {
                user.setGoal(request.equals(ResourceBundle.getBundle(btn).getString("goal_1")) ? "PUMP_UP" :
                        request.equals(ResourceBundle.getBundle(btn).getString("goal_2")) ? "SLIM" : "KEEP_FIT");
                session.setAttribute("new_user", user);
                session.removeAttribute("goal");
            } else {
                String message = ResourceBundle.getBundle(btn).getString("goal_input");
                ReplyKeyboardMarkup keyboard = getReplyKeyboardWithButtons(
                        ResourceBundle.getBundle(btn).getString("goal_1"),
                        ResourceBundle.getBundle(btn).getString("goal_2"),
                        ResourceBundle.getBundle(btn).getString("goal_3"));

                session.setAttribute("goal", new Object());
                response.setText(message);
                response.setReplyMarkup(keyboard);
                return response;
            }
        }
        if (user.getActivity() == null) {
            if (session.getAttribute("activity") != null &&
                    (request.equals(ResourceBundle.getBundle(btn).getString("activity_1")) ||
                     request.equals(ResourceBundle.getBundle(btn).getString("activity_2")) ||
                     request.equals(ResourceBundle.getBundle(btn).getString("activity_3")) ||
                     request.equals(ResourceBundle.getBundle(btn).getString("activity_4")))) {
                user.setActivity(request.equals(ResourceBundle.getBundle(btn).getString("activity_1")) ? "MINIMUM" :
                        request.equals(ResourceBundle.getBundle(btn).getString("activity_2")) ? "MIDDLE" :
                        request.equals(ResourceBundle.getBundle(btn).getString("activity_3")) ? "EVERYDAY" : "MAXIMUM");
                session.setAttribute("new_user", user);
                session.removeAttribute("activity");
            } else {
                String message = ResourceBundle.getBundle(btn).getString("activity_input");
                ReplyKeyboardMarkup keyboard = getReplyKeyboardWithButtons(
                        ResourceBundle.getBundle(btn).getString("activity_1"),
                        ResourceBundle.getBundle(btn).getString("activity_2"),
                        ResourceBundle.getBundle(btn).getString("activity_3"),
                        ResourceBundle.getBundle(btn).getString("activity_4"));

                session.setAttribute("activity", new Object());
                response.setText(message);
                response.setReplyMarkup(keyboard);
                return response;
            }
        }
        try {
            User registeredUser = saveUser(user);

            response.setText(registeredUser.getCaloriesInfo());
            response.setReplyMarkup(getDefaultKeyboard());
            return response;
        } catch (WebApiExeption e) {
            response.setText("Произошла ошибка - " + e.getMessage());
            response.setReplyMarkup(getDefaultKeyboard());
            return response;
        } finally {
            session.removeAttribute("command");
            session.removeAttribute("new_user");
        }
    }

    private User saveUser(User user) throws WebApiExeption {
        return userWebService.registerUser(user);
    }
}
