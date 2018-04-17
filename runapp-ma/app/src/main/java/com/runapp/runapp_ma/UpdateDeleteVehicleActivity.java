package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public class UpdateDeleteVehicleActivity extends AppCompatActivity {

    Bundle datos;

    private Context context;
    private String plate;
    private Integer model;
    private String color ;
    private Integer capacity;
    private String brand;
    private String kind;
    private SharedPreferences sharedPrefes;
    private Integer userid;
    private Integer id;
    private String img;

    private TextView tiplate;
    private TextView timodel;
    private TextView ticolor;
    private TextView ticapacity;
    private TextView tibrand;
    private Spinner sKind;

    ImageButton b_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_vehicle);

        //setupActionBar();
        b_menu = (ImageButton) findViewById(R.id.menu);

        b_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(UpdateDeleteVehicleActivity.this,LateralMenuActivity.class);
                startActivity(myIntent);
            }
        });

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

        datos = getIntent().getExtras();

        id = datos.getInt("id");
        plate = datos.getString("plate");
        model = datos.getInt("model");
        color = datos.getString("color");
        capacity = datos.getInt("capacity");
        brand = datos.getString("brand");
        kind = datos.getString("kind");
        img = datos.getString("image");

        tiplate = (TextView) findViewById(R.id.tiPlate);
        timodel = (TextView) findViewById(R.id.tiModel);
        ticolor = (TextView) findViewById(R.id.tiColor);
        ticapacity = (TextView) findViewById(R.id.tiCapacity);
        tibrand = (TextView) findViewById(R.id.tiBrand);
        sKind = (Spinner) findViewById(R.id.sKind);

        tiplate.setText(plate);
        timodel.setText(model.toString());
        ticolor.setText(color);
        ticapacity.setText(capacity.toString());
        tibrand.setText(brand);
        sKind.setSelection(categories.indexOf(kind));

        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApolloClient.getMyApolloClient().mutate(DeleteVehicleMutation.builder()
                    .id(id).build()).enqueue(new ApolloCall.Callback<DeleteVehicleMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<DeleteVehicleMutation.Data> response) {

                        Intent myIntent = new Intent(UpdateDeleteVehicleActivity.this, VehiclesActivity.class);
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plate = tiplate.getText().toString();
                try {
                    model = Integer.parseInt(timodel.getText().toString());
                }catch (Exception e){
                    model = 0;
                }
                color = ticolor.getText().toString();
                try {
                    capacity = Integer.parseInt(ticapacity.getText().toString());
                }
                catch (Exception e) {
                    capacity = 0;
                }
                brand = tibrand.getText().toString();
                kind = spinner.getSelectedItem().toString();
                sharedPrefes = getSharedPreferences("userData",context.MODE_PRIVATE);
                userid = sharedPrefes.getInt("userid", 0);
                boolean valid = validForm();
                if (!valid){
                    MyApolloClient.getMyApolloClient().mutate(UpdateVehicleMutation.builder()
                            .id(id)
                            .plate(plate)
                            .user_id(userid)
                            .kind(kind)
                            .model(model)
                            .capacity(capacity)
                            .image(img)
                            .brand(brand)
                            .color(color).build()).enqueue(new ApolloCall.Callback<UpdateVehicleMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<UpdateVehicleMutation.Data> response) {
                            if (response.data() == null){
                                UpdateDeleteVehicleActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tiplate.setError("La placa no existe");
                                        tiplate.requestFocus();
                                    }
                                });
                            }
                            else {
                                Intent myIntent = new Intent(UpdateDeleteVehicleActivity.this, VehiclesActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                            }
                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {

                        }
                    });
                }

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
        Intent intent = new Intent(UpdateDeleteVehicleActivity.this, VehiclesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean validForm(){
        boolean cancel = false;
        View focusView = null;

        if (!validModel()) {
            timodel.setError("Modelo no valido");
            focusView = timodel;
            cancel = true;
        }

        if (!validCapcity()) {
            ticapacity.setError("Capacidad no valido");
            focusView = ticapacity;
            cancel = true;
        }

        if (!validColor()){
            ticolor.setError("Color no valido");
            focusView = ticolor;
            cancel = true;
        }

        if (!validBrand()){
            tibrand.setError("Color no valido");
            focusView = tibrand;
            cancel = true;
        }

        if (!validPlate()){
            tiplate.setError("Placa no valida");
            focusView = tiplate;
            cancel = true;
        }

        return cancel;
    }

    public boolean validModel(){
        return model.toString().length() == 4;
    }

    public boolean validCapcity(){
        return capacity > 0;
    }

    public boolean validColor(){
        return color.length() >= 3;
    }

    public boolean validBrand(){
        return brand.length() >= 3;
    }

    public boolean validPlate(){
        String plateRegexp;
        boolean ret = false;
        if (kind == "Carro"){
            plateRegexp = "[A-Z]{3}-\\d{3}";
            ret = Pattern.matches(plateRegexp, plate.toString());
        }
        else if(kind == "Moto"){
            plateRegexp = "[A-Z]{3}-\\d{2}[A-Z]{1}";
            ret = Pattern.matches(plateRegexp, plate.toString());
        }

        return ret;
    }

}
