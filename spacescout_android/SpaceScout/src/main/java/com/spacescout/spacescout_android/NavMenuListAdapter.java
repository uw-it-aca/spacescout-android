package com.spacescout.spacescout_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ajay alfred on 11/4/13.
 */
public class NavMenuListAdapter extends BaseAdapter{

    //declare variables
    Context context;
    String[] mTitle;
    int[] mIcon;
    LayoutInflater inflater;

    public NavMenuListAdapter (Context context, String[] title, int[] icon) {
        this.context = context;
        this.mTitle = title;
        this.mIcon = icon;
    }


    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //declare variables
        TextView navItemText;
        ImageView imgIcon;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.drawer_list_item, parent, false);

        //locate the TextView in drawer_list_item.xml
        navItemText = (TextView) itemView.findViewById(R.id.itemText);

        //locate ImageView in drawer_list_item.xml
        imgIcon = (ImageView) itemView.findViewById(R.id.icon);

        //set the result into textView
        navItemText.setText(mTitle[position]);

        //set the results into ImageView
        imgIcon.setImageResource(mIcon[position]);

        return itemView;
    }
}
