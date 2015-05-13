package edu.uw.spacescout_android;

/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;

import java.io.IOException;
import java.util.WeakHashMap;

import edu.uw.spacescout_android.model.Buildings;
import edu.uw.spacescout_android.model.Spaces;
import edu.uw.spacescout_android.util.JSONParser;
import edu.uw.spacescout_android.util.JSONProcessor;

/**
 * The first Activity to run.
 * Immediately calls SpaceMapFragment on startup.
 *
 */

public class MainActivity extends FragmentActivity {
    private final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    // TODO: Replace deprecated class
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private FragmentManager fragmentManager;
    private Fragment fragSpaceList;
    private SpaceMapFragment fragSpaceMap;

    public JSONParser jParser;
    public WeakHashMap<String, AlertDialog> alertDialogues;
    public WeakHashMap<String, Toast> toasts;
    public Buildings buildings;
    public Spaces spaces;
    public String campus;
    public AsyncTask getJson;

    NavMenuListAdapter mNavMenuAdapter;
    String[] navItemTitle;
    int[] navItemIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //set content to layout_main
        setContentView(R.layout.layout_main);

        //get the title
        mTitle = mDrawerTitle = getTitle();

        //Generate nav menu item title
        navItemTitle = new String[]{"All Spaces", "Filter Spaces", "Favorite Spaces"};
        navItemIcon = new int[]{R.drawable.nav_all_spaces, R.drawable.nav_search, R.drawable.nav_fav_spaces};

        //Locate drawer_layout and drawer ListView in layout_main.xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //pass string arrays to NavMenuListAdapter
        mNavMenuAdapter = new NavMenuListAdapter(this.getBaseContext(), navItemTitle, navItemIcon);

        //set the NavMenuListAdapter to the ListView
        mDrawerList.setAdapter(mNavMenuAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_title_actionbar, null);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Manteka.ttf");

        TextView titleSpace = (TextView) v.findViewById(R.id.titleSpace);
        TextView titleScout = (TextView) v.findViewById(R.id.titleScout);
        titleSpace.setTypeface(typeface);
        titleScout.setTypeface(typeface);

        getActionBar().setCustomView(v);

        // for use the REST section
        alertDialogues = new WeakHashMap<>();
        toasts = new WeakHashMap<>();
        jParser = new JSONParser(this);

        // TODO: Need to consider campus selection when settings is set up
        campus = "seattle";

        fragSpaceMap = new SpaceMapFragment();
        fragSpaceList = new SpaceListFragment();

