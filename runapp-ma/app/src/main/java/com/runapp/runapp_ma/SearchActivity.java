package com.runapp.runapp_ma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    ImageButton b_picker;
    ImageButton b_reset;
    ImageButton b_find;
    ImageButton b_menu;
    EditText e_word;
    EditText e_cost;
    EditText e_spaces;
    ListView e_list;
    String s_date;
    String s_time;
    boolean status = false;
    Calendar c;
    int day, month, year, hour, minute, from;

    ArrayList<Route> routes = new ArrayList<>();

    String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        c = Calendar.getInstance();
        b_picker = (ImageButton) findViewById(R.id.b_picker);
        b_reset = (ImageButton) findViewById(R.id.reset);
        b_find = (ImageButton) findViewById(R.id.find);
        b_menu = (ImageButton) findViewById(R.id.menu);
        e_word = (EditText) findViewById(R.id.word);
        e_cost = (EditText) findViewById(R.id.cost);
        e_spaces = (EditText) findViewById(R.id.spaces);
        e_list = (ListView) findViewById(R.id.results);

        Intent myIntent = getIntent();
        from = myIntent.getIntExtra("from",0);

//        if (from == 1) {
//            myRoutesQuery();
//        }else{
//            boolean finalStatus = false;
//            finalStatus = RoutesQuery();
//        }

        s_date = "";
        s_time = "";

        b_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchActivity.this,LateralMenuActivity.class);
                startActivity(myIntent);
            }
        });

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
        int userid = 0;

//        word = e_word.getText().toString();

        SharedPreferences sharedPrefs = getSharedPreferences("userData", MODE_PRIVATE);
        userid = sharedPrefs.getInt("userid",0);
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
                        Log.d(TAG, "data: "+ response.data().searchOtherRoutes().isEmpty());
                        Log.d(TAG, "data: "+ response.data().searchOtherRoutes().size());
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
        int userid = 0;

//        word = e_word.getText().toString();

        SharedPreferences sharedPrefs = getSharedPreferences("userData", MODE_PRIVATE);
        userid = sharedPrefs.getInt("userid",0);
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
                        Log.d(TAG, "data: "+ response.data().searchMyRoutes().isEmpty());
                        Log.d(TAG, "data: "+ response.data().searchMyRoutes().size());
                        if (response.data() == null || response.data().searchMyRoutes().isEmpty()==true){
                            status = false;
                        }else{

                            //RouteAdapter adapter = new RouteAdapter(this, routes);
                            int j = response.data().searchMyRoutes().size();
                            routes = new ArrayList<>();
                            for (int i = 0;i<j;i++){
                                routes.add(new Route(response.data().searchMyRoutes().get(i).id(),
                                        response.data().searchMyRoutes().get(i).title(),
                                        response.data().searchMyRoutes().get(i).description(),
                                        response.data().searchMyRoutes().get(i).departure(),
                                        (float) response.data().searchMyRoutes().get(i).cost(),
                                        response.data().searchMyRoutes().get(i).spaces_available()
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
                        Intent myIntent = new Intent(SearchActivity.this,ShowRouteActivity.class);
                        myIntent.putExtra("routeid", r_id);
                        startActivity(myIntent);
                    }
                }
        );

    }
}
