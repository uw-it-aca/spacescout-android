package edu.uw.spacescout_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

/**
 * Created by ajay alfred on 11/5/13.
 *
 * Displays user's favorite spaces.
 * Currently uses dummy data.
 */

public class FavSpacesActivity extends Activity {

    //URL to get JSON Array
    private static String url = "http://students.washington.edu/ajalfred/space_scout/space_76.json";

    //Setting variables
    private TextView jsonText = null;
    private View view;
    public String spaceTitles[];

    //JSON Array
    private JSONArray info = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_pagetitle_actionbar, null);

        TextView pageTitle = (TextView) v.findViewById(R.id.pageTitle);
        pageTitle.setText("FAVORITE SPACES");

        getActionBar().setCustomView(v);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //set layout view
        setContentView(R.layout.layout_space_favs);

//        new JSONParse().execute();

        spaceTitles = new String[]{"Commons Room 106","Commons Room 108", "Commons Room 107",
                "Commons Room 110", "Commons Room 111", "Commons Room 122"};

        ListView listView = (ListView) findViewById(R.id.lvSpaceList);

        SpaceListArrayAdapter spaceListArrayAdapter = new SpaceListArrayAdapter(this, spaceTitles);
        listView.setAdapter(spaceListArrayAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.i("info","Item -> "+spaceTitles[i]);
//                Intent intent = new Intent(getBaseContext(), SpaceDetailsActivity.class);
//                intent.putExtra("spaceTitle",spaceTitles[i]);
//                startActivity(intent);
//            }
//        });
    }

//    private class JSONParse extends AsyncTask<String, String, JSONObject> {
//        private ProgressDialog pDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //get TextView
//            jsonText = (TextView)findViewById(R.id.json_txt);
//
//            pDialog = new ProgressDialog(SpaceListActivity.this);
//            pDialog.setMessage("Getting Data ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            JSONParser jParser = new JSONParser();
//
//            // Getting JSON from URL
//            JSONObject json = jParser.getJSONFromUrl(url);
//            return json;
//        }
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            pDialog.dismiss();
//
//            try {
//                // Getting JSON Array
//                JSONObject info = json.getJSONObject("location");
//
//                String toDisplay = "name -> "+info.getString("building_name")+"\n";
//                toDisplay += "floor -> "+info.getString("floor")+"\n";
//                toDisplay += "lat -> "+info.getString("latitude")+"\n";
//                toDisplay += "long -> "+info.getString("longitude")+"\n";
//
//                jsonText.setText(toDisplay);
//
////                JSONObject c = info.getJSONObject(0);
////
////                // Storing  JSON item in a Variable
////                String id = c.getString(TAG_ID);
////                String name = c.getString(TAG_NAME);
////                String email = c.getString(TAG_EMAIL);
////
////                //Set JSON Data in TextView
////                uid.setText(id);
////                name1.setText(name);
////                email1.setText(email);
//            }
//            catch(JSONException e){
//                e.printStackTrace();
//            }
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
