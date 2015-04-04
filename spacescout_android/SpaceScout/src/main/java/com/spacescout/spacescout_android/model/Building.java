package com.spacescout.spacescout_android.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gupta37 on 5/22/14.
 *
 * Building item for clustering.
 */

public class Building {
    private LatLng mPosition;
    private String mName;
    private int mSpots;

    public Building(LatLng position, String name, int spots){
        mPosition = position;
        mName = name;
        mSpots = spots;
    }

    public LatLng getPosition(){
        return mPosition;
    }

    public int getSpots(){
        return mSpots;
    }

    public String getName(){
        return mName;
    }

    public void increaseSpots(){
        mSpots += 1;
    }

}
