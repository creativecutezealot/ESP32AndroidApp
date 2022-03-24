package com.nader.esp32connectionapp.Service;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nader.esp32connectionapp.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class APIService {
    public static final String RESPONSE_UNWANTED = "UNWANTED";
    private static final String LINE_FEED = "\r\n";
    private static final String twoHyphens = "--";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public static String POSTwithAuthSSLRegistration(String url, String live_image_file, String client_id) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");
        con.addRequestProperty("Authorization", "Basic cnR0YWRtaW46cHZ0UEBzc3cwcmQ=");
        con.setRequestProperty("Content-Type","application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patient_mobile_number", live_image_file);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodyStr = jsonObject.toString();
        Log.d("Body", bodyStr);
        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(bodyStr);
        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String uploadData(String url,int data) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type","application/json");
        con.setDoInput(true);
        con.setDoOutput(true);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Constant.token);
            jsonObject.put("edificeId", Constant.edificeId);
            jsonObject.put("deviceId", Constant.deviceId);
            jsonObject.put("fetchDate", Constant.fetchDate);
            JSONArray dataArr = new JSONArray();
            JSONObject dataObject1 = new JSONObject();
            dataObject1.put("value", data);
            dataObject1.put("sensorId",Constant.sensorId);
            dataArr.put(dataObject1);
            jsonObject.put("data",dataArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodyStr = jsonObject.toString();
        Log.d("Body", bodyStr);
        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(bodyStr);
        writer.close();
        outputStream.close();
        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String POSTCreateUserwithJsonUrl(String url, String name, String email, String password) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type","application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Name", name);
            jsonObject.put("Email", email);
            jsonObject.put("Password", password);
            jsonObject.put("Kind", "Uploader");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodyStr = jsonObject.toString();
        Log.d("Body", bodyStr);
        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(bodyStr);
        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }
        bufferResponse.close();
        return newResponse.toString();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String LoginUserwithJsonUrl(String url, String email, String password) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type","application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", email);
            jsonObject.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodyStr = jsonObject.toString();
        Log.d("Body", bodyStr);
        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(bodyStr);
        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }
        bufferResponse.close();
        return newResponse.toString();
    }



    public static String POSTGetDatawithJsonUrl(String url, String bluetoothID) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type","application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bluetoothID", bluetoothID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodyStr = jsonObject.toString();
        Log.d("Body", bodyStr);
        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write(bodyStr);
        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String POSTLoginwithformdataUrl(String url, String email, String password) throws IOException {
        trustEveryone();
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String boundary = "===" + System.currentTimeMillis() + "===";

        con.setRequestMethod("POST");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        con.addRequestProperty("Accept", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);
        OutputStream outputStream = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //first parameter - email
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"Email\"" + LINE_FEED + LINE_FEED
                + email + LINE_FEED);
        //second parameter - password
        writer.write(twoHyphens + boundary + LINE_FEED);
        writer.write("Content-Disposition: form-data; name=\"Password\"" + LINE_FEED + LINE_FEED
                + password + LINE_FEED);

        writer.close();
        outputStream.close();

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            int code = con.getResponseCode();
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }

    public static String GETWithNonceURL(String url) throws IOException {
        trustEveryone();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.addRequestProperty("Cache-Control", "no-cache");
        con.addRequestProperty("Content-Type", "application/json");
        con.addRequestProperty("Accept", "application/json");

        //Retrieving Data
        BufferedReader bufferResponse;
        if (con.getResponseCode() / 100 == 2) {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            bufferResponse = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String line;
        StringBuilder newResponse = new StringBuilder();
        while ((line = bufferResponse.readLine()) != null) {
            newResponse.append(line);
        }

        bufferResponse.close();
        return newResponse.toString();
    }
    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {

                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}