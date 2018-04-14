package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class AddVehicleActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.sKind);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Carro");
        categories.add("Moto");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);




        Button btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tiplate = (TextView) findViewById(R.id.tiPlate);
                TextView timodel = (TextView) findViewById(R.id.tiModel);
                TextView ticolor = (TextView) findViewById(R.id.tiColor);
                TextView ticapacity = (TextView) findViewById(R.id.tiCapacity);
                TextView tibrand = (TextView) findViewById(R.id.tiBrand);

                String plate = tiplate.getText().toString();
                int model = Integer.parseInt(timodel.getText().toString());
                String color = ticolor.getText().toString();
                int capacity = Integer.parseInt(ticapacity.getText().toString());
                String brand = tibrand.getText().toString();
                String kind = spinner.getSelectedItem().toString();
                SharedPreferences sharedPrefes = getSharedPreferences("userData",context.MODE_PRIVATE);
                Integer userid = sharedPrefes.getInt("userid", 0);
                MyApolloClient.getMyApolloClient().mutate(CreateVehicleMutation.builder()
                        .plate(plate)
                        .user_id(userid)
                        .kind(kind)
                        .model(model)
                        .color(color)
                        .capacity(capacity)
                        .image("img.jpg")
                        .brand(brand).build()).enqueue(new ApolloCall.Callback<CreateVehicleMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CreateVehicleMutation.Data> response) {
                        Intent myIntent = new Intent(AddVehicleActivity.this,VehiclesActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });

            }
        });

    }

    private void submitVehicle(){

    }

}
