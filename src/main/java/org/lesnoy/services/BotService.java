package org.lesnoy;

import org.telegram.telegrambots.meta.api.objects.User;

public class BotService {

    private final String request;
    private final User user;
    private final WebService webService = new WebService();

    public BotService(String request, User user) {
        this.request = request;
        this.user = user;
    }

    public String getResponse() {
        return switch (this.request) {
            case "/start" -> start();
            case "Вывести КБЖУ" -> webService.calculateUser(this.user);
            case "Сброс данных пользователя" -> webService.calculateUser(this.user);
            default -> "Данная команда неизвестна";
        };
    }

    private String start() {
        webService.register(user);
        return null;
    }
}
