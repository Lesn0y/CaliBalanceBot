package org.lesnoy.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.dto.ProductDTO;
import org.lesnoy.dto.UserDTO;
import org.lesnoy.web.exceptions.WebApiExeption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebService {

    private final String serverUrl = "http://localhost:8080";

    public UserDTO registerUser(UserDTO user) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/users")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 201) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, UserDTO.class);
            }
            throw new WebApiExeption("Пользователь с никнеймом @" + user.getLogin() + " уже зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UserDTO getUserStats(String login) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/users/" + login)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, UserDTO.class);
            }

            throw new WebApiExeption("Пользователь с никнеймом @" + login + " ещё не зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ProductDTO> findAllProductsByOwnerAndType(String login, int typeId) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/products?type=" + typeId + "&owner=" + login)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                JsonNode jsonArray = mapper.readTree(jsonResponse);

                List<ProductDTO> products = new ArrayList<>();
                for (JsonNode element : jsonArray) {
                    products.add(mapper.treeToValue(element, ProductDTO.class));
                }
                return products;
            }
            throw new WebApiExeption("Продукты пользователя @" + login + " не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ProductDTO> findAllProductsByType(int typeId) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/products?type=" + typeId)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                JsonNode jsonArray = mapper.readTree(jsonResponse);

                List<ProductDTO> products = new ArrayList<>();
                for (JsonNode element : jsonArray) {
                    products.add(mapper.treeToValue(element, ProductDTO.class));
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
