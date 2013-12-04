package com.spacescout.spacescout_android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class FilterSpacesActivity extends Fragment {

    private View view;
    private CustomDialogFragment customDialog = new CustomDialogFragment();

    public String[] arrToDisplay;
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

        SeekBar spaceCapacity = (SeekBar) view.findViewById(R.id.seekerCapacity);
        spaceCapacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            TextView spaceCapValue = (TextView) view.findViewById(R.id.seekerCapacityValue);
            int progChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int val = progress / 5;
                spaceCapValue.setText("Seats: "+val);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        TextView spaceFromDay = (TextView) view.findViewById(R.id.spinnerSpaceFromDay);
        spaceFromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);
                boolean[] arrFromDayBool = new boolean[arrToDisplay.length];
                for (int i = 0; i < arrFromDayBool.length; i++) {
                    arrFromDayBool[i] = false;
                }
                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putBooleanArray("arrFromDayBool", arrFromDayBool);
                dialogBundle.putString("dialogType", "SpaceTimeFromDay");
                dialogBundle.putString("dialogSelect", "single");
                dialogBundle.putString("dialogTitle", "Select a day");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"from day dialog");
            }
        });

        TextView spaceFromTime = (TextView) view.findViewById(R.id.spinnerSpaceFromTime);
        spaceFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);
                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putString("dialogType", "none");
                dialogBundle.putString("dialogSelect", "time");
                dialogBundle.putString("dialogTitle", "Select time");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"from time dialog");
            }
        });

        TextView timeToDay = (TextView) view.findViewById(R.id.spinnerSpaceToDay);
        timeToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);
                boolean[] arrToDayBool = new boolean[arrToDisplay.length];
                for (int i = 0; i < arrToDayBool.length; i++) {
                    arrToDayBool[i] = false;
                }
                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putBooleanArray("arrToDayBool", arrToDayBool);
                dialogBundle.putString("dialogType", "SpaceTimeToDay");
                dialogBundle.putString("dialogSelect", "single");
                dialogBundle.putString("dialogTitle", "Select a day");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"to day dialog");
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


    }


    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
