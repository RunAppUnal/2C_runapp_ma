package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    ImageButton b_menu;

    private SharedPreferences sharedPrefes;

    final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                sharedPrefes = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPrefes = getSharedPreferences("userData", MODE_PRIVATE);
        boolean logged = sharedPrefes.getBoolean("logged", true);
        Log.d(TAG, "logged: " + logged);
        if (logged) {
            setContentView(R.layout.activity_main);
            Toolbar myToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            myToolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            b_menu = (ImageButton) findViewById(R.id.menu);

            b_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "clickListener: ");
                    Intent myIntent = new Intent(MainActivity.this,LateralMenuActivity.class);
                    startActivity(myIntent);
                }
            });
        }else{
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
    }

    public void toBikeRoutesActivity(View view) {
        ImageView image = findViewById(R.id.imageView2);
        Intent myIntent = new Intent(MainActivity.this, BikeRoutesActivity.class);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MainActivity.this, image, ViewCompat.getTransitionName(image));
        startActivity(myIntent, options.toBundle());
    }

    public void toCarPoolActivity(View view) {
        ImageView image = findViewById(R.id.imageView1);
        Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);

        startActivity(myIntent);
    }
}
