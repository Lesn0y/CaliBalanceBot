package org.lesnoy.entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.exeptions.WebApiExeption;
import org.lesnoy.user.UserEntryDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntryWebService {

    private final String serverUrl = "http://localhost:8080/api/v2";

    public UserEntryDTO getLastModifiedUserEntry(String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username + "/entries/daily-last")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, UserEntryDTO.class);
            }
            throw new WebApiExeption("Продукт не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Entry> getTodayUserEntries(String username) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + username + "/entries/daily")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();
                JsonNode jsonArray = mapper.readTree(jsonResponse);

                List<Entry> products = new ArrayList<>();
                for (JsonNode element : jsonArray) {
                    products.add(mapper.treeToValue(element, Entry.class));
                }
                return products;
            }
            throw new WebApiExeption("Продукты пользователя @" + username + " не найдены");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Entry saveEntryToUser(EntryDTO entryDTO) throws WebApiExeption {
        OkHttpClient httpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(entryDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(serverUrl + "/users/" + entryDTO.getUsername() + "/entries")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() == 201) {
                String jsonResponse = response.body().string();
                return mapper.readValue(jsonResponse, Entry.class);
            }
            throw new WebApiExeption("Произошла ошибка при записи приема пищи");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
