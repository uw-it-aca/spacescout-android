package com.spacescout.spacescout_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