        // If no saved state:
        // get buildings in a campus,
        // get spaces based on first position on campus & lay them,
        // & select the first item on the nav list.
        if (savedInstanceState == null) {
            // Default url is set in res > values > .xml that you should create yourself (and not add in git)
            String baseUrl = getResources().getString(R.string.baseUrl);
            connectToServer(baseUrl + "buildings?campus=" + campus, "buildings");

//            // Default position is set in res/values/config.xml
//            String centerLat = getResources().getString(R.string.default_center_latitude);
//            String centerLon = getResources().getString(R.string.default_center_longitude);
//
//            // From calculations, 220 is the (rounded-off) radiance distance
//            // of the default zoom level (17.2f)
//            String defaultUrl = baseUrl + "spot/?center_latitude=" + centerLat +
//                    "&center_longitude=" + centerLon + "&distance=220&open_now=true";
//            connectToServer(defaultUrl, "spaces");

            selectItem(0);
        }
    }

    // returns the prepared googlemap from SpaceMapFragment
    public GoogleMap getMap() {
        return fragSpaceMap.getMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

//        generalFrag = fragmentManager.findFragmentByTag("SPACE_LIST");
        if (fragSpaceList != null && !drawerOpen
                && fragSpaceList.isVisible()) {

            menu.findItem(R.id.action_space_list).setVisible(false);
        } else {
            menu.findItem(R.id.action_space_map).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        fragmentManager = getSupportFragmentManager();

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {

            //on click of space list action item
            case R.id.action_space_list:
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container, fragSpaceList, "SPACE_LIST");
                ft.addToBackStack("SPACE_LIST");
                ft.commit();
                invalidateOptionsMenu();
                return super.onOptionsItemSelected(item);

            case R.id.action_space_map:
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container, fragSpaceMap, "SPACE_MAP");
                ft.addToBackStack("SPACE_MAP");
                ft.commit();
                invalidateOptionsMenu();
                return super.onOptionsItemSelected(item);

            case R.id.action_search:
                Intent intent = new Intent(this, FilterSpacesActivity.class);
                startActivity(intent);
                mDrawerList.setItemChecked(1, true);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        fragmentManager = getSupportFragmentManager();
        Intent intent;
        // Locate Position
        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, fragSpaceMap, "SPACE_MAP").commit();
                invalidateOptionsMenu();
                break;
            case 1:
                intent = new Intent(this, FilterSpacesActivity.class);
                startActivity(intent);
                invalidateOptionsMenu();
                break;
            case 2:
                intent = new Intent(this, FavSpacesActivity.class);
                startActivity(intent);
                invalidateOptionsMenu();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "About SpaceScout", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerToggle.syncState();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_title_actionbar, null);

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/Manteka.ttf");

        TextView titleSpace = (TextView) v.findViewById(R.id.titleSpace);
        TextView titleScout = (TextView) v.findViewById(R.id.titleScout);
        titleSpace.setTypeface(typeface);
        titleScout.setTypeface(typeface);

        getActionBar().setCustomView(v);
    }

    // TODO: May need to consider making this into a class for use in other activities
    /* The REST is here */
    public void connectToServer(String url, String item) {
        // TODO: Check internet status
        getJson = new getJson(url, item).execute();
    }

    // Asynchronously get JSON data from API.
    // Requires URL for request & item ("buildings" or "spaces").
    // Sets global variables based on item string passed.
    private class getJson extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;
        private String url;
        private String item;
        protected int statusCode;

        public getJson(String url, String item) {
            this.url = url;
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(MainActivity.this);
//            this.pDialog.setMessage("Filling spaces");
//            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args){
            JSONArray json = getJSONFromUrl(url);
            statusCode = jParser.getStatusCode();

            return json;
        }

        // This is run instead of onPostExecute() if cancel(true) is called from outside
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "AsyncTask cancelled");
        }

        @Override
        protected void onPostExecute(JSONArray json) {
//            if (pDialog.isShowing())
//                pDialog.dismiss();

            handleHttpResponse(statusCode, json, url, item);
            if (json != null) {
                switch (item) {
                    case "spaces":
                        spaces = JSONProcessor.modelSpaces(json);
                        fragSpaceMap.DisplayClustersByDistance(spaces);
                        break;
                    case "buildings":
                        buildings = JSONProcessor.modelBuildings(json);
                        break;
                }
            }
        }
    }

    private JSONArray getJSONFromUrl(String url) {
        JSONArray json = new JSONArray();
        try {
            json = jParser.getJSONFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    // handle different status codes
    // only continue processing json if code 200 & json is not empty
    private void handleHttpResponse(int statusCode, JSONArray json, String url, String item) {
        switch (statusCode) {
            case 200:
                if (json != null) {
                    break;
                } else {
                    Toast toast = Toast.makeText(this, "Sorry, no spaces found", Toast.LENGTH_SHORT);
                    toasts.put("Sorry, no spaces found" ,toast);
                    toast.show();
                }
                break;
            case 401:
                showStatusDialog("Authentication Issue", "Check key & secret.", url, item);
                break;
            default:
                showStatusDialog("Connection Issue", "Can't connect to server. Status code: " +
                        statusCode + ".", url, item);
                break;
        }
    }

    // creates and shows a new dialog modal
    // let's user retry connecting to the server
    private void showStatusDialog(String title, String message, String url, String item) {
        final String theUrl = url;
        final String theItem = item;
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // retry
                        Log.d("oauth", "Retrying connection.");
                        connectToServer(theUrl, theItem);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dialogue = dialogueBuilder.create();
        alertDialogues.put(title, dialogue);
        dialogue.show();

        Log.d("oauth", "Showing dialogue " + title);
    }

    public AlertDialog getUsedDialogue(String key) {
        return alertDialogues.get(key);
    }
}