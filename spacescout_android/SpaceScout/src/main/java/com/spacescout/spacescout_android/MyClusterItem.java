package com.spacescout.spacescout_android;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by gupta37 on 5/13/14.
 */

public class MyClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private final String building_name;

    public MyClusterItem(double lat, double lng, String name) {
        mPosition = new LatLng(lat, lng);
        building_name = name;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getBuilding() {
        return building_name;
    }

}