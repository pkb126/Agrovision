package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.State;

import java.util.ArrayList;

/**
 * Created by Bhati on 12/12/2015.
 */
public class StateAdapter extends ArrayAdapter<State> {

    private Context context;
    private ArrayList<State> stateArrayList;


    public StateAdapter(Context context, int resource, ArrayList<State> objects) {
        super(context, resource, objects);
        this.context = context;
        this.stateArrayList = objects;

    }


    public int getCount() {
        return stateArrayList.size();
    }


    public State getItem(int position) {
        return stateArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(stateArrayList.get(position).getStateName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(stateArrayList.get(position).getStateName());
        label.setPadding(7,7,7,7);

        return label;
    }

}
