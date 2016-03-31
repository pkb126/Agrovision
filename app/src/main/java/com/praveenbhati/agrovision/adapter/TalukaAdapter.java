package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.Taluka;

import java.util.ArrayList;

/**
 * Created by Bhati on 12/12/2015.
 */
public class TalukaAdapter extends ArrayAdapter<Taluka> {

    private Context context;
    private ArrayList<Taluka> talukaArrayList;


    public TalukaAdapter(Context context, int resource, ArrayList<Taluka> objects) {
        super(context, resource, objects);
        this.context = context;
        this.talukaArrayList = objects;

    }


    public int getCount() {
        return talukaArrayList.size();
    }


    public Taluka getItem(int position) {
        return talukaArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(talukaArrayList.get(position).getTalukaName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(talukaArrayList.get(position).getTalukaName());
        label.setPadding(7,7,7,7);

        return label;
    }

}
