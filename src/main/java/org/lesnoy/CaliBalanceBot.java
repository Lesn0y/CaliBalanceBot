package org.lesnoy;

import org.apache.shiro.session.Session;
import org.lesnoy.bot.RequestHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.Optional;

public class CaliBalanceBot extends TelegramLongPollingSessionBot {

    private final RequestHandler handler = new RequestHandler();

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
        if (update.hasMessage() && update.getMessage().hasText()) {

            String username = update.getMessage().getFrom().getUserName();
            String request = update.getMessage().getText();

            SendMessage response = handler.handleMessage(username, request, session);

            response.setChatId(update.getMessage().getChatId().toString());
            try {
                execute(response);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }

        } else if (update.hasCallbackQuery()) {

            String username = update.getCallbackQuery().getFrom().getUserName();
            String request = update.getCallbackQuery().getData();

            SendMessage response = handler.handleCallback(username, request, session);

            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            try {
                execute(response);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}