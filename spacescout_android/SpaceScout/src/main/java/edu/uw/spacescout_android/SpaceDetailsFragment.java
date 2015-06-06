package edu.uw.spacescout_android;


import android.media.Image;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.uw.spacescout_android.model.Space;


/**
 * Shows Space details. Receives Space data from a Bundle passed into
 */

public class SpaceDetailsFragment extends Fragment {
    private static final String TAG = "SpaceDetailsFragment";

    private Space space;

    public SpaceDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        space = (Space) getArguments().get("space");

        ImageView imgView = (ImageView) getActivity().findViewById(R.id.spaceCarouselImage);
        ImageView imgHeart = (ImageView) getActivity().findViewById(R.id.spaceFavIcon);
        TextView txtTitle = (TextView) getActivity().findViewById(R.id.spaceTitle);
        TextView txtType = (TextView) getActivity().findViewById(R.id.spaceType);
        TextView txtCapacity = (TextView) getActivity().findViewById(R.id.spaceCapacity);
        ImageView imgCapacity = (ImageView) getActivity().findViewById(R.id.image_capacity);
        TextView txtDesc = (TextView) getActivity().findViewById(R.id.locationDesc);
        TextView txtMapsLink = (TextView) getActivity().findViewById(R.id.mapsLink);
        // TODO: Currently, the available_hours textviews are set in LinearLayouts. May want to programmatically add them in or hide/unhide
        LinearLayout layoutResources = (LinearLayout) getActivity().findViewById(R.id.layout_resources);
        LinearLayout layoutFood = (LinearLayout) getActivity().findViewById(R.id.layout_food);
        LinearLayout layoutNoise = (LinearLayout) getActivity().findViewById(R.id.layout_noise);
        LinearLayout layoutLight = (LinearLayout) getActivity().findViewById(R.id.layout_light);

        txtTitle.setText(space.getName());
        ArrayList<String> types = space.getTypes();
        String type = types.get(0);
        for (int i = 1; i < types.size(); i++) {
            type += ", " + types.get(i);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_space_details, container, false);
    }


}
