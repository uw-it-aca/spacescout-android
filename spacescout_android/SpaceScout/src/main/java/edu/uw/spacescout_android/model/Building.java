package edu.uw.spacescout_android.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gupta37 on 5/22/14.
 *
 * Building item for clustering.
 */

public class Building {
    private final String mName;
    private LatLng mPosition;
    private int mSpots;

    public Building(String name){
        mName = name;
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
