package org.lesnoy.entry;

import org.lesnoy.exeptions.WebApiExeption;
import org.lesnoy.user.User;
import org.lesnoy.user.UserEntryDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.lesnoy.bot.KeyboardProvider.*;
import static org.lesnoy.bot.MessageProvider.convertUserInfoToMessage;

public class EntryService {

    private final EntryWebService webService = new EntryWebService();

    public String getUserDailyInfo(String username) throws WebApiExeption {
        UserEntryDTO userInfo = webService.getLastModifiedUserEntry(username);
        return convertUserInfoToMessage(userInfo);
    }

    public SendMessage saveEntryToUser(int productId, int grams, String username) throws WebApiExeption {
        SendMessage response = new SendMessage();
        EntryDTO entryDTO = new EntryDTO(username, productId, grams);
        Entry entry = webService.saveEntryToUser(entryDTO);
        response.setText("Прием пищи был добавлен " + entry.getDate());
        response.setReplyMarkup(getDefaultKeyboard());
        return response;
    }
}
