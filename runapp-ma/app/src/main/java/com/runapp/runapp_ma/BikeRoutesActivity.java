package com.runapp.runapp_ma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.runapp.runapp_ma.utils.EditTextDatePicker;
import com.runapp.runapp_ma.utils.EditTextTimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

public class BikeRoutesActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    MapView map;
    Marker destination;
    Marker origin;
    EditTextDatePicker date;
    EditTextTimePicker time;
    private ProgressDialog PDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_routes);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingTool = findViewById(R.id.toolbar_layout);
        ;
        collapsingTool.setTitle("BiciJoin");

        date = new EditTextDatePicker(this,R.id.date);
        time = new EditTextTimePicker(this,R.id.time);

        map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng UN = new LatLng(4.636360, -74.083331);
        LatLng UN2 = new LatLng(4.666360, -74.086331);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(UN, 12));
        destination = map.addMarker(new MarkerOptions()
                .position(UN)
                .title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .draggable(true));
        origin = map.addMarker(new MarkerOptions()
                .position(UN2)
                .title("Origen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .draggable(true));
    }
    @Override
    public final void onDestroy()
    {
        map.onDestroy();
        super.onDestroy();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onLowMemory()
    {
        map.onLowMemory();
        super.onLowMemory();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onPause()
    {
        map.onPause();
        super.onPause();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        map.onResume();
    }

    public void submit(View view) {
        List<Double> orig = Arrays.asList(origin.getPosition().longitude, origin.getPosition().latitude);
        List<Double> dest = Arrays.asList(destination.getPosition().longitude, destination.getPosition().latitude);
        PDialog =  ProgressDialog.show(this, "Subiendo ruta ...", "Un momento por favor", true);

        int user_id = getSharedPreferences("userData", MODE_PRIVATE).getInt("userid", 0);
        MyApolloClient.getMyApolloClient().mutate(
                CreateBikeRouteMutation.builder()
                        .user_id(user_id)
                        .origin(orig)
                        .destination(dest)
                        .time(date.toString() + " " + time.toString())
                        .user_id(user_id)
                        .build()
        ).enqueue(new ApolloCall.Callback<CreateBikeRouteMutation.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<CreateBikeRouteMutation.Data> response) {
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                editor.putString("bikeRouteID", response.data().createBikeRoute().id());
                editor.apply();
                BikeRoutesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent myIntent = new Intent(BikeRoutesActivity.this,BikeRoutesMatchActivity.class);
                        myIntent.putExtra("bikeRouteID",response.data().createBikeRoute().id());
                        PDialog.dismiss();
                        startActivity(myIntent);

                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                BikeRoutesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PDialog.dismiss();
                    }
                });
            }
        });
    }


//        createBikeRoute br = new createBikeRoute(this,date.toString() + " " + time.toString(), orig, dest);
//
//        br.execute();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bike_routes, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        android.support.v4.app.Fragment fragment = null;

        if (id == R.id.nav_createRoute) {
            Intent i = new Intent(this, CreateRouteActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_searchRoute) {
            Intent i = new Intent(this, SearchActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            //i.putExtra("from", 0);
            startActivity(i);
        } else if (id == R.id.nav_favoriteRoute) {
            Intent i = new Intent(this, SearchActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            //i.putExtra("from", 1);
            startActivity(i);
        } else if (id == R.id.nav_myCars) {
            Intent i = new Intent(this, VehiclesActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_bicycle) {
            Intent i = new Intent(this, BikeRoutesActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_user) {
            Intent i = new Intent(this, UserActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            //i.putExtra("userid",userid);
            //i.putExtra("username", username);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
