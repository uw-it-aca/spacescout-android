package edu.uw.spacescout_android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ajay alfred on 11/5/13.
 *
 * Space filtering dialog.
 */

public class FilterSpacesActivity extends Activity implements CustomDialogFragment.FilterDialogActionsListener {

    private CustomDialogFragment customDialog = new CustomDialogFragment();

    public String[] arrToDisplay;
    public boolean[] arrSpaceTypeBool;
    public int arrSpaceLocBool;
    public int arrFromDayBool;
    public int arrToDayBool;
    public int fromHour = 13;
    public int fromMinute = 13;
    public int toHour;
    public int toMinute;
    public boolean[] arrSpaceNoiseBool;
    public boolean[] arrSpaceResourcesBool;
    public boolean[] arrSpaceFoodBool;
    public TextView spaceType, spaceLoc, spaceNoise, spaceCapacity, spaceFromDay, spaceFromTime, spaceToDay, spaceToTime;
    public TextView spaceResources, spaceFood;

    private boolean initLoad;

    public int singleSelect;

    public FilterSpacesActivity() {
        initLoad = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_pagetitle_actionbar, null);

        TextView pageTitle = (TextView) v.findViewById(R.id.pageTitle);
        pageTitle.setText("FILTER SPACES");

        getActionBar().setCustomView(v);

        //set layout view
        setContentView(R.layout.layout_space_filter);

        if(initLoad){
            //space type list
            arrToDisplay = getResources().getStringArray(R.array.space_type_list);
            arrSpaceTypeBool = new boolean[arrToDisplay.length];
            for (int i = 0; i < arrSpaceTypeBool.length; i++) {
                arrSpaceTypeBool[i] = false;
            }

            //space loc single select bool
            arrSpaceLocBool = 0;

            //space noise
            arrSpaceNoiseBool = new boolean[arrToDisplay.length];
            for (int i = 0; i < arrSpaceNoiseBool.length; i++) {
                arrSpaceNoiseBool[i] = false;
            }

            //space from day
            arrFromDayBool = 0;

            //space to day
            arrToDayBool = 0;

            //space resources
            arrSpaceResourcesBool = new boolean[arrToDisplay.length];
            for (int i = 0; i < arrSpaceResourcesBool.length; i++) {
                arrSpaceResourcesBool[i] = false;
            }

            //space food
            arrSpaceFoodBool = new boolean[arrToDisplay.length];
            for (int i = 0; i < arrSpaceFoodBool.length; i++) {
                arrSpaceFoodBool[i] = false;
            }
        }

        spaceType = (TextView) findViewById(R.id.spinnerSpaceType);
        spaceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_type_list);

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

        spaceLoc = (TextView) findViewById(R.id.spinnerSpaceLoc);
        spaceLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_loc_list);

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putInt("singleSelect", arrSpaceLocBool);
                dialogBundle.putString("dialogType", "SpaceLoc");
                dialogBundle.putString("dialogSelect", "single");
                dialogBundle.putString("dialogTitle", "Select a location");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"space loc dialog");
            }
        });

        spaceNoise = (TextView) findViewById(R.id.spinnerSpaceNoise);
        spaceNoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_noise_list);

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

        SeekBar spaceCapacity = (SeekBar) findViewById(R.id.seekerCapacity);
        spaceCapacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            TextView spaceCapValue = (TextView) findViewById(R.id.seekerCapacityValue);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int val = progress / 5;
                spaceCapValue.setText("Seats: "+val);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        spaceFromDay = (TextView) findViewById(R.id.spinnerSpaceFromDay);
        spaceFromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putInt("singleSelect", arrFromDayBool);
                dialogBundle.putString("dialogType", "SpaceTimeFromDay");
                dialogBundle.putString("dialogSelect", "single");
                dialogBundle.putString("dialogTitle", "Select a day");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"from day dialog");
            }
        });

        spaceFromTime = (TextView) findViewById(R.id.spinnerSpaceFromTime);

        //initial time setup
        if(initLoad){
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            spaceFromTime.setText(displayFormat.format(c.getTime()));
            fromHour = c.get(Calendar.HOUR_OF_DAY);
            fromMinute = c.get(Calendar.MINUTE);
        }

        spaceFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);
                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putString("dialogType", "SpaceFromTime");
                dialogBundle.putString("dialogSelect", "time");
                dialogBundle.putString("dialogTitle", "Select time");
                dialogBundle.putInt("hour", fromHour);
                dialogBundle.putInt("minute", fromMinute);
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"from time dialog");
            }
        });

        spaceToDay = (TextView) findViewById(R.id.spinnerSpaceToDay);
        spaceToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putInt("singleSelect", arrToDayBool);
                dialogBundle.putString("dialogType", "SpaceTimeToDay");
                dialogBundle.putString("dialogSelect", "single");
                dialogBundle.putString("dialogTitle", "Select a day");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"to day dialog");
            }
        });

        spaceToTime = (TextView) findViewById(R.id.spinnerSpaceToTime);
        //initial time setup
        if(initLoad){
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.HOUR, 1);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            spaceToTime.setText(displayFormat.format(c.getTime()));
            toHour = c.get(Calendar.HOUR_OF_DAY);
            toMinute = c.get(Calendar.MINUTE);
        }

        spaceToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrToDisplay = getResources().getStringArray(R.array.space_timings_daylist);
                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putString("dialogType", "SpaceToTime");
                dialogBundle.putString("dialogSelect", "time");
                dialogBundle.putString("dialogTitle", "Select time");
                dialogBundle.putInt("hour", toHour);
                dialogBundle.putInt("minute", toMinute);
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"to time dialog");
            }
        });

        spaceResources = (TextView) findViewById(R.id.spinnerSpaceResources);
        spaceResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_resources_list);

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putBooleanArray("arrSpaceResourcesBool", arrSpaceResourcesBool);
                dialogBundle.putString("dialogType", "SpaceResources");
                dialogBundle.putString("dialogSelect", "multi");
                dialogBundle.putString("dialogTitle", "Resources");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"space resources dialog");
            }
        });

        spaceFood = (TextView) findViewById(R.id.spinnerSpaceFood);
        spaceFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrToDisplay = getResources().getStringArray(R.array.space_food_list);

                Bundle dialogBundle = new Bundle();
                dialogBundle.putStringArray("arrayToDisplay", arrToDisplay);
                dialogBundle.putBooleanArray("arrSpaceFoodBool", arrSpaceFoodBool);
                dialogBundle.putString("dialogType", "SpaceFood");
                dialogBundle.putString("dialogSelect", "multi");
                dialogBundle.putString("dialogTitle", "Food/Coffee");
                customDialog.setArguments(dialogBundle);
                customDialog.show(getFragmentManager(),"space food dialog");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_spaces, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

            case R.id.action_filter_reset:
                ScrollView scrollView = (ScrollView) findViewById(R.id.scrollerFilterPage);
                Toast toast = Toast.makeText(getBaseContext(), "Filters Reset", Toast.LENGTH_SHORT);
                toast.show();
                scrollView.smoothScrollTo(0,0);
        }
        return true;
