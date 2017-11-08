package com.bhavaneulergmail.newsflow.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class NetworkConnect {

    private static final String LOG_TAG = NetworkConnect.class.getSimpleName();


    private NetworkConnect() {

    }

    public static Boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String makeHttpRequest(String requestUrl) throws IOException {

        URL url = null;

        url = createUrl(requestUrl);

        String jsonResponse = null;

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(6000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, e.toString());

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    public static URL createUrl(String requestUrl) {

        Uri.Builder builder = Uri.parse(requestUrl).buildUpon();
        Uri uri = builder.build();


        URL url = null;

        try {

            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return url;


    }


    private static String readFromStream(InputStream inputStream) throws IOException {


        StringBuilder output = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        while (line != null) {
            output.append(line);
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
            }

        }

        return output.toString();

    }
}
