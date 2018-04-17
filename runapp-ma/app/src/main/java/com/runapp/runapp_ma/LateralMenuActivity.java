package com.runapp.runapp_ma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class LateralMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int userid;
    String username;

    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateral_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datos = getIntent().getExtras();
        userid = datos.getInt("userid");
        username = datos.getString("username");



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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lateral_menu, menu);
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
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_searchRoute) {
            Intent i = new Intent(this, SearchActivity.class);
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            i.putExtra("from", 0);
            startActivity(i);
        } else if (id == R.id.nav_favoriteRoute) {
            Intent i = new Intent(this, SearchActivity.class);
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            i.putExtra("from", 1);
            startActivity(i);
        } else if (id == R.id.nav_myCars) {
            Intent i = new Intent(this, VehiclesActivity.class);
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_bicycle) {
            Intent i = new Intent(this, BikeRoutesActivity.class);
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_user) {
            Intent i = new Intent(this, UserActivity.class);
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("userid",userid);
            i.putExtra("username", username);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
