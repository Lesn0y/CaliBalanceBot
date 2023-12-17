package org.lesnoy.product;

import org.apache.shiro.session.Session;
import org.lesnoy.exeptions.WebApiExeption;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static org.lesnoy.bot.KeyboardProvider.*;

public class ProductService {

    private final ProductWebService webService = new ProductWebService();

    public SendMessage getResponse(String request, Session session) {
        SendMessage response = new SendMessage();
        return switch (ProductOption.valueOf(request)) {
            case OWN_PRODUCTS -> {
                session.setAttribute("productMenu_btn", ProductOption.OWN_PRODUCTS);
                response.setReplyMarkup(getProductTypeInlineKeyboard());
                response.setText("Выберите тип продукта:");
                yield response;
            }
            case ADD_PRODUCT -> {
                session.setAttribute("productMenu_btn", ProductOption.ADD_PRODUCT);
                response.setText("Введите название продукта:");
                response.setReplyMarkup(null);
                yield response;
            }
            case EXIT -> null;
        };
    }

    public SendMessage getResponseByAttribute(String username, String request, Session session) throws WebApiExeption {
        SendMessage response = new SendMessage();
        if (session.getAttribute("product_id") == null) {
            switch (ProductOption.valueOf(session.getAttribute("productMenu_btn").toString())) {
                case OWN_PRODUCTS -> {
                    session.setAttribute("product_id", new Object());
                    response.setText("Нажмите на продукт чтобы удалить его из своего рациона");
                    List<Product> products =
                            findProductsByUsernameAndType(username, Integer.parseInt(request));
                    response.setReplyMarkup(getInlineKeyboardWithProductsInfo(products));
                }
                case ADD_PRODUCT -> response = saveProduct(username, request, session);
            }
        } else {
            session.removeAttribute("product_id");
            session.removeAttribute("command");
            if (ProductOption.valueOf(String.valueOf(session.getAttribute("productMenu_btn"))) == ProductOption.OWN_PRODUCTS) {
                response = removeProductFromUserMenu(Integer.parseInt(request), username);
            }
            session.removeAttribute("productMenu_btn");
        }
        return response;
    }

    public List<Product> findProductsByUsernameAndType(String username, int ordinal) throws WebApiExeption {
        return webService.getUserProductsByType(username, ordinal);
    }

    public SendMessage saveProduct(String username, String request, Session session) throws WebApiExeption {
        SendMessage response = new SendMessage();
        if (session.getAttribute("new_product") == null) {
            session.setAttribute("new_product", new Product(request));
        }

        Product product = (Product) session.getAttribute("new_product");

        if (product.getProductType() == null) {
            if (session.getAttribute("type") != null) {
                try {
                    product.setProductTypeByTypeName(request);
                } catch (Exception e) {
                    response.setText("Данные введены некорректно, повторите попытку ввода");
                    return response;
                }
                session.setAttribute("new_product", product);
                session.removeAttribute("type");
            } else {
                response.setText("Выберите к какому типу этот продукт относится:");
                response.setReplyMarkup(
                        getReplyKeyboardWithButtons(ProductType.getAllNames())
                );
                session.setAttribute("type", new Object());
                return response;
            }
        }
        if (product.getCal() == 0) {
            if (session.getAttribute("cal") != null) {
                String[] stats = request.split("/");
                if (stats.length != 5) {
                    response.setText("Данные введены некорректно, повторите попытку ввода");
                    return response;
                }
                product.setGrams(Integer.parseInt(stats[0]));
                product.setCal(Float.parseFloat(stats[1]));
                product.setProt(Float.parseFloat(stats[2]));
                product.setFats(Float.parseFloat(stats[3]));
                product.setCarbs(Float.parseFloat(stats[4]));
                session.removeAttribute("cal");
            } else {
                String message = "Укажите данные в формате ВЕС/КАЛОРИИ/БЕЛКИ/ЖИРЫ/УГЛЕВОДЫ";
                response.setText(message);
                session.setAttribute("cal", new Object());
                return response;
            }
        }

        session.removeAttribute("command");
        session.removeAttribute("productOption");
        session.removeAttribute("new_product");
        session.removeAttribute("productMenu_btn");
        Product newProduct = webService.saveProductToUser(product, username);
        response.setText("Продукт " + getProductInfo(newProduct) + " - успешно сохранён");
        response.setReplyMarkup(getDefaultKeyboard());

        return response;
    }

    public SendMessage removeProductFromUserMenu(int productId, String username) throws WebApiExeption {
        SendMessage response = new SendMessage();
        webService.deleteProductFromUserMenu(productId, username);
        response.setText("Продукт " + productId + " был удалён из вашего меню");
        response.setReplyMarkup(getDefaultKeyboard());
        return response;
    }

    public static String getProductInfo(Product product) {
        StringBuilder builder = new StringBuilder();

        builder.append(product.getName());
        builder.append("\n");
        builder.append("К-");
        builder.append(product.getCal());
        builder.append(" / ");
        builder.append("Б-");
        builder.append(product.getProt());
        builder.append(" / ");
        builder.append("Ж-");
        builder.append(product.getFats());
        builder.append(" / ");
        builder.append("У-");
        builder.append(product.getCarbs());

        return builder.toString();
    }
}
