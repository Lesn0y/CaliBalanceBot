package org.lesnoy;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi tgBot = new TelegramBotsApi(DefaultBotSession.class);
            tgBot.registerBot(new CaliBalanceBot("6112685779:AAENonY4XYk2hItZ_IFCwjZHKeXS4nvnoNk"));
        } catch (TelegramApiException e) {
            System.out.println(e.getCause());
            throw new RuntimeException(e);
        }
    }
}