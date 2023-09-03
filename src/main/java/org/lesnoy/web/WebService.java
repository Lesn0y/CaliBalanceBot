package org.lesnoy.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.dto.UserDTO;

import java.io.IOException;

public class WebService {

    private final String serverUrl = "http://localhost:8080";

    public UserDTO registerUser(UserDTO user) throws Exception {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

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
            throw new Exception("Пользователь с никнеймом @" + user.getLogin() + " уже зарегистрирован");
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public UserDTO getUserStats(String login) throws Exception {
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

            throw new Exception("Пользователь с никнеймом @" + login + " ещё не зарегистрирован");
        } catch (IOException e) {
            throw new Exception(e);
        }
    }
}
