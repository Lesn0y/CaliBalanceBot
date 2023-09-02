package org.lesnoy.services;

import org.lesnoy.dto.UserDTO;

public class MessageService {

    public String getUserInfo(UserDTO user) {
        StringBuilder builder = new StringBuilder();

        builder.append("Ваша норма каллорий: ");
        builder.append(user.getCal());
        builder.append("\nВаша норма белков: ");
        builder.append(user.getProt());
        builder.append("\nВаша норма жиров: ");
        builder.append(user.getFats());
        builder.append("\nВаша норма углеводов: ");
        builder.append(user.getCarbc());

        return builder.toString();
    }

}
