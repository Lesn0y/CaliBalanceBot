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

    private final String serverUrl = "http://localhost:8080";

    public Product saveProduct(Product product) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/products")
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

    public List<Product> findProductByName(String productName) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/products?name=" + productName)
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
            throw new WebApiExeption("Продукт " + productName + " не найден");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Product> findAllProductsByOwnerAndType(String login, int typeId) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/products?type=" + typeId + "&owner=" + login)
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
            throw new WebApiExeption("Продукты пользователя @" + login + " не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Product> findAllProductsByType(int typeId) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/products?type=" + typeId)
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
            throw new WebApiExeption("Продукты не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
