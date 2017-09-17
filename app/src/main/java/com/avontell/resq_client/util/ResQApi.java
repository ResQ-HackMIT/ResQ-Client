package com.avontell.resq_client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * API hooks for the ResQ-Server
 */
public class ResQApi {

    public static final String url = "http://35.196.47.57";
    public static final String LOCATION = "/location";
    public static final String RESPONDER = "/api/auth/firstresponder";
    public static final String TRIAGE = "/api/triage";
    public static final String USER = "/api/auth/user";

    public static final String ACCOUNT_INFO_KEY = "ACCOUNTINFOKEY";
    public static final String ACCOUNT_AUTH_KEY = "ACCOUNTAUTHKEY";
    public static final String SHARED_PREFS = "ACCOUNTAUTHKEY";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static boolean createAccount(Context context,
                                        String name,
                                        String[] medicalConditions,
                                        String[] allergies,
                                        String[] medications,
                                        int weight,
                                        int age,
                                        int height,
                                        int kids,
                                        int animals,
                                        boolean spouse,
                                        boolean transportation,
                                        boolean evacuate) {

        try {

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("medicalConditions", medicalConditions.length);
            data.put("allergies", allergies.length);
            data.put("medications", medications.length);
            data.put("weight", weight);
            data.put("height", height);
            data.put("age", age);
            data.put("animals", animals);
            data.put("kids", kids);
            data.put("spouse", spouse);
            data.put("transportation", transportation);
            data.put("evacuate", evacuate);

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, data.toString());
            Request request = new Request.Builder()
                    .url(url + USER)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject result = new JSONObject(responseString);
            Log.e("ACCOUNT", responseString);
            if (result.has("success") && result.getString("success").equals("true")) {
                saveToPrefs(context, ACCOUNT_INFO_KEY, data.toString());
                saveToPrefs(context, ACCOUNT_AUTH_KEY, result.getString("authorizationKey"));
            } else {
                Log.e("ACCOUNT CREATION FAILED", "" + result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }


    public static boolean updateLocation(Context context, Location location) {

        double lon = location.getLongitude();
        double lat = location.getLatitude();

        try {

            JSONObject data = new JSONObject();
            data.put("lat", lat);
            data.put("long", lon);

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, data.toString());
            Request request = new Request.Builder()
                    .url(url + USER)
                    .addHeader("Authorization", getFromPrefs(context, ACCOUNT_AUTH_KEY, "null"))
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject result = new JSONObject(responseString);
            Log.e("LOCATION UPDATE", responseString);
            if (result.has("success") && result.getString("success").equals("true")) {
            } else {
                Log.e("ACCOUNT CREATION FAILED", "" + result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    private static void saveToPrefs(Context context, String key, String value) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString(key, value);
        edit.apply();

    }

    private static String getFromPrefs(Context context, String key, String def) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString(key, def);
    }

}
