package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.Village;

import java.util.ArrayList;

/**
 * Created by Bhati on 12/12/2015.
 */
public class VillageAdapter extends ArrayAdapter<Village> {

    private Context context;
    private ArrayList<Village> villageArrayList;


    public VillageAdapter(Context context, int resource, ArrayList<Village> objects) {
        super(context, resource, objects);
        this.context = context;
        this.villageArrayList = objects;

    }


    public int getCount() {
        return villageArrayList.size();
    }


    public Village getItem(int position) {
        return villageArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(villageArrayList.get(position).getVillageName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(villageArrayList.get(position).getVillageName());
        label.setPadding(7,7,7,7);

        return label;
    }

}
