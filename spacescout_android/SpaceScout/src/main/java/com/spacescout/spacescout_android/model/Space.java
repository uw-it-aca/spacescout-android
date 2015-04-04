package com.spacescout.spacescout_android.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

/**
 * Created by gupta37 on 5/13/14.
 *
 * Item class for marker clustering.
 */

public class Space implements ClusterItem {
    private final int id;
    private final String name;
    private ArrayList<String> typeArray;
    private final LatLng mPosition;
    private double height_from_sea_level;
    private String building_name;
    private int floor;
    private String room_number;
    private String description;
    private String capacity;
    private String display_access_restrictions;
    private ArrayList<ImageInfo> images;
    private ArrayList<Hours> available_hours;
    private String organization;
    private String manager;
    // TODO: Dealing with multiple fields in "extended_info"
    private String last_modified;

    public Space(int id, double lat, double lng, String name) {
        this.id = id;
        mPosition = new LatLng(lat, lng);
        this.name = name;
    }

    /** classed items **/
    class ImageInfo {
        int id;
        String url;
        String content_type;
        int width;
        int height;
        String creation_date;
        String modification_date;
        String user_name;
        String upload_application;
        String thumbnail_root;
        String description;
        int display_index;
    }

    class Hours {
        String day;
        // TODO: need to think of having multiple hours

        Hours(String day) {
            this.day = day;
        }
    }

    /** Setters **/

    public void setBuilding(String buildingName) {
        this.building_name = buildingName;
    }

    public void setTypeArray(ArrayList<String> typeArray) {
        this.typeArray = typeArray;
    }

    /** Getters **/

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getBuilding() {
        return building_name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getTypeArray() {
        return typeArray;
    }

}