package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
    ImageButton b_menu;

    String userid, username;

    Bundle datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        datos = getIntent().getExtras();
        userid = datos.getString("userid");
        username = datos.getString("username");


        vehicles = new ArrayList<>();
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

        //setupActionBar();
        getMyVehicles();

        b_menu = (ImageButton) findViewById(R.id.menu);
        b_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(VehiclesActivity.this,LateralMenuActivity.class);
                myIntent.putExtra("userid",userid);
                myIntent.putExtra("username", username);
                startActivity(myIntent);
            }
        });

        lvItems = (ListView) findViewById(R.id.lvItems);
        vehicleAdapter = new VehicleAdapter(vehicles, this);
        lvItems.setAdapter(vehicleAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, "onItemClick: tab en el la lista" + position);
                //Log.d(TAG, "onItemClick: " + vehicles.get(0).getPlate().toString());
                Intent intent = new Intent(VehiclesActivity.this, UpdateDeleteVehicleActivity.class);
                intent.putExtra("id", vehicles.get(position).getId());
                intent.putExtra("plate", vehicles.get(position).getPlate());
                intent.putExtra("user_id", vehicles.get(position).getUser_id());
                intent.putExtra("kind", vehicles.get(position).getKind());
                intent.putExtra("model", vehicles.get(position).getModel());
                intent.putExtra("capacity", vehicles.get(position).getCapacity());
                intent.putExtra("image", vehicles.get(position).getImage());
                intent.putExtra("brand", vehicles.get(position).getBrand());
                intent.putExtra("color", vehicles.get(position).getColor());
                startActivity(intent);
            }
        });
    }

    private void getMyVehicles(){
        SharedPreferences sharedPrefes = getSharedPreferences("userData",context.MODE_PRIVATE);
        Integer userid = sharedPrefes.getInt("userid", 0);
        Log.d(TAG, "USERID PLS "+userid);

        MyApolloClient.getMyApolloClient().query(MyVehiclesQuery.builder().userid(userid).build())
                .enqueue(new ApolloCall.Callback<MyVehiclesQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<MyVehiclesQuery.Data> response) {
                int j = response.data().myVehicles().size();
                for (int i = 0; i < j; i++ ){
                    vehicles.add(new Vehicle(
                            (int)response.data().myVehicles().get(i).id,
                            response.data().myVehicles().get(i).plate,
                            (int)response.data().myVehicles().get(i).user_id,
                            response.data().myVehicles().get(i).kind,
                            (int)response.data().myVehicles().get(i).model,
                            response.data().myVehicles().get(i).color,
                            (int)response.data().myVehicles().get(i).capacity,
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

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VehiclesActivity.this, LateralMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
