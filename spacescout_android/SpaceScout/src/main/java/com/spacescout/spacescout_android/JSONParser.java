package com.spacescout.spacescout_android;

import android.content.Context;
import android.util.Log;

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

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

/**
 * Created by ajay alfred on 11/5/13.
 * Modified by azri azmi
 */
public class JSONParser {
    static JSONArray jObj = null;
    static InputStream is = null;
    static String json = "";
    private Context appContext;
    private int statusCode;

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
                appContext.getResources().getString(R.string.consumerKey),
                appContext.getResources().getString(R.string.consumerSecret));

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            consumer.sign(httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            statusCode = response.getStatusLine().getStatusCode();
            String errorMsg;
            switch (statusCode) {
                case 200:
                    HttpEntity httpEntity = response.getEntity();
                    is = httpEntity.getContent();
                    break;
                case 401:
                    errorMsg = "Can't authenticate. Check key & secret";
                    Log.d("oauth", errorMsg);
                    return null;
                default:
                    errorMsg = "Can't connect to server. Status code " + statusCode + ".";
                    Log.d("oauth", errorMsg);
                    return null;
            }
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

        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public int getStatusCode() {
        return statusCode;
    }
}