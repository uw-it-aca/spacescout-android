package edu.uw.spacescout_android.util;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.spacescout_android.model.Space;
import edu.uw.spacescout_android.model.Spaces;

/**
 * Created by aazri3 on 4/10/15.
 *
 * This class processes JSONArray into model objects.
 */
public class JSONProcessor {
    // Accepts a JSONArray
    // Returns a Spaces object (a List of Space objects)
    public static Spaces toModel(JSONArray json) {
        Spaces spaces = new Spaces();

        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject curr = json.getJSONObject(i);

                JSONObject location = curr.getJSONObject("location");
                int id = Integer.parseInt(curr.getString("id"));
                String name = curr.getString("name");
                double lng = Double.parseDouble(location.getString("longitude"));
                double lat = Double.parseDouble(location.getString("latitude"));

                Space space = new Space(id, lat, lng, name);

//                JSONObject info = curr.getJSONObject("extended_info");
//                String campus = info.getString("campus");

                spaces.add(space);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return spaces;
    }
}
