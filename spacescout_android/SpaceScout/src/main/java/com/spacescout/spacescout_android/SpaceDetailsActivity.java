package com.spacescout.spacescout_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

/**
 *
 * Created by ajay alfred on 11/5/13.
 */
public class SpaceDetailsActivity extends Activity{

    //Setting variables
    private TextView sampleText = null;
    private View view;
    public String spaceTitles[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_pagetitle_actionbar, null);

        TextView pageTitle = (TextView) v.findViewById(R.id.pageTitle);
        pageTitle.setText("SPACE DETAILS");

        getActionBar().setCustomView(v);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.layout_space_details);

        Bundle extras = getIntent().getExtras();

//        sampleText = (TextView) findViewById(R.id.txtSample);
//        sampleText.setText(extras.getString("spaceTitle"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
