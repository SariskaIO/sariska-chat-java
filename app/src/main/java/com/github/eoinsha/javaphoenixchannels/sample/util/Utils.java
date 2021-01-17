package com.github.eoinsha.javaphoenixchannels.sample.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import com.github.eoinsha.javaphoenixchannels.sample.chat.R;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {

    private Context context;
    private SharedPreferences sharedPref;

    private static final String KEY_SHARED_PREF = "PHOENIX_CHAT_SAMPLE";
    private static final int KEY_MODE_PRIVATE = 0;
    private static final String KEY_TOPIC = "topic";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public OkHttpClient client = new OkHttpClient();

    public Utils(Context context) {
        this.context = context;
        sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF, KEY_MODE_PRIVATE);
    }

    public void storeChannelDetails(final String topic) {
        Editor editor = sharedPref.edit();
        editor.putString(KEY_TOPIC, topic);
        editor.apply();
    }

    public String getTopic() {
        final String prevTopic = sharedPref.getString(KEY_TOPIC, null);
        return prevTopic != null ? prevTopic : context.getText(R.string.default_topic).toString();
    }

    public String getUrl() {
        return context.getText(R.string.default_url).toString();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public String fetchToken() throws IOException {
        String json = "{'apiKey':'" + context.getText(R.string.api_key) + "'}";
        String response = this.post("https://api.collabor8.xyz/api/v1/misc/generate-token", json);
        System.out.println(response);
        return response;
    }
}