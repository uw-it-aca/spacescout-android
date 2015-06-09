package edu.uw.spacescout_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.google.maps.android.ui.IconGenerator;

import edu.uw.spacescout_android.model.Space;
import edu.uw.spacescout_android.model.Spaces;

/**
 * Created by ajay alfred on 11/5/13.
 * Modified by azri92.
 *
 * This class displays spaces on a Google map and its markers/clusters.
 * Implements OnMapReadyCallback for callback method for preparing the map.
 * Requests to server is initiated in onCameraChange within CustomClusterRenderer
 */

public class SpaceMapFragment extends Fragment implements OnMapReadyCallback,
         ClusterManager.OnClusterClickListener<Space>, ClusterManager.OnClusterItemClickListener<Space> {
    private final String TAG = "SpaceMapFragment";

    // TODO: Should change based on User's preference (campus)
    private LatLng campusCenter;

    private GoogleMap map;
    private View view;
    private ClusterManager<Space> mClusterManager;
    private CustomClusterRenderer mClusterRenderer;

    public TouchableWrapper mTouchView;
    public IconGenerator tc;

    public PolylineOptions line;

    public SpaceMapFragment() {
        ///empty constructor required for fragment subclasses
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        setRetainInstance(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null) { // azri92: not sure if this actually helps
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        campusCenter = new LatLng(Float.parseFloat(getResources().getString(R.string.default_center_latitude)),
                Float.parseFloat(getResources().getString(R.string.default_center_longitude)));

//        tc = new IconGenerator(getActivity());
    }

    @Override
    // Map zoom controls and rotation gesture disabled.
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setUpClustererAndCenterMap();
    }

    public GoogleMap getMap() {
        return map;
    }

    // Setting up the ClusterManager which would contain all the clusters.
    // Sets custom cluster renderer and algorithm.
    public void setUpClustererAndCenterMap() {
        Log.d(TAG, "setUpClustererAndCenterMap fired");
        if (mClusterManager == null) {
            // Initialize the manager with the context and the map.
            mClusterManager = new ClusterManager<>(getActivity(), map);
            mClusterRenderer = new CustomClusterRenderer(getActivity(), map, mClusterManager);
            // To enable OnCameraChange in cluster renderer
            map.setOnCameraChangeListener(mClusterRenderer);
//            map.setOnCameraChangeListener(mClusterManager); // use this instead to also enable re-clustering on zoom
            // To enable marker/cluster listeners in ClusterManager
            map.setOnMarkerClickListener(mClusterManager);
            mClusterManager.setRenderer(mClusterRenderer); // use a our custom renderer
            mClusterManager.setOnClusterClickListener(this); // to override onClusterClick
            mClusterManager.setOnClusterItemClickListener(this); // to override onClusterItemClick
            // use our custom algorithm to cluster markers
            mClusterManager.setAlgorithm(new PreCachingAlgorithmDecorator<>(new CustomClusteringAlgorithm<>()));
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(campusCenter, 17.2f));
    }

    // Called when a cluster marker is clicked
    @Override
    public boolean onClusterClick(Cluster cluster) {
        // TODO: Go to list view - pass the cluster items (Spaces)
        return true;
    }

    // Called when a cluster item (one marker) is clicked
    @Override
    public boolean onClusterItemClick(Space space) {
        // TODO: Implement savedInstanceState to save last center coords before moving on to another fragment
        Intent next = new Intent(getActivity(), SpaceDetailsActivity.class);
        next.putExtra("space", space);
        startActivity(next);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadein);

        // TODO: Maybe instead of removing them, we should save them & avoid making a request when arriving back from another fragment
        // clear map of all markers, etc
//        map.clear();

        return true;
    }

    // for use by getJson asynctask in MainActivity
    public void sendPostCancelRequest() {
        mClusterRenderer.buildAndSendRequest();
    }

    // Implement TouchableWrapper if you want to use gesture recognition
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        super.onCreate(bundle);

        if(view == null)
            view = inflater.inflate(R.layout.fragment_space_map, container, false);

//        mTouchView = new TouchableWrapper(getActivity());
        // TODO: need to avoid restarting fragment on backpress
        if(view.getParent() != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
//        mTouchView.addView(view);
//        return mTouchView;
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume called");
////        setUpMap();
//    }

//    // Setting up a Map
//    private void setUpMap() {
//        if (map != null)
//            return;
//        map = mapFragment.getMap();
//        if(map == null)
//            return;
//    }

    // TODO: save current conditions here to be reloaded
//    @Override
//    public void onSaveInstanceState (Bundle savedInstanceState) {
//
//    }

    // This method displays the clusters on the map by clustering by distance
    // It takes the json data as parameter
    public void DisplayClustersByDistance(Spaces spaces){
        mClusterManager.clearItems();

        // Looping through Spaces model to obtain each Space
        for(int i = 0; i < spaces.size(); i++){
            mClusterManager.addItem(spaces.get(i));
        }
        mClusterManager.cluster();
    }

    public void disableMap(boolean disable) {
        if (map != null) {
            if (disable) {
                map.getUiSettings().setAllGesturesEnabled(false);
            } else {
                map.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

//    // TODO: Remove this method and integrate clustering by building in clustering
//    // This method displays the clusters on the map by clustering by Building Names
//    // It takes the json data as parameter
//    public void DisplayClustersByBuilding(JSONArray mJson){
//
//        // HashMap to keep track of all buildings with their building objects
//        HashMap<String, Building> building_cluster = new HashMap<String, Building>();
//        try {
//
//            // Builder object to build bound for all clusters/markers
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//            // Looping through all json data
//            for(int i = 0; i < mJson.length(); i++){
//                JSONObject curr = mJson.getJSONObject(i);
//                JSONObject info = curr.getJSONObject("extended_info");
//
//                JSONObject location = curr.getJSONObject("location");
//                double lng = Double.parseDouble(location.getString("longitude"));
//                double lat = Double.parseDouble(location.getString("latitude"));
//                String name = curr.getString("name");
//                String building_name = location.getString("building_name");
//                String campus = info.getString("campus");
//
//                if(campus.equals("seattle") && (!building_cluster.containsKey(building_name))){
//                    LatLng currLoc = new LatLng(lat, lng);
//
//                    // Creating Building Objects with location, building name
//                    Building buil = new Building(building_name);
//                    building_cluster.put(building_name, buil);
//                    builder.include(currLoc);
//                }else if(building_cluster.containsKey(building_name)){
//
//                    // Increasing the number of spots in the current building
//                    Building temp = building_cluster.get(building_name);
//                    temp.increaseSpots();
//                }
//            }
//
//            // Iterating through the hashmap of all buildings to add to the maps
//            for (String key : building_cluster.keySet()) {
//                Building b = building_cluster.get(key);
//                addMarkerToMap(b.getPosition(), b.getSpots());
//            }
//
//            LatLngBounds bounds = builder.build();
//            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // This adds a marker to the map with IconGenerator class
//    // The method takes the LatLng object location and text to be put on the marker/cluster as an Integer
//    protected void addMarkerToMap(LatLng loc, int text) {
//        IconGenerator iconFactory = new IconGenerator(getActivity());
//        addIcon(iconFactory, Integer.toString(text), loc);
//    }
//
//    // This is the helper method for adding a marker to the map
//    // This is invoked by addMarkerToMap
//    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
//        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
//        Bitmap bmp = iconFactory.makeIcon(text);
//        MarkerOptions markerOptions = new MarkerOptions().
//                icon(BitmapDescriptorFactory.fromBitmap(bmp)).
//                position(position).
//                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
//
//        map.addMarker(markerOptions);
//    }

}
