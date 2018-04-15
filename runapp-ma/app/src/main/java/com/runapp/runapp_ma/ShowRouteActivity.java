package com.runapp.runapp_ma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import javax.annotation.Nonnull;

import static com.runapp.runapp_ma.R.id.find;
import static com.runapp.runapp_ma.R.id.s_title;

public class ShowRouteActivity extends AppCompatActivity {

    GoogleMap googleMap;

    String TAG = "ShowRouteActivity";

    int r_id;

    TextView s_title;
    TextView s_description;
    TextView s_cost;
    TextView s_departure;
    TextView s_plate;
    TextView s_type;
    TextView s_brand;
    TextView s_colour;
    TextView s_model;
    TextView s_name;
    TextView s_email;
    MapView mapView;

    int user_id;
    int car_id;

    String title;
    String description;
    String from_lat;
    String from_lng;
    String to_lat;
    String to_lng;
    String waypoints;
    String departure;
    String cost;
    String users_in_route[];
    String spaces_available;

    String plate;
    String type;
    String brand;
    String model;
    String colour;
    String capacity;

    String name;
    String email;

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent myIntent = getIntent();
        r_id = myIntent.getIntExtra("routeid",0);

        s_title = (TextView) findViewById(R.id.s_title);
        s_description = (TextView) findViewById(R.id.s_description);
        s_cost = (TextView) findViewById(R.id.s_cost);
        s_departure = (TextView) findViewById(R.id.s_departure);
        s_plate = (TextView) findViewById(R.id.s_plate);
        s_type = (TextView) findViewById(R.id.s_type);
        s_brand = (TextView) findViewById(R.id.s_brand);
        s_colour = (TextView) findViewById(R.id.s_colour);
        s_model = (TextView) findViewById(R.id.s_model);
        s_name = (TextView) findViewById(R.id.s_name);
        s_email = (TextView) findViewById(R.id.s_email);
        mapView = (MapView) findViewById(R.id.mapView);

        getRoute();

//        mapView.onCreate(savedInstanceState);
//        setUpMap();

    }


    private void getRoute(){

        MyApolloClient.getMyApolloClient().query(
                RouteByIdQuery.builder()
                .id(r_id).build())
                .enqueue(new ApolloCall.Callback<RouteByIdQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<RouteByIdQuery.Data> response) {
                        user_id = response.data().routeById().user_id();
                        car_id = response.data().routeById().car_id();
                        title = response.data().routeById().title();
                        description = response.data().routeById().description();
                        from_lat = String.valueOf(response.data().routeById().from_lat());
                        from_lng = String.valueOf(response.data().routeById().from_lng());
                        to_lat = String.valueOf(response.data().routeById().to_lat());
                        to_lng = String.valueOf(response.data().routeById().to_lng());
                        waypoints = response.data().routeById().waypoints();
                        departure = response.data().routeById().departure();
                        cost = String.valueOf(response.data().routeById().cost());
                        users_in_route = response.data().routeById().users_in_route().split(",");
                        spaces_available = String.valueOf(response.data().routeById().spaces_available());



                        Log.d(TAG, "title: "+title);
                        ShowRouteActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                s_title.setText(title);
                                s_description.setText(description);
                                s_cost.setText(cost);
                                s_departure.setText(departure.substring(5,7)+"/"+departure.substring(8,10)+" "+departure.substring(11,16));
                            }
                        });
                        getOwner();
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }


    private void getOwner(){
        Log.d(TAG, "userid: "+user_id);
        MyApolloClient.getMyApolloClient().query(
                UserByIdQuery.builder()
                .userid(user_id).build())
                .enqueue(new ApolloCall.Callback<UserByIdQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UserByIdQuery.Data> response) {
                        Log.d(TAG, "GetOwner: "+ response.data());
                        if (response.data()!=null) {
                            name = response.data().userById().name() + " " + response.data().userById().lastname();
                            email = response.data().userById().email();
                            ShowRouteActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    s_name.setText(name);
                                    s_email.setText(email);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.d(TAG, "getOwner failure: "+e);
                    }
                });

    }



    private void getCarInfo(){
        
    }

}
