package com.praveenbhati.agrovision.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.praveenbhati.agrovision.model.ProductType;

import java.util.ArrayList;

/**
 * Created by Bhati on 11/9/2015.
 */
public class InterestTypeSpinnerAdapter extends ArrayAdapter<ProductType> {

    private Context context;
    private ArrayList<ProductType> productArrayList;


    public InterestTypeSpinnerAdapter(Context context, int resource, ArrayList<ProductType> objects) {
        super(context, resource, objects);
        this.context = context;
        this.productArrayList = objects;

    }


    public int getCount() {
        return productArrayList.size();
    }


    public ProductType getItem(int position) {
        return productArrayList.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(productArrayList.get(position).getProductTypeName());
        label.setPadding(7,7,7,7);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(productArrayList.get(position).getProductTypeName());
        label.setPadding(7,7,7,7);
        return label;
    }
}
