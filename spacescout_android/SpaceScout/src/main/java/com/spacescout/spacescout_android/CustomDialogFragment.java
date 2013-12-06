package com.spacescout.spacescout_android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class CustomDialogFragment extends DialogFragment{

    private View titleView;
    public String[] arrToDisplay;
    public boolean[] arrSelectBool;
    public String dialogType;
    public String dialogSelect;
    public int singleSelect;

    public CustomDialogFragment() {
        //empty constructor
    }

    public interface FilterDialogActionsListener {
        void postFilterDialogActions(String dType, boolean[] boolArray, String[] displayArray, int singleSelect);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        //intialize custom dialog title view
        titleView = inflater.inflate(R.layout.dialog_custom_title, null);
        TextView title = (TextView) titleView.findViewById(R.id.dialogTitle);
        title.setText(getArguments().getString("dialogTitle"));

        View timeView = inflater.inflate(R.layout.custom_time_picker, null);

        //get array and bool to display
        arrToDisplay = getArguments().getStringArray("arrayToDisplay");

        //get type of dialog
        dialogType = getArguments().getString("dialogType");

        if (dialogType.equalsIgnoreCase("SpaceType")) {
            arrSelectBool = getArguments().getBooleanArray("arrSpaceTypeBool");
        } else if (dialogType.equalsIgnoreCase("SpaceLoc")) {
            singleSelect = getArguments().getInt("singleSelect");
        } else if (dialogType.equalsIgnoreCase("SpaceNoise")) {
            arrSelectBool = getArguments().getBooleanArray("arrSpaceNoiseBool");
        } else if (dialogType.equalsIgnoreCase("SpaceTimeFromDay")) {
            singleSelect = getArguments().getInt("singleSelect");
        } else if (dialogType.equalsIgnoreCase("SpaceTimeToDay")) {
            singleSelect = getArguments().getInt("singleSelect");
        } else if (dialogType.equalsIgnoreCase("SpaceResources")) {
            arrSelectBool = getArguments().getBooleanArray("arrSpaceResourcesBool");
        } else if (dialogType.equalsIgnoreCase("SpaceFood")) {
            arrSelectBool = getArguments().getBooleanArray("arrSpaceFoodBool");
        }

        //get dialog select type
        dialogSelect = getArguments().getString("dialogSelect");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity(), R.style.popupStyle);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setCustomTitle(titleView);

        if (dialogSelect.equalsIgnoreCase("multi")) {
            alertDialogBuilder.setMultiChoiceItems(arrToDisplay, arrSelectBool, new DialogInterface.OnMultiChoiceClickListener() {
                public void onClick(DialogInterface dialogInterface, int item, boolean isChecked) {
                    if (isChecked) {
                        Log.i("INFO", "checked item -> " + item);
                        arrSelectBool[item] = isChecked;
                    }
                    if (!isChecked) {
                        Log.i("INFO", "unchecked -> " + item);
                        arrSelectBool[item] = isChecked;
                    }


                }
            })
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FilterDialogActionsListener callingActivity = (FilterDialogActionsListener) getActivity();
                    callingActivity.postFilterDialogActions(dialogType, arrSelectBool, arrToDisplay, 0);
                    dialog.dismiss();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }

        else if (dialogSelect.equalsIgnoreCase("time")) {
            alertDialogBuilder.setView(timeView)
            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            })
            .setCustomTitle(null);
        }

        else
        {
            alertDialogBuilder.setSingleChoiceItems(arrToDisplay, singleSelect, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Log.i("INFO", "selected -> " + item);
                    FilterDialogActionsListener callingActivity = (FilterDialogActionsListener) getActivity();
                    callingActivity.postFilterDialogActions(dialogType, arrSelectBool, arrToDisplay, item);
                    dialog.dismiss();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }


        // create alert dialog
        AlertDialog dialog = alertDialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundResource(R.drawable.custom_filter_action_button);
                positiveButton.setTextColor(getResources().getColor(R.color.white));

                Button negativeButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setBackgroundResource(R.drawable.custom_filter_action_button);
                negativeButton.setTextColor(getResources().getColor(R.color.white));
            }
        });

        return dialog;
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
