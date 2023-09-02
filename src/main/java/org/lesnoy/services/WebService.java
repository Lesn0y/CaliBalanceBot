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


        try (Response response = httpClient.newCall(request).execute()){
            System.out.println("RESPONSE - " + response.body().string());
            return user;
        } catch (IOException e) {
            throw new Exception(e);
        }
    }
}
