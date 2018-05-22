package com.runapp.runapp_ma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.runapp.runapp_ma.utils.EditTextDatePicker;
import com.runapp.runapp_ma.utils.EditTextTimePicker;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class BikeRoutesActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerDragListener {
    GoogleMap gmap;
    MapView mapView;
    Marker destination;
    Marker origin;
    EditTextDatePicker date;
    EditTextTimePicker time;
    private ProgressDialog PDialog;
    float from_lat, from_lng, to_lat, to_lng;
    int counter;
    String TAG = "CreateRouteActivity";
    Polyline line;
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


        date = new EditTextDatePicker(this,R.id.date);
        time = new EditTextTimePicker(this,R.id.time);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gmap = map;
        gmap.setMinZoomPreference(12);
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gmap.setOnMarkerDragListener(this);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        LatLng unal = new LatLng(4.635540, -74.082807);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(unal));

        gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (counter<2) {
                    if (counter == 0) {
                        from_lat = (float) latLng.latitude;
                        from_lng = (float) latLng.longitude;
                    } else if (counter == 1) {
                        to_lat = (float) latLng.latitude;
                        to_lng = (float) latLng.longitude;
                    }
                    counter++;
                    addMarkerToMap(latLng.latitude, latLng.longitude, gmap);
//                    gmap.addMarker(latLng.latitude, latLng.longitude)
//                }else{
//                    middlepoints.add(latLng);
//                    addMarkerToMap(latLng.latitude, latLng.longitude, gmap);
                }
            }
        });


//        destination = map.addMarker(new MarkerOptions()
//                .position(UN)
//                .title("Destino")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                .draggable(true));
//        origin = map.addMarker(new MarkerOptions()
//                .position(UN2)
//                .title("Origen")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                .draggable(true));
    }
    @Override
    public final void onDestroy()
    {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onLowMemory()
    {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onPause()
    {
        mapView.onPause();
        super.onPause();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    public void submit(View view) {
        if(date.toString().length() < 2){
            Toast.makeText(this, "Agregar fecha",Toast.LENGTH_SHORT).show();
        }else if(time.toString().length() < 2){
            Toast.makeText(this, "Agregar hora",Toast.LENGTH_SHORT).show();
        }else if(origin == null){
            Toast.makeText(this, "Agregar origen",Toast.LENGTH_SHORT).show();
        }else if(destination == null){
            Toast.makeText(this, "Agregar destino",Toast.LENGTH_SHORT).show();
        }else {
            List<Double> orig = Arrays.asList(origin.getPosition().longitude, origin.getPosition().latitude);
            List<Double> dest = Arrays.asList(destination.getPosition().longitude, destination.getPosition().latitude);
            PDialog = ProgressDialog.show(this, "Subiendo ruta ...", "Un momento por favor", true);

            ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "db_usuarios", null, 1);
            String[] dat = UsuarioSQLite.consultaUsuario(con);
            int user_id = Integer.parseInt(dat[0]);
            MyApolloClient.getMyApolloClient().mutate(
                    CreateBikeRouteMutation.builder()
                            .user_id(user_id)
                            .origin(orig)
                            .destination(dest)
                            .time(date.toString() + " " + time.toString())
                            .build()
            ).enqueue(new ApolloCall.Callback<CreateBikeRouteMutation.Data>() {
                @Override
                public void onResponse(@Nonnull final Response<CreateBikeRouteMutation.Data> response) {
                    SharedPreferences.Editor editor = getSharedPreferences("bikeData", MODE_PRIVATE).edit();
                    editor.putString("bikeRouteID", response.data().createBikeRoute().id());
                    editor.apply();
                    BikeRoutesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent myIntent = new Intent(BikeRoutesActivity.this, BikeRoutesMatchActivity.class);
                            myIntent.putExtra("bikeRouteID", response.data().createBikeRoute().id());
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
                            Toast toast = Toast.makeText(getApplicationContext(), "Algo ocurrio con la conexion.",Toast.LENGTH_LONG);
                            PDialog.dismiss();
                        }
                    });
                }
            });
        }
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

        commonMethods.navegationItemSelect(this, item, id);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addMarkerToMap(double lat, double lng, GoogleMap mMap) {
        String marketTitle = "Salida";
        if (counter == 2){
            marketTitle = "Llegada";
            destination = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lng))
                    .title(marketTitle)
                    .draggable(true)
            );
            afterArrivalSelected();
        }else{
            origin = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lng))
                    .title(marketTitle)
                    .draggable(true)
            );
        }
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        if(line != null){
            line.remove();
        }
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        line = mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private void afterArrivalSelected(){
        BikeRoutesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DirectionsResult route = getDirections();
                if (route != null) {
                    addPolyline(route, gmap);
                    //addMarkersToMap(route, gmap)
                }
            }
        });
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directionsApiKey))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private DirectionsResult getDirections(){


        com.google.maps.model.LatLng origin2 = new com.google.maps.model.LatLng( origin.getPosition().latitude,origin.getPosition().longitude);
        com.google.maps.model.LatLng end = new com.google.maps.model.LatLng(destination.getPosition().latitude,destination.getPosition().longitude);
        GeoApiContext geo = getGeoContext();
        try {
            return DirectionsApi.newRequest(geo)
                    .mode(TravelMode.DRIVING)
                    .origin(origin2)
                    .destination(end)
//                    .waypoints("["+waypoints+"]")
                    .await();
        } catch (ApiException e) {
            Log.d(TAG, "ApiException: "+e);
            return null;
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException: "+e);
            return null;
        } catch (IOException e) {
            Log.d(TAG, "IOException: "+e);
            return null;
        }

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (counter>1){
            afterArrivalSelected();
        }
    }
}