//
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void postFilterDialogActions(String dType, boolean[] boolArray, String[] displayArray, int singleSelect) {

        //flip bool flag for initial load
        initLoad = false;
        String temp = "";

        if (dType.equalsIgnoreCase("SpaceType")) {
            arrSpaceTypeBool = boolArray;
            for(int i=0; i<displayArray.length; i++){
                if(boolArray[i])
                    temp += displayArray[i]+", ";
            }

            //if all values are unchecked, set to NP
            if(temp.equalsIgnoreCase("")) temp="No preference";

            //set text
            spaceType.setText(temp);
        }
        else if (dType.equalsIgnoreCase("SpaceLoc")) {
            arrSpaceLocBool = singleSelect;
            temp = displayArray[singleSelect];

            //if all values are unchecked, set to NP
            if(temp.equalsIgnoreCase("")) temp="No preference";

            //set text
            spaceLoc.setText(temp);
        }
        else if (dType.equalsIgnoreCase("SpaceNoise")) {
            arrSpaceNoiseBool = boolArray;
            for(int i=0; i<displayArray.length; i++){
                if(boolArray[i])
                    temp += displayArray[i]+", ";
            }

            //if all values are unchecked, set to NP
            if(temp.equalsIgnoreCase("")) temp="No preference";

            //set text
            spaceNoise.setText(temp);
        }
        else if (dType.equalsIgnoreCase("SpaceTimeFromDay")) {
            arrFromDayBool = singleSelect;
            temp = displayArray[singleSelect];

            //set text
            spaceFromDay.setText(temp);
        }
        else if (dType.equalsIgnoreCase("SpaceFromTime")) {
            fromHour =  Integer.valueOf(displayArray[0]);
            fromMinute = Integer.valueOf(displayArray[1]);

            final Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, fromHour);
            c.set(Calendar.MINUTE, fromMinute);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

            //set text
            spaceFromTime.setText(displayFormat.format(c.getTime()));
        }
        else if (dType.equalsIgnoreCase("SpaceTimeToDay")) {
            arrToDayBool = singleSelect;
            temp = displayArray[singleSelect];

            //set text
            spaceToDay.setText(temp);
        }
        else if (dType.equalsIgnoreCase("SpaceToTime")) {
            toHour =  Integer.valueOf(displayArray[0]);
            toMinute = Integer.valueOf(displayArray[1]);

            final Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, toHour);
            c.set(Calendar.MINUTE, toMinute);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");

            //set text
            spaceToTime.setText(displayFormat.format(c.getTime()));
        }
        else if (dType.equalsIgnoreCase("SpaceResources")) {
            arrSpaceResourcesBool = boolArray;
            for(int i=0; i<displayArray.length; i++){
                if(boolArray[i])
                    temp += displayArray[i]+", ";
            }

            //if all values are unchecked, set to NP
            if(temp.equalsIgnoreCase("")) temp="No preference";

            //set text
            spaceResources.setText(temp);
        }
        else if (dType.equalsIgnoreCase("SpaceFood")) {
            arrSpaceFoodBool = boolArray;
            for(int i=0; i<displayArray.length; i++){
                if(boolArray[i])
                    temp += displayArray[i]+", ";
            }

            //if all values are unchecked, set to NP
            if(temp.equalsIgnoreCase("")) temp="No preference";

            //set text
            spaceFood.setText(temp);
        }
    }

}
