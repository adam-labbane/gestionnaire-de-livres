package com.example.cdi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class OpenLibraryAPI {

    private static final OkHttpClient client = new OkHttpClient();

    public static JSONObject searchBookByISBN(String isbn) throws Exception {
        String url = "https://openlibrary.org/api/books?bibkeys=" + isbn + "&format=json&jscmd=details";
        System.out.println("API Request URL: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Unexpected code " + response);

            String responseData = response.body().string();
            return new JSONObject(responseData);
        }
    }
}
