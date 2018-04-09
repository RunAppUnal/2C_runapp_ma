package com.runapp.runapp_ma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPrefes = getSharedPreferences("userData", MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (sharedPrefes.getBoolean("logged", false)) {
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }else{

        }


    }

    public void toBikeRoutesActivity(View view) {
    }

    public void toCarPoolActivity(View view) {
    }
}
