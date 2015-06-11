package edu.uw.spacescout_android;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ajay alfred on 11/5/13.
 *
 * This class displays spaces in a list that corresponds to the results of spaces on maps.
 * For now just displays dummy data.
 *
 */

public class SpaceListFragment extends Fragment {
    private static final String TAG = "SpaceListFragment";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        super.onCreate(bundle);

        ArrayList spaces = (ArrayList) getArguments().get("spaces");

        if(view == null)
            view = inflater.inflate(R.layout.fragment_space_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lvSpaceList);

        SpaceListArrayAdapter spaceListArrayAdapter = new SpaceListArrayAdapter(getActivity(), spaces);
        listView.setAdapter(spaceListArrayAdapter);

        return view;
    }

}
