package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.Country;

import java.util.ArrayList;

/**
 * Created by Bhati on 12/12/2015.
 */
public class CountryAdapter extends ArrayAdapter<Country> {

    private Context context;
    private ArrayList<Country> countryArrayList;


    public CountryAdapter(Context context, int resource, ArrayList<Country> objects) {
        super(context, resource, objects);
        this.context = context;
        this.countryArrayList = objects;

    }


    public int getCount() {
        return countryArrayList.size();
    }


    public Country getItem(int position) {
        return countryArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(countryArrayList.get(position).getCountryName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(countryArrayList.get(position).getCountryName());
        label.setPadding(7,7,7,7);

        return label;
    }

}
