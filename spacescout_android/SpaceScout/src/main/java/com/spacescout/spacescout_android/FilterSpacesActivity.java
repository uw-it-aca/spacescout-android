package com.spacescout.spacescout_android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class FilterSpacesActivity extends Fragment {

    private View view;
    private View dialogView;
    private AlertDialog.Builder alertDialogBuilder;
    private Dialog dialog;
    private CustomDialogFragment customDialog = new CustomDialogFragment();

    public String[] arrToDisplay;
    public String dialogType;
    public String dialogSelect;
    public int singleSelect;

    public FilterSpacesActivity() {
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        super.onCreate(bundle);

        if(view == null)
            view = inflater.inflate(R.layout.fragment_space_filter, container, false);

        dialogView = inflater.inflate(R.layout.dialog_custom_title, container, false);

        TextView spaceType = (TextView) view.findViewById(R.id.spinnerSpaceType);
        spaceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_type_list);
                boolean[] arrSpaceTypeBool = new boolean[arrToDisplay.length];
                for (int i = 0; i < arrSpaceTypeBool.length; i++) {
                    arrSpaceTypeBool[i] = false;
                }

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putBooleanArray("arrSpaceTypeBool", arrSpaceTypeBool);
                dialogBundle.putString("dialogType", "SpaceType");
                dialogBundle.putString("dialogSelect", "multi");
                dialogBundle.putString("dialogTitle", "Select a space type");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"space type dialog");

            }
        });

        TextView spaceLoc = (TextView) view.findViewById(R.id.spinnerSpaceLoc);
        spaceLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_loc_list);
                singleSelect = 0;

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putInt("singleSelect", singleSelect);
                dialogBundle.putString("dialogType", "SpaceLoc");
                dialogBundle.putString("dialogSelect", "single");
                dialogBundle.putString("dialogTitle", "Select a location");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"space loc dialog");
            }
        });

        TextView spaceNoise = (TextView) view.findViewById(R.id.spinnerSpaceNoise);
        spaceNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_noise_list);
                boolean[] arrSpaceNoiseBool = new boolean[arrToDisplay.length];
                for (int i = 0; i < arrSpaceNoiseBool.length; i++) {
                    arrSpaceNoiseBool[i] = false;
                }
                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putBooleanArray("arrSpaceNoiseBool", arrSpaceNoiseBool);
                dialogBundle.putString("dialogType", "SpaceNoise");
                dialogBundle.putString("dialogSelect", "multi");
                dialogBundle.putString("dialogTitle", "Noise level");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"space noise dialog");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spinner;
        List<String> list;
        ArrayAdapter<String> adapter;

        spinner = (Spinner) view.findViewById(R.id.spinnerSpaceCap);
        list =  Arrays.asList(getResources().getStringArray(R.array.space_loc_list));
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.custom_spinner_text, list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.spinnerSpaceFromDay);
        list =  Arrays.asList(getResources().getStringArray(R.array.space_timings_list));
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.custom_spinner_text, list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.spinnerSpaceFromTime);
        list =  Arrays.asList(getResources().getStringArray(R.array.space_timings_list));
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.custom_spinner_text, list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.spinnerSpaceResources);
        list =  Arrays.asList(getResources().getStringArray(R.array.space_noise_list));
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.custom_spinner_text, list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row);
        spinner.setAdapter(adapter);

        spinner = (Spinner) view.findViewById(R.id.spinnerSpaceFood);
        list =  Arrays.asList(getResources().getStringArray(R.array.space_noise_list));
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.custom_spinner_text, list);
        adapter.setDropDownViewResource(R.layout.custom_spinner_row);
        spinner.setAdapter(adapter);

    }


    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
