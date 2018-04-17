package com.runapp.runapp_ma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CreateRouteActivity extends AppCompatActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    GoogleMap gmap;
    MapView mapView;
    String TAG = "CreateRouteActivity";
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    ImageButton btnCalendar;
    TextView tiSalida;

    int day, month, year, hour, minute, counter, user_id;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    float from_lat, from_lng, to_lat, to_lng;
    TextInputEditText tiTitle, tiDescription, tiCost, tiSpace;
    Spinner sVehicles;
    Button save;

    int userid, carid, cost, spaces;

    ArrayList<String> cars = new ArrayList<>();
    ArrayList<Integer> cars_id = new ArrayList<>();

    Context context;



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
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);
        SharedPreferences sharedPref = getSharedPreferences("userData",context.MODE_PRIVATE);
        user_id = sharedPref.getInt("userid",0);

        Bundle mapViewBundle = null;
        if (savedInstanceState !=null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        tiTitle = (TextInputEditText) findViewById(R.id.tiTitle);
        tiDescription = (TextInputEditText) findViewById(R.id.tiDescription);
        tiCost = (TextInputEditText) findViewById(R.id.tiCost);
        tiSpace = (TextInputEditText) findViewById(R.id.tiSpace);
        sVehicles = (Spinner) findViewById(R.id.sVehicles);
        cars = new ArrayList<>();
        cars_id = new ArrayList<>();
        getMyVehicles();

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
                        int s = response.data().myVehicles().size();
                        for (int i =0; i<s; i++){
                            cars.add(response.data().myVehicles().get(i).plate());
                            cars_id.add(response.data().myVehicles().get(i).id());
                        }
//                        if (s >0) {
                            fillSpinner();
//                        }
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

}

