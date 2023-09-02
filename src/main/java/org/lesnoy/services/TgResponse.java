package org.lesnoy.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public record TgResponse(String message, ReplyKeyboardMarkup keyboard) {
}
