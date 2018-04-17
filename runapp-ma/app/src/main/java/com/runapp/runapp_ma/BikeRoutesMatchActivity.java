package com.runapp.runapp_ma;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class BikeRoutesMatchActivity extends AppCompatActivity {
    private ProgressDialog PDialog;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mListView = (ListView) findViewById(R.id.list_routes);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_routes_match);

        PDialog =  ProgressDialog.show(this, "Encontrando rutas parecidas ...", "Un momento por favor", true);

    }
}
