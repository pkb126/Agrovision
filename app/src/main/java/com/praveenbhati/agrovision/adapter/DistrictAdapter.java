package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.District;

import java.util.ArrayList;

/**
 * Created by Bhati on 12/12/2015.
 */
public class DistrictAdapter extends ArrayAdapter<District> {

    private Context context;
    private ArrayList<District> districtArrayList;


    public DistrictAdapter(Context context, int resource, ArrayList<District> objects) {
        super(context, resource, objects);
        this.context = context;
        this.districtArrayList = objects;

    }


    public int getCount() {
        return districtArrayList.size();
    }


    public District getItem(int position) {
        return districtArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(districtArrayList.get(position).getDistrictName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(districtArrayList.get(position).getDistrictName());
        label.setPadding(7,7,7,7);

        return label;
    }

}
