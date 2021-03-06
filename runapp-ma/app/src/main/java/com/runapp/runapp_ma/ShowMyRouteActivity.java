package com.runapp.runapp_ma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class ShowMyRouteActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {


    GoogleMap gmap;
    MapView mapView;


    String TAG = "ShowRouteActivity";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    int r_id;

    TextView s_title;
    TextView s_description;
    TextView s_cost;
    TextView s_departure;
    TextView s_spaces_available;
    TextView s_plate;
    TextView s_type;
    TextView s_brand;
    TextView s_colour;
    TextView s_model;
    TextView s_capacity;

    Button edit;
    Button delete;


    ListView usersIn;
    ArrayList<User> users = new ArrayList<>();

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
        gmap = googleMap;
        gmap.setMinZoomPreference(8);
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        LatLng unal = new LatLng(4.635540, -74.082807);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(unal));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_route);
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

        Bundle mapViewBundle = null;
        if (savedInstanceState !=null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);



        Intent myIntent = getIntent();
        r_id = myIntent.getIntExtra("routeid",0);


        s_title = (TextView) findViewById(R.id.s_title);
        s_description = (TextView) findViewById(R.id.s_description);
        s_cost = (TextView) findViewById(R.id.s_cost);
        s_departure = (TextView) findViewById(R.id.s_departure);
        s_spaces_available = (TextView) findViewById(R.id.s_spaces_available);
        s_plate = (TextView) findViewById(R.id.s_plate);
        s_type = (TextView) findViewById(R.id.s_type);
        s_brand = (TextView) findViewById(R.id.s_brand);
        s_colour = (TextView) findViewById(R.id.s_colour);
        s_model = (TextView) findViewById(R.id.s_model);
        s_capacity = (TextView) findViewById(R.id.s_capacity);

        usersIn = (ListView) findViewById(R.id.usersIn);

        edit = (Button) findViewById(R.id.b_edit);
        delete = (Button) findViewById(R.id.b_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRoute();
            }
        });

        getRoute();
    }



    private void getRoute(){

        MyApolloClient.getMyApolloClient().query(
                RouteByIdQuery.builder()
                        .id(r_id).build())
                .enqueue(new ApolloCall.Callback<RouteByIdQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<RouteByIdQuery.Data> response) {
                        user_id = (int) response.data().routeById().user_id();
                        car_id = (int) response.data().routeById().car_id();
                        title = response.data().routeById().title();
                        description = response.data().routeById().description();
                        from_lat = String.valueOf(response.data().routeById().from_lat());
                        from_lng = String.valueOf(response.data().routeById().from_lng());
                        to_lat = String.valueOf(response.data().routeById().to_lat());
                        to_lng = String.valueOf(response.data().routeById().to_lng());
                        waypoints = response.data().routeById().waypoints();
                        Log.d(TAG, "waypoints: "+waypoints);
                        departure = response.data().routeById().departure();
                        cost = String.valueOf(response.data().routeById().cost());
                        users_in_route = response.data().routeById().users_in_route().split(", ");
                        Log.d(TAG, "users in route: " + users_in_route.toString());
                        spaces_available = String.valueOf(response.data().routeById().spaces_available());
                        ShowMyRouteActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                s_title.setText(title);
                                s_description.setText(description);
                                s_cost.setText(cost);
                                s_departure.setText(departure.substring(5,7)+"/"+departure.substring(8,10)+" "+departure.substring(11,16));
                                s_spaces_available.setText(spaces_available);
                            }
                        });
                        getCarInfo();
                        getDirections();
                        Log.d(TAG, "users in route ------> "+ users_in_route[0].toString());
                        if (users_in_route.length>0) {
                            fillUsers();
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }

    private void fillUsers(){
        users = new ArrayList<>();
        for (int i = 0; i<users_in_route.length;i++) {
            if (users_in_route[i].equals("") == false) {
                MyApolloClient.getMyApolloClient().query(
                        UserByIdQuery.builder()
                                .userid(Long.parseLong(users_in_route[i])).build())
                        .enqueue(new ApolloCall.Callback<UserByIdQuery.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<UserByIdQuery.Data> response) {
                                if (response.data() != null) {
                                    Log.d(TAG, "GetOwner: " + response.data());
                                    if (response.data() != null) {
                                        users.add(new User(response.data().userById().name(), response.data().userById().lastname(), response.data().userById().email()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                Log.d(TAG, "getUserById failure: " + e);
                            }
                        });
            }
        }
        populateView();

    }

    private void populateView(){
        ShowMyRouteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserAdapter userAdapter = new UserAdapter(ShowMyRouteActivity.this, users);
                usersIn.setAdapter(userAdapter);
            }
        });
    }



    private void getCarInfo(){
        MyApolloClient.getMyApolloClient().query(
                VehicleByIdQuery.builder()
                        .id(car_id).build())
                .enqueue(new ApolloCall.Callback<VehicleByIdQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<VehicleByIdQuery.Data> response) {
                        if (response.data()!=null){
                            plate = response.data().vehicleById().plate();
                            type = response.data().vehicleById().kind();
                            brand = response.data().vehicleById().brand();
                            model = String.valueOf(response.data().vehicleById().model());
                            colour = response.data().vehicleById().color();
                            capacity = String.valueOf(response.data().vehicleById().capacity());
                            ShowMyRouteActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    s_plate.setText(plate);
                                    s_type.setText(type);
                                    s_brand.setText(brand);
                                    s_model.setText(model);
                                    s_colour.setText(colour);
                                    s_capacity.setText(capacity);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
    }

    private  void deleteRoute(){
        MyApolloClient.getMyApolloClient().mutate(
                DeleteRouteMutation.builder()
                        .id(r_id).build())
                .enqueue(new ApolloCall.Callback<DeleteRouteMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<DeleteRouteMutation.Data> response) {
                        ShowMyRouteActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShowMyRouteActivity.this, "Has borrado la ruta", Toast.LENGTH_LONG).show();
                            }
                        });
                        Intent intent = new Intent(ShowMyRouteActivity.this, SearchActivity.class);
                        intent.putExtra("mine", 1);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

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
        com.google.maps.model.LatLng origin = new com.google.maps.model.LatLng(Float.parseFloat(from_lat),Float.parseFloat(from_lng));
        com.google.maps.model.LatLng end = new com.google.maps.model.LatLng(Float.parseFloat(to_lat),Float.parseFloat(to_lng));
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

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title("Salida"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title("Llegada"));
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private void positionCamera(DirectionsResult results, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(results.routes[0].legs[0].startLocation.lat, results.routes[0].legs[0].startLocation.lng), 12));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_route, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        commonMethods.navegationItemSelect(this, item, id);
        return true;
    }
}
