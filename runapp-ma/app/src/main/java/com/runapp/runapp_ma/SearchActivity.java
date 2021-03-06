package com.runapp.runapp_ma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

//import java.text.DateFormat;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nonnull;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,  NavigationView.OnNavigationItemSelectedListener{

    ImageButton b_picker;
    ImageButton b_reset;
    ImageButton b_find;
    EditText e_word;
    EditText e_cost;
    EditText e_spaces;
    ListView e_list;
    String s_date;
    String s_time;
    boolean status = false;
    Calendar c;
    int day, month, year, from, userid;

    ArrayList<Route> routes = new ArrayList<>();

    String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConexionSQLiteHelper con = new ConexionSQLiteHelper(SearchActivity.this, "db_usuarios", null, 1);
        String []dat  = UsuarioSQLite.consultaUsuario(con);
        userid = Integer.parseInt(dat[0]);
        commonMethods.validateToken(dat[2], dat[1], dat[7]);
        Intent iii = getIntent();
        from = iii.getIntExtra("mine",0);

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

        c = Calendar.getInstance();
        b_picker = (ImageButton) findViewById(R.id.b_picker);
        b_reset = (ImageButton) findViewById(R.id.reset);
        b_find = (ImageButton) findViewById(R.id.find);
        e_word = (EditText) findViewById(R.id.word);
        e_cost = (EditText) findViewById(R.id.cost);
        e_spaces = (EditText) findViewById(R.id.spaces);
        e_list = (ListView) findViewById(R.id.results);

        Intent myIntent = getIntent();
        s_date = "";
        s_time = "";

        b_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from == 1) {
                    myRoutesQuery();
                }else{
                    boolean finalStatus = false;
                    finalStatus = RoutesQuery();
                }
            }
        });



        b_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this, SearchActivity.this,year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        b_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public boolean RoutesQuery(){
        String word = "";
        String cost = "";
        String departure = "";
        String spaces = "";
        word = e_word.getText().toString();
        cost = e_cost.getText().toString();
        spaces = e_spaces.getText().toString();
        if (s_date == "") {
            departure = "";
        }else {
            departure = s_date;
        }
        MyApolloClient.getMyApolloClient().query(
                SearchRoutesQuery.builder()
                .userid(userid)
                .word(word)
                .cost(cost)
                .datetime(s_date)
                .spaces(spaces).build()).enqueue(
                new ApolloCall.Callback<SearchRoutesQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SearchRoutesQuery.Data> response) {
                        if (response.data() == null || response.data().searchOtherRoutes().isEmpty()==true){
                            status = false;
                        }else{

                            //RouteAdapter adapter = new RouteAdapter(this, routes);
                            int j = response.data().searchOtherRoutes().size();
                            routes = new ArrayList<>();
                            for (int i = 0;i<j;i++){
                                routes.add(new Route((int)response.data().searchOtherRoutes().get(i).id(),
                                        response.data().searchOtherRoutes().get(i).title(),
                                        response.data().searchOtherRoutes().get(i).description(),
                                        response.data().searchOtherRoutes().get(i).departure(),
                                        (float) response.data().searchOtherRoutes().get(i).cost(),
                                        (int)response.data().searchOtherRoutes().get(i).spaces_available()
                                ));
                            }
                            status = true;


                        }
                        fill(status);
                        //status = true;
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        status = false;
                    }
                }
        );

        return status;
    }


    public boolean myRoutesQuery(){
        String word = "";
        String cost = "";
        String departure = "";
        String spaces = "";

        word = e_word.getText().toString();
        cost = e_cost.getText().toString();
        spaces = e_spaces.getText().toString();
        if (s_date == "") {
            departure = "";
        }else {
            departure = s_date;
        }
        MyApolloClient.getMyApolloClient().query(
                SearchMyRoutesQuery.builder()
                        .userid(userid)
                        .word(word)
                        .cost(cost)
                        .datetime(s_date)
                        .spaces(spaces).build()).enqueue(
                new ApolloCall.Callback<SearchMyRoutesQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SearchMyRoutesQuery.Data> response) {
                        if (response.data() == null || response.data().searchMyRoutes().isEmpty()==true){
                            status = false;
                        }else{

                            //RouteAdapter adapter = new RouteAdapter(this, routes);
                            int j = response.data().searchMyRoutes().size();
                            routes = new ArrayList<>();
                            for (int i = 0;i<j;i++){
                                routes.add((new Route((int)response.data().searchMyRoutes().get(i).id(),
                                        response.data().searchMyRoutes().get(i).title(),
                                        response.data().searchMyRoutes().get(i).description(),
                                        response.data().searchMyRoutes().get(i).departure(),
                                        (float) response.data().searchMyRoutes().get(i).cost(),
                                        (int)response.data().searchMyRoutes().get(i).spaces_available()
                                )));
                            }
                            status = true;


                        }
                        fill(status);
                        //status = true;
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        status = false;
                    }
                }
        );

        return status;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        s_date = year+"-"+(month+1)+"-"+dayOfMonth;
        Log.d(TAG, "s_date: "+s_date);
    }


    public void fill(final boolean stat){
        SearchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RouteAdapter routeAdapter = new RouteAdapter(SearchActivity.this, routes);
                e_list.setAdapter(null);
                if (stat == true) {
                    e_list.setAdapter(null);
                    e_list.setAdapter(routeAdapter);
                }else{
                    e_list.setAdapter(null);
                }


            }
        });
        e_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        Context ctx = view.getContext();
                        Route r = (Route) e_list.getItemAtPosition(position);
                        int r_id = r.getId();
                        if (from == 1){
                            Intent ii = new Intent(SearchActivity.this, ShowMyRouteActivity.class);
                            ii.putExtra("routeid", r_id);
                            startActivity(ii);
                        }else {
                            Intent myIntent = new Intent(SearchActivity.this, ShowRouteActivity.class);
                            myIntent.putExtra("routeid", r_id);
                            startActivity(myIntent);
                        }
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
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
