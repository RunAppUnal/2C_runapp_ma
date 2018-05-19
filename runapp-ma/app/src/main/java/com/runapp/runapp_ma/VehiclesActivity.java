package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

public class VehiclesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvItems;
    private VehicleAdapter vehicleAdapter;
    ArrayList<Vehicle> vehicles;
    private String TAG = "VehiclesActivity";
    private Context context = this;
    ImageButton b_menu;

    String  username;
    Integer user_id;

    Bundle datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "db_usuarios", null, 1);
        String []dat  = UsuarioSQLite.consultaUsuario(con);
        user_id = Integer.parseInt(dat[0]);
        commonMethods.validateToken(dat[2], dat[1], dat[7]);


        vehicles = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.adddel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent myIntent = new Intent(VehiclesActivity.this,AddVehicleActivity.class);
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setupActionBar();
        getMyVehicles();

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
        System.out.println("------>>> id" + user_id);
        MyApolloClient.getMyApolloClient().query(MyVehiclesQuery.builder().userid(user_id).build())
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vehicles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VehiclesActivity.this, LateralMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        android.support.v4.app.Fragment fragment = null;

        if (id == R.id.nav_createRoute) {
            Intent i = new Intent(this, CreateRouteActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_searchRoute) {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_favoriteRoute) {
            Intent i = new Intent(this, SearchActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_myCars) {
            Intent i = new Intent(this, VehiclesActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_bicycle) {
            Intent i = new Intent(this, BikeRoutesActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_user) {
            Intent i = new Intent(this, UserActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
