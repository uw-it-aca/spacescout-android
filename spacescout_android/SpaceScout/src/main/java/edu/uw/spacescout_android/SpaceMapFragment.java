package edu.uw.spacescout_android;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.google.maps.android.ui.IconGenerator;
import edu.uw.spacescout_android.TouchableWrapper.UpdateMapAfterUserInteraction;
import edu.uw.spacescout_android.model.Building;
import edu.uw.spacescout_android.model.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Created by ajay alfred on 11/5/13.
 * Modified by azri92.
 *
 * This class displays spaces on a Google map and its markers/clusters.
 * Extends a fragment that is embedded onto MainActivity.
 * Implements UpdateMapAfterUserInteraction for callback ethod for Map touch or zoom change.
 * Implements OnMapReadyCallback for callback method for preparing the map.
 *
 */

public class SpaceMapFragment extends Fragment implements UpdateMapAfterUserInteraction, OnMapReadyCallback {

    static final LatLng UnivWashington = new LatLng(47.655263166697765, -122.30669233862307);

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private View view;

    public TouchableWrapper mTouchView;
    public static boolean mMapIsTouched = false;
    public float zoomLevel = 0;
    public IconGenerator tc;
    public ClusterManager<Space> mClusterManager;
//    public JSONArray mJson;

    public SpaceMapFragment() {
        ///empty constructor required for fragment subclasses
    }

    // This is the default method needed for Android Fragments
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null) { // azri92: not sure if this actually helps
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        tc = new IconGenerator(getActivity());

        // this is how you access methods in parent activity
        ((MainActivity) getActivity()).testMethod();
//        mJson = ((MainActivity) getActivity()).mJson;

    }

    @Override
    // disables map zoom controls and rotation gesture
    public void onMapReady(GoogleMap map) {
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(UnivWashington, 17.2f));
    }

    // This method is being implemented as part of the interface UpdateMapAfterUserInteraction
    // This is essentially a callback for touch event on the map
    // TODO: send new request to server when the user moves around the map
    public void onUpdateMapAfterUserInteraction() {
//        ((MainActivity) getActivity()).connectToServer();
    }

    // This is the default method needed for Android Frafments
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        super.onCreate(bundle);
        if(view == null)
            view = inflater.inflate(R.layout.fragment_space_map, container, false);

        mTouchView = new TouchableWrapper(getActivity());
        // TODO: need to avoid restarting fragment on backpress
        if(view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        mTouchView.addView(view);
        return mTouchView;
    }

    // This is the default method needed for Android Fragments
    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
    }

    // Setting up a Map
    private void setUpMap() {
        if (map != null)
            return;
        map = mapFragment.getMap();
        if(map == null)
            return;
    }

    // This method displays the clusters on the map by clustering by distance
    // It takes the json data as parameter
    public void DisplayClustersByDistance(JSONArray mJson){

        // Setting up cluster manager with the CustomClusteringAlgorithm Class
        setUpClusterer();
        mClusterManager.setAlgorithm(new PreCachingAlgorithmDecorator<>(new CustomClusteringAlgorithm<Space>()));
        //TODO: Use CustomRenderer to set minimum cluster size
        // mClusterManager.setRenderer(new CustomeRenderer<>(this, mMap, mClusterManager));

        // Use to create a minimum bound based on a set of LatLng points.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        try {
            // Looping through JSON Data to add it to the Cluster Manager
            for(int i = 0; i < mJson.length(); i++){
                JSONObject curr = mJson.getJSONObject(i);
                JSONObject info = curr.getJSONObject("extended_info");

                JSONObject location = curr.getJSONObject("location");
                double lng = Double.parseDouble(location.getString("longitude"));
                double lat = Double.parseDouble(location.getString("latitude"));
                int id = Integer.parseInt(curr.getString("id"));
                String name = curr.getString("name");
                String campus = info.getString("campus");

                if(campus.equals("seattle")){
                    LatLng currLoc = new LatLng(lat, lng);
                    mClusterManager.addItem(new Space(id, lat, lng, name));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Setting up the ClusterManager which would contain all the clusters
    // This is only used by DisplayClustersByDistance() method
    public void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this.getActivity(), map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

    }

    // This method displays the clusters on the map by clustering by Building Names
    // It takes the json data as parameter
    public void DisplayClustersByBuilding(JSONArray mJson){

        // HashMap to keep track of all buildings with their building objects
        HashMap<String, Building> building_cluster = new HashMap<String, Building>();
        try {

            // Builder object to build bound for all clusters/markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            // Looping through all json data
            // TODO: this section will use model objects instead of json
            for(int i = 0; i < mJson.length(); i++){
                JSONObject curr = mJson.getJSONObject(i);
                JSONObject info = curr.getJSONObject("extended_info");

                JSONObject location = curr.getJSONObject("location");
                double lng = Double.parseDouble(location.getString("longitude"));
                double lat = Double.parseDouble(location.getString("latitude"));
                String name = curr.getString("name");
                String building_name = location.getString("building_name");
                String campus = info.getString("campus");

                if(campus.equals("seattle") && (!building_cluster.containsKey(building_name))){
                    LatLng currLoc = new LatLng(lat, lng);

                    // Creating Building Objects with location, building name
                    Building buil = new Building(currLoc, building_name, 1);
                    building_cluster.put(building_name, buil);
                    builder.include(currLoc);
                }else if(building_cluster.containsKey(building_name)){

                    // Increasing the number of spots in the current building
                    Building temp = building_cluster.get(building_name);
                    temp.increaseSpots();
                }
            }

            // Iterating through the hashmap of all buildings to add to the maps
            Iterator it = building_cluster.keySet().iterator();
            while (it.hasNext()) {
                String key = (String)it.next();
                Building b = building_cluster.get(key);
                addMarkerToMap(b.getPosition(), b.getSpots());
            }

            LatLngBounds bounds = builder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // This adds a marker to the map with IconGenerator class
    // The method takes the LatLng object location and text to be put on the marker/cluster as an Integer
    protected void addMarkerToMap(LatLng loc, int text) {
        IconGenerator iconFactory = new IconGenerator(getActivity());
        addIcon(iconFactory, Integer.toString(text), loc);
    }

    // This is the helper method for adding a marker to the map
    // This is invoked by addMarkerToMap
    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        Bitmap bmp = iconFactory.makeIcon(text);
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(bmp)).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        map.addMarker(markerOptions);
    }

}
