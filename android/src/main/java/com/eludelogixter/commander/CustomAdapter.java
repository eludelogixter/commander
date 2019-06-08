package com.birsan.commander;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    private final Activity activity;
    private final ArrayList<Device> list;
    //private int mSelectedItem = -1;


    public CustomAdapter(Context context, int resource, ArrayList<Device> objects, Activity activity) {
        super(context, resource, objects);
        this.activity = activity;
        this.list = objects;

    }

//    public void setmSelectedItem(int mSelectedItem) {
//        this.mSelectedItem = mSelectedItem;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row, null);

            // Hold the view objects in an object, that way the don't need to be "re-  finded"
            view = new ViewHolder();
            view.robot_name= (TextView) rowView.findViewById(R.id.robot_name);
            view.robot_mac= (TextView) rowView.findViewById(R.id.robot_mac);

            //view.valueProgressBar = (ProgressBar)rowView.findViewById(R.id.some_progress);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }

        /** Set data to your Views. */
        Device device = list.get(position);
        view.robot_name.setText(device.getName());
        view.robot_mac.setText(device.getMac());

//        if (position == mSelectedItem) {
//            view.valueProgressBar.setVisibility(View.VISIBLE);
//        }


//        Device optionItem = list.get(position);
//
//        int progress = (int) (optionItem.getOptionValue());
//
//        holder.valueProgressBar.setProgress(progress);

        return rowView;
    }



    protected static class ViewHolder{
        protected TextView robot_name;
        protected TextView robot_mac;
        //protected ProgressBar valueProgressBar;
    }
}