package com.spacescout.spacescout_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class SpaceListActivity extends Activity{

    //URL to get JSON Array
    private static String url = "http://almond.eplt.washington.edu:8000/api/v1/spot/76";

    //Setting variables
    TextView jsonText = null;

    //JSON Array
    JSONArray info = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_space_list);

        Log.i("INFO","Got this far");

        getActionBar().setDisplayHomeAsUpEnabled(true);

        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("INFO","Pre-executing");

            //get TextView
            jsonText = (TextView)findViewById(R.id.json_txt);

            pDialog = new ProgressDialog(SpaceListActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            Log.i("INFO","In background...");
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            Log.i("INFO","Fetching from URL...");
            JSONObject json = jParser.getJSONFromUrl(url);
            Log.i("INFO","Fetch COMPLETE...");
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            Log.i("INFO","Dismissing prog dialog...");
            pDialog.dismiss();

            try {
                // Getting JSON Array
                JSONObject info = json.getJSONObject("location");

                String toDisplay = "name -> "+info.getString("building_name")+"\n";
                toDisplay += "floor -> "+info.getString("floor")+"\n";
                toDisplay += "lat -> "+info.getString("latitude")+"\n";
                toDisplay += "long -> "+info.getString("longitude")+"\n";

                jsonText.setText(toDisplay);

//                JSONObject c = info.getJSONObject(0);
//
//                // Storing  JSON item in a Variable
//                String id = c.getString(TAG_ID);
//                String name = c.getString(TAG_NAME);
//                String email = c.getString(TAG_EMAIL);
//
//                //Set JSON Data in TextView
//                uid.setText(id);
//                name1.setText(name);
//                email1.setText(email);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

        }
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
