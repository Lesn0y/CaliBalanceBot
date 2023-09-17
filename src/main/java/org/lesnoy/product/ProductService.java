package org.lesnoy.product;

import org.lesnoy.bot.TgRequest;
import org.lesnoy.exeptions.WebApiExeption;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public class ProductService {

    private final ProductWebService webService = new ProductWebService();

    public SendMessage getResponse(TgRequest tgRequest) {


        return null;
    }

    public List<ProductDTO> findAllProductsByType(int ordinal) throws WebApiExeption {
        return webService.findAllProductsByType(ordinal);
    }

    public List<ProductDTO> findAllProductsByOwnerAndType(String userName, int ordinal) throws WebApiExeption {
        return webService.findAllProductsByOwnerAndType(userName, ordinal);
    }

    private String getProductsInfo(List<ProductDTO> products) {
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
