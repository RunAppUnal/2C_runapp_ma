package com.runapp.runapp_ma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class VehicleAdapter extends BaseAdapter {

    private ArrayList<Vehicle> listItems;
    private Context context;

    public VehicleAdapter(ArrayList<Vehicle> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Vehicle item = (Vehicle) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.vehicle_row, null);
        //ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        TextView plate = (TextView) convertView.findViewById(R.id.tvPlate);
        TextView kind = (TextView) convertView.findViewById(R.id.tvKind);
        TextView model = (TextView) convertView.findViewById(R.id.tvModel);
        TextView capcity = (TextView) convertView.findViewById(R.id.tvCapacity);
        TextView brand = (TextView) convertView.findViewById(R.id.tvBrand);

        //imgPhoto.setImageResource(item.getImage());
        plate.setText(item.getPlate());
        kind.setText("Tipo: " + item.getKind());
        model.setText("Modelo: " + Integer.toString(item.getModel()));
        capcity.setText("Capacidad: " + item.getCapacity());
        brand.setText("Marca: " + item.getBrand());

        return convertView;
    }
}
