package com.runapp.runapp_ma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nonnull;

import static android.content.Context.MODE_PRIVATE;

public class createBikeRoute extends AsyncTask<Object,Integer,Boolean> {
    private WeakReference<Context> context;
    private String time;
    private List<Double> origin;
    private List<Double> destination;
    private ProgressDialog PDialog;
    boolean doInBack = false;

    public createBikeRoute(Context context, String time, List<Double> origin, List<Double> destination) {
        this.context = new WeakReference<>(context);
        this.time = time;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    protected void onPreExecute() {
        PDialog =  ProgressDialog.show(context.get(), "Subiendo ruta ...", "Un momento por favor", true);
    }

    @Override
    protected Boolean doInBackground(Object... args) {

        MyApolloClient.getMyApolloClient().mutate(
             CreateBikeRouteMutation.builder()
                     .user_id(user_id)
                     .origin(origin)
                     .destination(destination)
                     .time(time)
                     .user_id(user_id)
                     .build()
        ).enqueue(new ApolloCall.Callback<CreateBikeRouteMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<CreateBikeRouteMutation.Data> response) {
                SharedPreferences.Editor editor = context.get().getSharedPreferences("bikeData", MODE_PRIVATE).edit();
                editor.putString("id", response.data().createBikeRoute().id());
                doInBack = true;
                editor.apply();
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ApolloCall.CallBack createBikeRoute fail", String.valueOf(e));
                doInBack = false;
            }
        });
        Log.e("Create Bike Route", String.valueOf(doInBack));
        return doInBack;
    }

    @Override
    protected void onPostExecute(Boolean result){
        PDialog.dismiss();
        if(result){
            super.onPostExecute(result);
            Intent intent = new Intent(context.get(), BikeRoutesMatchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.get().startActivity(intent);
        }else{

        }
    }

}
