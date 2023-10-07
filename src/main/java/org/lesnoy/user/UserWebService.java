package org.lesnoy.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.entry.Entry;
import org.lesnoy.exeptions.WebApiExeption;

import java.io.IOException;

public class UserWebService {

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
            throw new WebApiExeption("Пользователь с никнеймом @" + user.getUsername() + " уже зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UserDTO getUserStats(String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/users/" + username)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, UserDTO.class);
            }

            throw new WebApiExeption("Пользователь с никнеймом @" + username + " ещё не зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Entry getDailyUserStats(String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/entries?username=" + username)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, Entry.class);
            } else if (response.code() == 204) {
                return null;
            }

            throw new WebApiExeption("Пользователь с никнеймом @" + username + " ещё не зарегистрирован");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
