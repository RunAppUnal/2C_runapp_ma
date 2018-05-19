package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "UserActvity";
    Context context = this;
    private TextView uname, name, email, cellphone;

    String username;
    int userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ConexionSQLiteHelper con = new ConexionSQLiteHelper(getApplicationContext(), "db_usuarios", null, 1);
        String []dat  = UsuarioSQLite.consultaUsuario(con);
        commonMethods.validateToken(dat[2], dat[1], dat[7]);
        userid = Integer.parseInt(dat[0]);
        username = dat[5];

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
//        datos = getIntent().getExtras();

        uname = (TextView) findViewById(R.id.txtUserName);
        name = (TextView) findViewById(R.id.txtName);
        email = (TextView) findViewById(R.id.txtEmail);
        cellphone = (TextView) findViewById(R.id.txtCellphone);



        getUserByUserName();
    }

    private void getUserByUserName(){
        MyApolloClient.getMyApolloClient().query(
                UserByUserNameQuery.builder().username(username).build()).enqueue(new ApolloCall.Callback<UserByUserNameQuery.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<UserByUserNameQuery.Data> response) {
                Log.d(TAG, "onResponse: " + response.data().userByUsername);
                UserActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uname.setText(response.data().userByUsername().username);
                        name.setText(response.data().userByUsername().name + " " + response.data().userByUsername().lastname);
                        email.setText(response.data().userByUsername().email);
                        cellphone.setText("3113124464");
                    }
                });
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
        getMenuInflater().inflate(R.menu.user, menu);
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
        Intent intent = new Intent(UserActivity.this, LateralMenuActivity.class);
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
