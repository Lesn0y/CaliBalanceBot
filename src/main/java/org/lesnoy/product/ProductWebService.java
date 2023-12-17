package org.lesnoy.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.exeptions.WebApiExeption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductWebService {

    private final String serverUrl = "http://localhost:8080/api/v2";

    public List<Product> getUserProductsByType(String username, int typeId) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username + "/products?type-id=" + typeId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                JsonNode jsonArray = mapper.readTree(jsonResponse);

                List<Product> products = new ArrayList<>();
                for (JsonNode element : jsonArray) {
                    products.add(mapper.treeToValue(element, Product.class));
                }
                return products;
            }
            throw new WebApiExeption("Продукты пользователя @" + username + " не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Product getProductByUsernameAndId(String username, int productId) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username + "/" + productId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, Product.class);
            }
            throw new WebApiExeption("Продукт не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Product saveProductToUser(Product product, String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username + "/products")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 201) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, Product.class);
            }
            throw new WebApiExeption("Произошла ошибка при сохранении продукта " + product.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteProductFromUserMenu(int productId, String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username + "/products/" + productId)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() != 204) {
                throw new WebApiExeption("Произошла ошибка при сохранении продукта #" + productId);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
