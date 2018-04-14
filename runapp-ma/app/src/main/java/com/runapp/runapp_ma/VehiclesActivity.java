package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class VehiclesActivity extends AppCompatActivity {

    private ListView lvItems;
    private VehicleAdapter vehicleAdapter;
    ArrayList<Vehicle> vehicles;
    private String TAG = "VehiclesActivity";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vehicles = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent myIntent = new Intent(VehiclesActivity.this,AddVehicleActivity.class);
                startActivity(myIntent);
            }
        });

        getMyVehicles();

        lvItems = (ListView) findViewById(R.id.lvItems);
        vehicleAdapter = new VehicleAdapter(vehicles, this);
        lvItems.setAdapter(vehicleAdapter);
    }

    private void getMyVehicles(){
        SharedPreferences sharedPrefes = getSharedPreferences("userData",context.MODE_PRIVATE);
        Integer userid = sharedPrefes.getInt("userid", 0);
        MyApolloClient.getMyApolloClient().query(MyVehiclesQuery.builder().userid(userid).build()).enqueue(new ApolloCall.Callback<MyVehiclesQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<MyVehiclesQuery.Data> response) {
                int j = response.data().myVehicles().size();
                for (int i = 0; i < j; i++ ){
                    vehicles.add(new Vehicle(
                            response.data().myVehicles().get(i).id,
                            response.data().myVehicles().get(i).plate,
                            response.data().myVehicles().get(i).user_id,
                            response.data().myVehicles().get(i).kind,
                            response.data().myVehicles().get(i).model,
                            response.data().myVehicles().get(i).color,
                            response.data().myVehicles().get(i).capacity,
                            response.data().myVehicles().get(i).image,
                            response.data().myVehicles().get(i).brand
                    ));
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

}
