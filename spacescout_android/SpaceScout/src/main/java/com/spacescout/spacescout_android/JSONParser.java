package com.spacescout.spacescout_android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 * Created by ajay alfred on 11/5/13.
 */
public class JSONParser {
    static JSONArray jObj = null;
    static InputStream is = null;
    static String json = "";
    private Context appContext;

    // constructor
    public JSONParser(Context c) {
        appContext = c;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    // TODO: may need to break up this method
    // Params: request url
    // Returns: JSON object
    // Connects to the server with the url. Signs the request with signpost for OAuth 1.0.
    // Handled exceptions: HttpHostConnectException
    public JSONArray getJSONFromUrl(String url) throws IOException {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
//          OAuthConsumer consumer = new DefaultOAuthConsumer(
                appContext.getResources().getString(R.string.consumerKey),
                appContext.getResources().getString(R.string.consumerSecret));

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            consumer.sign(httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();
            Log.d("oauth", "Sending 'GET' request to URL : " + url);
            Log.d("oauth", "Response Code : " +
                    response.getStatusLine().getStatusCode());
        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            String errorMsg = "Can't connect to server. Probably down.";
            Log.d("oauth", errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        /* Aba: trying to implement httpURLConnection */
//        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//        int responseCode = 0;
//
//        try {
//            // optional default is GET
//            conn.setRequestMethod("GET");
//            consumer.sign(conn);
//            conn.connect();
//            responseCode = conn.getResponseCode();
//            Log.d("oauth" ,"Sending 'GET' request to URL : " + url);
//            Log.d("oauth" ,"Response Code : " + responseCode);
//        } catch (IOException e) {
//            responseCode = conn.getResponseCode();
//        } catch (OAuthCommunicationException | OAuthExpectationFailedException | OAuthMessageSignerException e) {
//            e.printStackTrace();
//        }

//        // handle 401 response or check if responseCode never changed
//        if (responseCode == 401) {
//            CharSequence text_401 = "Got a " + responseCode;
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(appContext, text_401, duration);
//            toast.show();
//            System.out.println(text_401);
//        } else if (responseCode == 0) {
//            CharSequence text_401 = "Response did not change!";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(appContext, text_401, duration);
//            toast.show();
//            System.out.println(text_401);
//        }
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(conn.getInputStream()));
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        while ((line = in.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        in.close();
//        json = sb.toString();

        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}