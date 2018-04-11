package com.runapp.runapp_ma;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RouteAdapter extends ArrayAdapter<Route>{

    RouteAdapter(Context context, ArrayList<Route> routes){
        super(context, R.layout.route_row, routes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.route_row, parent, false);

        Route singleRouteItem = getItem(position);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        TextView seats = (TextView) rowView.findViewById(R.id.seats);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        TextView cost = (TextView) rowView.findViewById(R.id.cost);

        title.setText(singleRouteItem.getTitle());
        description.setText(singleRouteItem.getDescription());
//        seats.setText(singleRouteItem.getSpaces_available());
        date.setText(singleRouteItem.getDate());
        time.setText(singleRouteItem.getTime());
//        cost.setText((int) singleRouteItem.getCost());

        return rowView;

    }
}
