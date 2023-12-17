package org.lesnoy.bot;

import org.lesnoy.entry.Entry;
import org.lesnoy.user.User;
import org.lesnoy.user.UserEntryDTO;

public class MessageProvider {
    public static String convertUserInfoToMessage(UserEntryDTO userInfo) {
        User user = userInfo.getUser();
        String userCallInfo = "Ваша норма:" +
                "\n\tКаллорий - " +
                user.getCal() +
                "\n\tБелков - " +
                user.getProt() +
                "\n\tЖиров - " +
                user.getFats() +
                "\n\tУглеводов - " +
                user.getCarbs();
        if (userInfo.getLastEntry() != null) {
            Entry entry = userInfo.getLastEntry();
            return userCallInfo +
                    "\n\nЕщё добрать:" +
                    "\n\tКаллорий - " +
                    entry.getCalLeft() +
                    "\n\tБелков - " +
                    entry.getProtLeft() +
                    "\n\tЖиров - " +
                    entry.getFatsLeft() +
                    "\n\tУглеводов - " +
                    entry.getCarbsLeft();
        }
        return userCallInfo;
    }
}
