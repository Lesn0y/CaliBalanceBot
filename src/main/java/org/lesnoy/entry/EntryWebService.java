package org.lesnoy.entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lesnoy.exeptions.WebApiExeption;

import java.io.IOException;

public class EntryWebService {

    private final String serverUrl = "http://89.108.76.111:8080";

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
                .url(serverUrl + "/api/v1/entries")
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
