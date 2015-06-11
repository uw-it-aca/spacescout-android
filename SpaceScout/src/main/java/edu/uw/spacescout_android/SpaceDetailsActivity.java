package edu.uw.spacescout_android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uw.spacescout_android.model.Space;


public class SpaceDetailsActivity extends Activity {
    private static final String TAG = "SpaceDetailsActivity";

    private Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_space_details);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_pagetitle_actionbar, null);

        TextView pageTitle = (TextView) v.findViewById(R.id.pageTitle);
        pageTitle.setText("SPACE DETAILS");

        getActionBar().setCustomView(v);

        setContentView(R.layout.layout_space_details);

        space = (Space) getIntent().getSerializableExtra("space");

        ImageView imgView = (ImageView) findViewById(R.id.spaceCarouselImage);
        ImageView imgHeart = (ImageView) findViewById(R.id.spaceFavIcon);
        TextView txtTitle = (TextView) findViewById(R.id.spaceTitle);
        TextView txtType = (TextView) findViewById(R.id.spaceType);
        TextView txtCapacity = (TextView) findViewById(R.id.spaceCapacity);
        ImageView imgCapacity = (ImageView) findViewById(R.id.image_capacity);
        TextView txtDesc = (TextView) findViewById(R.id.locationDesc);
        TextView txtMapsLink = (TextView) findViewById(R.id.mapsLink);
        // TODO: Currently, the available_hours textviews are set in LinearLayouts. May want to programmatically add them in or hide/unhide
        LinearLayout layoutResources = (LinearLayout) findViewById(R.id.layout_resources);
        LinearLayout layoutFood = (LinearLayout) findViewById(R.id.layout_food);
        LinearLayout layoutNoise = (LinearLayout) findViewById(R.id.layout_noise);
        LinearLayout layoutLight = (LinearLayout) findViewById(R.id.layout_light);

        txtTitle.setText(space.getName());

        // Grabbing the types and their matching text
        String[] arrayRaw = getResources().getStringArray(R.array.space_type_key);
        String[] arrayTypes = getResources().getStringArray(R.array.space_type_list);
        Map<String, String> arrayMap = new HashMap<>();
        for (int i = 0; i < arrayTypes.length; i++) {
            arrayMap.put(arrayRaw[i], arrayTypes[i]);
        }

        // TODO: probably wanna change this to be done in the model
        // fencepost - set the types with their nice text if it exists in strings.xml
        ArrayList<String> types = space.getTypes();
        String type;
        if (arrayMap.containsKey(types.get(0))) {
            type = arrayMap.get(types.get(0));
        } else {
            type = types.get(0);
        }
        for (int i = 1; i < types.size(); i++) {
            String key = types.get(i);
            if (arrayMap.containsKey(key)) {
                type += arrayMap.get(key);
            }
                type += ", " + key;
        }
        txtType.setText(type);

        if(space.getCapacity() > 0) {
            txtCapacity.setText(space.getCapacity());
        } else {
            txtCapacity.setVisibility(View.GONE);
            imgCapacity.setVisibility(View.GONE);
        }

        txtDesc.setText((String) space.getExtended_info().get("location_description"));

        Log.d(TAG, "Started details page for: " + space.getName());
    }

}
