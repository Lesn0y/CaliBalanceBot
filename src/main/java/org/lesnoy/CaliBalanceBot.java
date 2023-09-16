package org.lesnoy;

import org.apache.shiro.session.Session;
import org.lesnoy.bot.BotService;
import org.lesnoy.bot.TgResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.Optional;

public class CaliBalanceBot extends TelegramLongPollingSessionBot {

    public CaliBalanceBot(String botToken) {
        super(botToken);
    }


    @Override
    public void onUpdateReceived(Update update, Optional<Session> optional) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            User user = update.getMessage().getFrom();
            String request = update.getMessage().getText();

            BotService service = new BotService(request, user, optional.orElse(null));


            TgResponse response = service.getResponse();

            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(response.message());
            if (response.keyboard() != null) {
                message.setReplyMarkup(response.keyboard());
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "CaliBalanceBot";
    }
}
