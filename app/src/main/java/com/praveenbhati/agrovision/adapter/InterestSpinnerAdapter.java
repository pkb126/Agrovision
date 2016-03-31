package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.Product;

import java.util.ArrayList;

/**
 *@author  Bhati on 11/9/2015.
 */
public class InterestSpinnerAdapter extends ArrayAdapter<Product> {

    private Context context;
    private ArrayList<Product> productArrayList;


    public InterestSpinnerAdapter(Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.productArrayList = objects;

    }


    public int getCount() {
        return productArrayList.size();
    }


    public Product getItem(int position) {
        return productArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(productArrayList.get(position).getProductName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(productArrayList.get(position).getProductName());
        label.setPadding(7,7,7,7);

        return label;
    }
}
