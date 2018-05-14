package com.runapp.runapp_ma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class CreateRouteActivity extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, NavigationView.OnNavigationItemSelectedListener {

    GoogleMap gmap;
    MapView mapView;
    String TAG = "CreateRouteActivity";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    ImageButton btnCalendar;
    TextView tiSalida;

    int day, month, year, hour, minute, counter, user_id;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    int cost, spaces_available;
    String title, description, waypoints, departure, users_in_route;
    float from_lat, from_lng, to_lat, to_lng;
    Boolean active, validation;
    TextInputEditText tiTitle, tiDescription, tiCost, tiSpace;
    Spinner sVehicles;
    Button save;
    Intent myIntent;

    int userid, carid;

    ArrayList<String> cars = new ArrayList<>();
    ArrayList<Integer> cars_id = new ArrayList<>();

    Context context;

    String username;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        final ArrayList<LatLng> middlepoints = new ArrayList<>();
        gmap = googleMap;
        gmap.setMinZoomPreference(8);
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

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
        myIntent = getIntent();
        userid = myIntent.getIntExtra("userid",0);
        username = myIntent.getStringExtra("username");
        validation = false;

        Bundle mapViewBundle = null;
        if (savedInstanceState !=null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        if (validateToken()==false){
            Intent invalidIntent = new Intent(CreateRouteActivity.this, LoginActivity.class);
            startActivity(invalidIntent);
        }

        tiTitle = (TextInputEditText) findViewById(R.id.tiTitle);
        tiDescription = (TextInputEditText) findViewById(R.id.tiDescription);
        tiCost = (TextInputEditText) findViewById(R.id.tiCost);
        tiSpace = (TextInputEditText) findViewById(R.id.tiSpace);
        sVehicles = (Spinner) findViewById(R.id.sVehicles);
//        tiSalida = (TextView) findViewById(R.id.tiSalida);
        save = (Button) findViewById(R.id.btnSave);
        cars = new ArrayList<>();
        cars_id = new ArrayList<>();
        getMyVehicles();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoute();
            }
        });

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        btnCalendar = (ImageButton) findViewById(R.id.btnCalendar);
        tiSalida = (TextView) findViewById(R.id.tiSalida);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateRouteActivity.this, CreateRouteActivity.this,  year, month, day);
                datePickerDialog.show();

            }
        });


    }

    public void onDateSet(DatePicker datePicker, int i, int i1, int i2){
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateRouteActivity.this, CreateRouteActivity.this, hour, minute, true);
        timePickerDialog.show();
    }

    public void onTimeSet(TimePicker timePicker, int i, int i1){
        hourFinal = i;
        minuteFinal = i1;

        tiSalida.setText(yearFinal + "-" + monthFinal + "-" + dayFinal + " " + hourFinal + ":" + minuteFinal + ":00");
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
        com.google.maps.model.LatLng origin = new com.google.maps.model.LatLng(from_lat,from_lng);
        com.google.maps.model.LatLng end = new com.google.maps.model.LatLng(to_lat,to_lng);
        GeoApiContext geo = getGeoContext();
        try {
            return DirectionsApi.newRequest(geo)
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
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

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }


    private void positionCamera(DirectionsResult results, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(results.routes[0].legs[0].startLocation.lat, results.routes[0].legs[0].startLocation.lng), 12));
    }

    private void addMarkerToMap(double lat, double lng, GoogleMap mMap) {
        String marketTitle = "Salida";
        if (counter == 2){
            marketTitle = "Llegada";
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(marketTitle));
        if (counter==2){
            afterArrivalSelected();
        }
    }

    private void afterArrivalSelected(){
        CreateRouteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DirectionsResult route = getDirections();
                if (route != null) {
                    addPolyline(route, gmap);
                    //addMarkersToMap(route, gmap);
                    positionCamera(route, gmap);
                }
            }
        });
    }

    private void getMyVehicles(){

        Log.d(TAG, "USERID: "+user_id);
        MyApolloClient.getMyApolloClient().query(
                MyVehiclesQuery.builder()
                .userid(user_id).build()).enqueue(
                new ApolloCall.Callback<MyVehiclesQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<MyVehiclesQuery.Data> response) {
                        if (response.data()!=null){
                        int s = response.data().myVehicles().size();
                        for (int i =0; i<s; i++){
                            cars.add(response.data().myVehicles().get(i).plate());
                            cars_id.add((int) response.data().myVehicles().get(i).id());
                        }
//                        if (s >0) {
                            fillSpinner();
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                }
        );

    }


    private void fillSpinner(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cars);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateRouteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sVehicles.setAdapter(adapter);
            }
        });
        sVehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carid = cars_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                carid = cars_id.get(0);
            }
        });
    }

//    private void formatWaypoints(float lat, float lng){
//        String waypoint = "";
//    }

    private void createRoute(){
        title = tiTitle.getText().toString();
        description = tiDescription.getText().toString();
        waypoints = "[]";
        departure = tiSalida.getText().toString();
        cost = Integer.parseInt(tiCost.getText().toString());
        users_in_route = "";
        active = true;
        Log.d(TAG, "LATS AND LNGS: "+from_lat + " "+from_lng + " "+ to_lng + "" + to_lat);
        spaces_available = Integer.parseInt(tiSpace.getText().toString());
        MyApolloClient.getMyApolloClient().mutate(
                CreateRouteMutation.builder()
                .user_id(user_id)
                .car_id(carid)
                .title(title)
                .description(description)
                .from_lat((double) from_lat)
                .from_lng((double) from_lng)
                .to_lat((double) to_lat)
                .to_lng((double) to_lng)
                .waypoints(waypoints)
                .departure(departure)
                .cost((double) cost)
                .users_in_route(users_in_route)
                .active(active)
                .spaces_available(spaces_available).build())
                .enqueue(new ApolloCall.Callback<CreateRouteMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CreateRouteMutation.Data> response) {
                        if (response.data()!=null) {
                            Intent intent = new Intent(CreateRouteActivity.this, ShowRouteActivity.class);
                            intent.putExtra("routeid", response.data().createRoute().id());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });

    }

    private boolean validateToken(){
        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "db_usuarios", null, 1);
        String []dat  = UsuarioSQLite.consultaUsuario(con);
        String token = dat[2];
        String uid = dat[1];
        String client = dat[7];
        MyApolloClient.getMyApolloClient().query(
                ValidateTokenQuery.builder()
                .token(token)
                .uid(uid)
                .client(client).build())
                .enqueue(new ApolloCall.Callback<ValidateTokenQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ValidateTokenQuery.Data> response) {
                        if (response.data()!=null){
                            if (response.data().validateToken().success().equals("true")){
                                validation = true;
                            }else {
                                validation = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        validation = false;
                    }
                });
        return validation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_route, menu);
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

