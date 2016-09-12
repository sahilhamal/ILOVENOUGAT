package com.example.android.ilovenougat;

import android.app.DownloadManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public final class zjson {
    private static final String LOG_TAG = zjson.class.getSimpleName();

    public static List<Data> zdata(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Data> zdata = extractFeatureFromJson(jsonResponse);

        return zdata;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Data> extractFeatureFromJson(String itemJSON) {
        if (TextUtils.isEmpty(itemJSON)) {
            return null;
        }
        List<Data> zdata = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(itemJSON);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject itemDetails = resultsArray.getJSONObject(i);

                String brand = itemDetails.optString("brandName").toString();

                String price = itemDetails.optString("originalPrice").toString();

                String dprice= itemDetails.optString("price").toString();

                String discount= itemDetails.optString("percentOff").toString();

                String productName= itemDetails.optString("productName").toString();

                String iurl=itemDetails.optString("thumbnailImageUrl").toString();

                Data datas = new Data(brand, price, dprice,discount,iurl,productName);

                zdata.add(datas);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Zappos json parsing problem.", e);
        }
        return zdata;
    }
}




