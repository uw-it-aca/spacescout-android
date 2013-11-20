package com.spacescout.spacescout_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class FilterSpacesActivity extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        super.onCreate(bundle);

        if(view == null)
            view = inflater.inflate(R.layout.fragment_space_filter, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spinnerSType = (Spinner) view.findViewById(R.id.spinnerSpaceType);
        List<String> list = new ArrayList<String>(Arrays.asList(getResources().
                getStringArray(R.array.space_type_list)));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.custom_spinner_text, list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row);
        spinnerSType.setAdapter(adapter);
    }



    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
