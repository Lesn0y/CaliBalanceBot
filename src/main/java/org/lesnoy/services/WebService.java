package org.lesnoy;

import okhttp3.*;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;

public class WebService {

    private final String serverUrl = "http://localhost:8080";

    public User register(User user) {
        OkHttpClient httpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("", user.get)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/users/" + user.getUserName() + "/stats")
                .post(requestBody)
                .build();
    }

    public String calculateUser(User user) {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(serverUrl + "/api/v1/users/" + user.getUserName() + "/stats")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            int code = response.code();
            return switch (code) {
                case 200 -> "Все хорошо вот данные";
                case 404 -> "Похоже вы еще не зарегистрированы";
                default -> "Произашла непридвиденная ошибка";
            };
        } catch (IOException e) {
            return "Произашла непридвиденная ошибка";
        }
    }

}
