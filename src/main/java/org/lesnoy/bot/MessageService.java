package org.lesnoy.bot;

import org.lesnoy.product.ProductDTO;
import org.lesnoy.user.UserDTO;

import java.util.List;

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

    public String getProductsInfo(List<ProductDTO> products) {
        StringBuilder builder = new StringBuilder();

        for (ProductDTO element : products) {
            builder.append(element.getName());
            builder.append(" - ");
            builder.append(element.getProductType().getName());
            builder.append(" - ");
            builder.append(element.getCal());
            builder.append("/");
            builder.append(element.getProt());
            builder.append("/");
            builder.append(element.getFats());
            builder.append("/");
            builder.append(element.getCarbs());
            builder.append("\n");
        }

        return builder.toString();


    }
}
