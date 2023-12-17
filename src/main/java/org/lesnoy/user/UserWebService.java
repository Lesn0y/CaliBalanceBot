package org.lesnoy.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.exeptions.WebApiExeption;

import java.io.IOException;

public class UserWebService {

    private final String serverUrl = "http://localhost:8080/api/v2";

    public User getUserByUsername(String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, User.class);
            }

            throw new WebApiExeption("Пользователь с никнеймом @" + username + " ещё не зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public User registerUser(User user) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(serverUrl + "/users")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 201) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, User.class);
            }
            throw new WebApiExeption("Пользователь с никнеймом @" + user.getUsername() + " уже зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public User updateUserInfo(String username, UserCallInfoDTO userCallInfoDTO) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(userCallInfoDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username)
                .put(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, User.class);
            }
            throw new WebApiExeption("Пользователь с никнеймом @" + username + " ещё не зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
