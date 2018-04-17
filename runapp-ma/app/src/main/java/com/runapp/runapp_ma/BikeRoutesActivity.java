package com.runapp.runapp_ma;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;

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

public class BikeRoutesActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapView map;
    Marker destination;
    Marker origin;
    EditTextDatePicker date;
    EditTextTimePicker time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(UN, 10));
        destination = map.addMarker(new MarkerOptions()
                .position(UN)
                .title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .draggable(true));
        origin = map.addMarker(new MarkerOptions()
                .position(UN)
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
        createBikeRoute br = new createBikeRoute(this,date.toString() + time.toString(), orig, dest);
        br.execute();
    }
}
