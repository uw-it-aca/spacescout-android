package com.spacescout.spacescout_android;

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


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private FragmentManager fragmentManager;
    private Fragment fragSpaceList = new SpaceListActivity();
    private Fragment fragFavSpaces = new FavSpacesActivity();
    private Fragment fragSpaceMap = new SpaceMapActivity();
    private Fragment generalFrag;

    NavMenuListAdapter mNavMenuAdapter;
    String[] navItemTitle;
    int[] navItemIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        //set content to layout_main
        setContentView(R.layout.layout_main);

        //get the title
        mTitle = mDrawerTitle = getTitle();

        //Generate nav menu item title
        navItemTitle = new String[]{"All Spaces", "Filter Spaces", "My Favorite Spaces"};

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

        if (savedInstanceState == null) {
            selectItem(0);
        }
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
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        menu.findItem(R.id.action_space_list).setVisible(!drawerOpen);
        menu.findItem(R.id.action_space_map).setVisible(!drawerOpen);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);

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
    public boolean onOptionsItemSelected(MenuItem item) {

        fragmentManager = getFragmentManager();

        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {

            //on click of space list action item
            case R.id.action_space_list:
                fragmentManager.beginTransaction().replace(R.id.container, fragSpaceList, "SPACE_LIST").commit();
                invalidateOptionsMenu();
                return super.onOptionsItemSelected(item);

            case R.id.action_space_map:
                fragmentManager.beginTransaction().replace(R.id.container, fragSpaceMap, "SPACE_MAP").commit();
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

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        fragmentManager = getFragmentManager();

        // Locate Position
        switch (position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, fragSpaceMap, "SPACE_MAP").commit();
                invalidateOptionsMenu();
                break;
            case 1:
                Intent intent = new Intent(this, FilterSpacesActivity.class);
                startActivity(intent);
                invalidateOptionsMenu();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, fragFavSpaces, "FAV_SPACES").commit();
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
    }

}