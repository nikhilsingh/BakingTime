package com.example.android.bakingtime.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nikhil on 8/8/17.
 */

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    private static final String REQ_SCHEME_HTTPS = "https";

    private static final String REQ_AUTHORITY = "d17h27t6h515a5.cloudfront.net";
    private static final String REQ_PATH="topher/2017/May/59121517_baking/baking.json";

    public static URL buildURL(){
        Uri uri = new Uri.Builder().scheme(REQ_SCHEME_HTTPS)
                .authority(REQ_AUTHORITY).appendEncodedPath(REQ_PATH).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG, "url - " + url.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String reqHttpForJSONData(URL reqUrl) {

        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request req = new Request.Builder().url(reqUrl).build();
        try {
            Response response = client.newCall(req).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            return "Please try again later";
        }

    }

    public static String getRecipeJSON(){
        return reqHttpForJSONData(buildURL());
    }

}

//TODO: modify network implementation

//https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json