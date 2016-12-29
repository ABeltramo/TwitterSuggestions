package com.abeltramo.twsuggestions;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import java.io.IOException;

/**
 * twitter-suggestions
 * Created by ABeltramo on 29/12/16.
 */
public class RESTClient {
    public static JSONObject getUrl(String url){
        JSONObject result = null;
        String remoteBody = "";
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            remoteBody =  response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            result = new JSONObject(remoteBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
