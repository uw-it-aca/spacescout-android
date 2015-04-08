package com.spacescout.spacescout_android.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gupta37 on 5/13/14. Modified by aazri3/azri92.
 *
 * Item class for marker clustering.
 */

public class Space implements ClusterItem {
    private final int id;
    private final String name;
    private ArrayList<String> types;
    private final LatLng mPosition;
    private double height_from_sea_level;
    private String building_name;
    private int floor;
    private String room_number;
    private String description;
    private String capacity;
    private String display_access_restrictions;
    private List<ImageInfo> images;
    private Map<String, Hours> available_hours;
    private String organization;
    private String manager;
    private Map<String, Object> extended_info;
    private String last_modified;
//    private boolean isOpen;

    public Space(int id, double lat, double lng, String name) {
        this.id = id;
        mPosition = new LatLng(lat, lng);
        this.name = name;
        images = new ArrayList<>();
        available_hours = new HashMap<>();
        extended_info = new HashMap<>();
    }

    /** classed items **/

    protected class ImageInfo {
        protected int id;
        protected String url;
        protected String content_type;
        protected int width;
        protected int height;
        protected String creation_date;
        protected String modification_date;
        protected String user_name;
        protected String upload_application;
        protected String thumbnail_root;
        protected String description;
        protected int display_index;
    }

    // You would parse through this in a for loop, calling the methods
    protected class Hours {
        // TODO: need to think of having multiple hours
        protected ArrayList<Date[]> hours;

        public Date startTime(int i) throws ParseException {
            if (i > hours.size())
                throw new NullPointerException("This space does not have that many hours");

//            String[] array = hours.get(i);
//            String hour = array[0];
//            DateFormat formatter = new SimpleDateFormat("HH:mm");
//            return formatter.parse(hour);

            return hours.get(i)[0];
        }
        public Date endTime(int i) throws ParseException {
            if (i > hours.size())
                throw new NullPointerException("This space does not have that many hours");

//            String[] array = hours.get(i);
//            String hour = array[1];
//            DateFormat formatter = new SimpleDateFormat("HH:mm");
//            return formatter.parse(hour);
            return hours.get(i)[1];
        }
    }

    /** Getters **/

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public double getHeight_from_sea_level() {
        return height_from_sea_level;
    }

    public String getBuilding() {
        return building_name;
    }

    public int getFloor() {
        return floor;
    }

    public String getRoom_number() {
        return room_number;
    }

    public String getDescription() {
        return description;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getDisplay_access_restrictions() {
        return display_access_restrictions;
    }

    public List<ImageInfo> getImages() {
        return images;
    }

    public Map<String, Hours> getAvailable_hours() {
        return available_hours;
    }

    public String getOrganization() {
        return organization;
    }

    public String getManager() {
        return manager;
    }

    public Map<String, Object> getExtended_info() {
        return extended_info;
    }

    public String getLast_modified() {
        return last_modified;
    }


    /** Setters **/

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public void setHeight_from_sea_level(double height_from_sea_level) {
        this.height_from_sea_level = height_from_sea_level;
    }

    public void setBuilding(String buildingName) {
        this.building_name = buildingName;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setDisplay_access_restrictions(String display_access_restrictions) {
        this.display_access_restrictions = display_access_restrictions;
    }

    public void setImages(ArrayList<ImageInfo> images) {
        this.images = images;
    }

    public void setAvailable_hours(Map<String, Hours> available_hours) {
        this.available_hours = available_hours;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setExtended_info(Map<String, Object> extended_info) {
        this.extended_info = extended_info;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    /** other methods **/
    public boolean isOpen() {
        // TODO: set isOpen
        // Should be checked the instant it's asked because time
        // is not constant.
        return true;
    }
}