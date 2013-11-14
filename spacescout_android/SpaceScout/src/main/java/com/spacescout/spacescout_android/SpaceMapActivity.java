package com.spacescout.spacescout_android;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class SpaceMapActivity extends Fragment{

    public SpaceMapActivity() {
        ///empty constructor required for fragment subclasses
    }

    static final LatLng UnivWashington = new LatLng(47.655263166697765, -122.30669233862307);
    private GoogleMap map;
    private MapFragment mapFragment;
    private FragmentManager fm;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        super.onCreate(bundle);

        if(view == null)
            view = inflater.inflate(R.layout.fragment_space_map, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
    }


    private void setUpMap() {
        if (map != null)
            return;
        map = mapFragment.getMap();
        if(map == null)
            return;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fm = getFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        if(mapFragment == null)
        {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        map = mapFragment.getMap();

//        map.setMyLocationEnabled(true);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);


        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions()
                .position(UnivWashington)
                .title("University of Washington")
                .snippet("Students: 1234")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(UnivWashington, 15.5f));



    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
