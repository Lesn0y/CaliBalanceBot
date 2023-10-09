package org.lesnoy.entry;

import org.lesnoy.exeptions.WebApiExeption;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.lesnoy.bot.KeyboardProvider.*;

public class EntryService {

    private final EntryWebService webService = new EntryWebService();

    public SendMessage saveEntryToUser(int productId, int grams, String username) throws WebApiExeption {
        SendMessage resposne = new SendMessage();
        EntryDTO entryDTO = new EntryDTO(username, productId, grams);
        Entry entry = webService.saveEntryToUser(entryDTO);
        resposne.setText("Прием пищи был добавлен " + entry.getDate());
        resposne.setReplyMarkup(getDefaultKeyboard());
        return resposne;
    }
}
