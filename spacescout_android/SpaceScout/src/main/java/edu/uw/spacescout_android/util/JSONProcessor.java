package edu.uw.spacescout_android.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import edu.uw.spacescout_android.model.Space;
import edu.uw.spacescout_android.model.Spaces;

/**
 * Created by aazri3 on 4/10/15.
 *
 * This class processes JSONArray into model objects.
 * Using "opt" for accessing json data returns a null if the key does not exist.
 */
public class JSONProcessor {
    // Accepts a JSONArray
    // Returns a Spaces object (a List of Space objects)
    public static Spaces modelSpaces(JSONArray json) {
        Spaces spaces = new Spaces();

        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject curr = json.getJSONObject(i);

                int id = Integer.parseInt(curr.getString("id"));
                String name = curr.getString("name");
                JSONObject location = curr.getJSONObject("location");
                double lng = location.getDouble("longitude");
                double lat = Double.parseDouble(location.getString("latitude"));

                Space space = new Space(id, lat, lng, name);

                JSONArray jsonTypes = curr.getJSONArray("type");
                ArrayList<String> types = new ArrayList<>();
                for (int j = 0; j < jsonTypes.length(); j++) {
                    types.add(jsonTypes.getString(j));
                }
                space.setTypes(types);

                if (location.get("height_from_sea_level") != JSONObject.NULL)
                    space.setHeight_from_sea_level(location.getDouble("height_from_sea_level"));
                space.setBuilding(location.getString("building_name"));
                space.setFloor(location.getString("floor"));
                space.setRoom_number(location.getString("room_number"));
                if (location.opt("capacity") != null && location.opt("capacity") != JSONObject.NULL)
                    space.setCapacity(curr.getInt("capacity"));
                space.setDisplay_access_restrictions(curr.getString("display_access_restrictions"));
                space.setOrganization(curr.getString("organization"));
                space.setManager(curr.getString("manager"));
                space.setLast_modified(curr.getString("last_modified"));

                if (curr.get("images") != null) {
                    JSONArray jsonImages = curr.getJSONArray("images");
                    ArrayList<Space.ImageInfo> images = new ArrayList<>();
                    for (int k = 0; k < jsonImages.length(); k++) {
                        JSONObject jsonImage = jsonImages.getJSONObject(k);
                        Space.ImageInfo image = new Space.ImageInfo(jsonImage.getInt("id"));
                        image.content_type = jsonImage.optString("content_type");
                        image.width = jsonImage.getInt("width");
                        image.height = jsonImage.getInt("height");

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                        try {
                            image.creation_date = formatter.parse(jsonImage.getString("creation_date"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        image.user_name = jsonImage.optString("user_name");
                        image.thumbnail_root = jsonImage.getString("thumbnail_root");
                        image.description = jsonImage.getString("description");

                        images.add(image);
                    }
                    space.setImages(images);
                }

                // TODO: Test parsing hours and extended_info
                Map<String, Space.Hours> availableHours = new HashMap<>();
                String[] days = new String[]{"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
                JSONObject allHours = curr.getJSONObject("available_hours");
                for (String day : days) {
                    JSONArray hoursByDay = allHours.getJSONArray(day);
                    ArrayList<Date[]> hours = new ArrayList<>();
                    for (i = 0; i > hoursByDay.length(); i++) {
                        Date[] window = new Date[2];
                        JSONArray hour = hoursByDay.getJSONArray(i);
                        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
                        try {
                            window[0] = formatter.parse(hour.getString(0));
                            window[1] = formatter.parse(hour.getString(1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        hours.add(window);
                    }
                    availableHours.put(day, new Space.Hours(hours));
                }
                space.setAvailable_hours(availableHours);

                Map<String, Object> extendedInfo = new HashMap<>();
                JSONObject jsonInfo = curr.getJSONObject("extended_info");
                Iterator<String> info = jsonInfo.keys();
                while (info.hasNext()) {
                    String key = info.next();
                    extendedInfo.put(key, jsonInfo.get(key));
                }
                space.setExtended_info(extendedInfo);

                spaces.add(space);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return spaces;
    }
    // TODO: model Buildings
//    public static Buildings modelBuildings(JSONArray json) {
//        Buildings buildings = new Buildings();
//        for (int i = 0; i > json.length(); i++) {
//            try {
//                buildings.add(json.getString(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return buildings;
//    }
}
