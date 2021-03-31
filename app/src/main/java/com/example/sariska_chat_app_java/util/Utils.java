package com.example.sariska_chat_app_java.util;

import android.annotation.TargetApi;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Utils {

    final String[] token = {null};

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String fetchToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://api.sariska.io/api/v1/misc/generate-token";
        String json = "{\"apiKey\":\"24926faa88ca145d7466c2e123aca790768002ff8faf338e29ca\"," +
                "\"user\":{\"id\":\"99266\"," +
                "\"name\":\"Dipak\"," +
                "\"email\":\"dipak.work.14@gmail.com\"," +
                "\"avatar\":\"null\"}}";
        System.out.println(json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        try(Response response = client.newCall(request).execute()){
            String responseString = response.body().string();
            responseString = "[" + responseString + "]";
            JSONArray array = new JSONArray(responseString);
            String finalResponse = null;
            for(int i=0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                finalResponse = object.getString("token");
            }
            return finalResponse;
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("This cannot be done");
            return null;
        }
    }
}
