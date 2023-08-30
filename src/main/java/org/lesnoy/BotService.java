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
            case "/start" -> "Добро пожаловать в CaliBalance";
            case "/calculate" -> webService.calculateUser(this.user);
            default -> "Данная команда неизвестна";
        };
    }
}
