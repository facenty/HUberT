package com.hyper.ubertransport.order.usable;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

class GoogMatrixRequest {

    private static final String API_KEY = "AIzaSyClOBAq8ne46gt8FDT3Ae_iWE03m6-5ArM";

    //places

    private String str_from;
    private String str_to;
    private final OkHttpClient client = new OkHttpClient();

    //main request

    public Long transfer() {
        String URL_REQUEST = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:" + str_from
                + "&destinations=place_id:" + str_to + "&mode=driving&language=pl-PL&avoid=tolls&key=" + API_KEY;
        Log.i("TRANSFER.F", URL_REQUEST);
        Log.i("TRANSFER.F", str_from);
        Log.i("TRANSFER.F", str_to);
        String response = run(URL_REQUEST);
        Log.i("TRANSFER.F", "res" + response);
        return extractJson(response);
    }

    //Reading info from json files
    private Long extractJson(String response) {


        JSONObject jObject = null;
        try {
            jObject = new JSONObject(response);
        } catch (JSONException e) {
            Log.i("TRANSFER.F", "Json create");
            e.printStackTrace();
        }

        JSONArray jArray = null;
        try {
            jArray = Objects.requireNonNull(jObject).getJSONArray("rows");
        } catch (JSONException e) {
            Log.i("TRANSFER.F", "Json rows");
            e.printStackTrace();
        }

        long distanceValue = 0L;
        for (int i = 0; i < Objects.requireNonNull(jArray).length(); i++) {
            try {

                JSONObject jObject1 = null;
                try {
                    jObject1 = jArray.getJSONObject(0);
                } catch (JSONException e) {
                    Log.i("TRANSFER.F", "Json empty");
                    e.printStackTrace();
                }

                JSONArray jArray1 = null;
                try {
                    jArray1 = Objects.requireNonNull(jObject1).getJSONArray("elements");
                } catch (JSONException e) {
                    Log.i("TRANSFER.F", "Json elements");
                    e.printStackTrace();
                }
                for (int j = 0; j < Objects.requireNonNull(jArray1).length(); j++) {

                    JSONObject jObject2 = null;
                    try {
                        jObject2 = jArray1.getJSONObject(0);
                    } catch (JSONException e) {
                        Log.i("TRANSFER.F", "Json empty");
                        e.printStackTrace();
                    }

                    JSONObject jObject3 = null;
                    try {
                        jObject3 = Objects.requireNonNull(jObject2).getJSONObject("distance");
                        Log.i("TRANSFER.F", "Zawartosc" + jObject3.toString());
                    } catch (JSONException e) {
                        Log.i("TRANSFER.F", "Json distance");
                        e.printStackTrace();
                    }

                    //JSONObject oneObject = jObject3.getJSONObject("value");
                    distanceValue = Objects.requireNonNull(jObject3).getLong("value");
                    //String oneObjectsItem2 = oneObject.getString("duration");
                    Log.i("JSON1", Long.toString(distanceValue));
                    // Log.i("JSON2", oneObjectsItem2);
                }
            } catch (JSONException e) {
                Log.i("JSONY", "Blad\n" + response);
            }
        }

        return distanceValue;
    }

    private String run(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException();
        } catch (IOException e) {
            Log.i("RUN.F", "ResponseCall");
            e.printStackTrace();
            return "";
        }
        try {
            return response.body().string();
        } catch (IOException e) {
            Log.i("RUN", "ResponseReturn");
            e.printStackTrace();
            return "";
        }
    }

    public void setStr_from(String str_from) {
        this.str_from = str_from;
    }

    public void setStr_to(String str_to) {

        this.str_to = str_to;
    }
}
