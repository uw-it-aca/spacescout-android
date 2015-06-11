package edu.uw.spacescout_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uw.spacescout_android.model.Space;

/**
 * Created by ajay alfred on 11/6/13.
 *
 * Adapter for SpaceListFragment.
 */

public class SpaceListArrayAdapter extends ArrayAdapter {
    private static final String TAG = "SpaceListArrayAdapter";

    //define variables
    private final Context context;
    private final ArrayList values;
    private Map<String, String> arrayMap;

    public SpaceListArrayAdapter(Context context, ArrayList values) {
        super(context, R.layout.custom_space_list_row, values);
        this.context = context;
        this.values = values;

        // Grabbing the types and their matching text
        String[] arrayRaw = context.getResources().getStringArray(R.array.space_type_key);
        String[] arrayTypes = context.getResources().getStringArray(R.array.space_type_list);
        arrayMap = new HashMap<>();
        for (int i = 0; i < arrayTypes.length; i++) {
            arrayMap.put(arrayRaw[i], arrayTypes[i]);
        }
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        Space space = (Space) values.get(index);

        //get LayoutInflater to inflate space list row XML
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get space list row view group
        // TODO: Should use view holder pattern
        View rowView = inflater.inflate(R.layout.custom_space_list_row, parent, false);

        //get the different views inside the view group
        //spaceThumb
        ImageView imgSpaceThumb = (ImageView) rowView.findViewById(R.id.spaceThumb);

        //spaceTitle & spaceDesc
        TextView txtSpaceTitle = (TextView) rowView.findViewById(R.id.spaceTitle);
        TextView txtSpaceType = (TextView) rowView.findViewById(R.id.spaceType);

        //spaceLocation & spaceSeatCount
        TextView txtSpaceLoc = (TextView) rowView.findViewById(R.id.spaceLocation);
        TextView txtSpaceSeatCount = (TextView) rowView.findViewById(R.id.spaceSeatCount);
        ImageView imgSeat = (ImageView) rowView.findViewById(R.id.seatIcon);

        //this is where all the changing is supposed to happen

        txtSpaceTitle.setText(space.getName());

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
        txtSpaceType.setText(type);

        // TODO: The layout needs to account for longer descriptions
        txtSpaceLoc.setText((String) space.getExtended_info().get("location_description"));

        if(space.getCapacity() > 0) {
            txtSpaceSeatCount.setText("" + space.getCapacity());
        } else {
            txtSpaceSeatCount.setVisibility(View.GONE);
            imgSeat.setVisibility(View.GONE);
        }

        return rowView;
    }

}
