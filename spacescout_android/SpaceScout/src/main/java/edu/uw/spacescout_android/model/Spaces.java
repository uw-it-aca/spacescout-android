package edu.uw.spacescout_android.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by aazri3 on 4/1/15.
 */
public class Spaces {
    private final String name;
    private final LatLng position;
    private final String buildingName;


    public Spaces(String name, LatLng position, String buildingName) {
        this.name = name;
        this.position = position;
        this.buildingName = buildingName;
    }

    public String getName() {
        return name;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getBuildingName() {
        return buildingName;
    }
}
