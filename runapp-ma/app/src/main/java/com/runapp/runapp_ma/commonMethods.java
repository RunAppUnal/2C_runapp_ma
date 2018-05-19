package com.runapp.runapp_ma;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;


public class commonMethods extends Application{
    private static Context mContext;

    @SuppressLint("MissingSuperCall")
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }


    public static void validateToken(String token, String uid, String client){
        MyApolloClient.getMyApolloClient().query(
            ValidateTokenQuery.builder()
                    .token(token)
                    .uid(uid)
                    .client(client).build())
            .enqueue(new ApolloCall.Callback<ValidateTokenQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<ValidateTokenQuery.Data> response) {
                    if (response.data()!=null){
                        if (response.data().validateToken().success().equals("true")!=true){
                            final android.content.Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                }
            });
    }

    public static void navegationItemSelect(final Context context, MenuItem item, int id){
        if (id == R.id.nav_createRoute) {
            Intent i = new Intent(context, CreateRouteActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_searchRoute) {
            Intent i = new Intent(context, SearchActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_favoriteRoute) {
            Intent i = new Intent(context, SearchActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_myCars) {
            Intent i = new Intent(context, VehiclesActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_bicycle) {
            Intent i = new Intent(context, BikeRoutesActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_user) {
            Intent i = new Intent(context, UserActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
        } else if (id == R.id.nav_logout){
            ConexionSQLiteHelper con = new ConexionSQLiteHelper(context, "db_usuarios", null, 1);
            String []dat  = UsuarioSQLite.consultaUsuario(con);
            MyApolloClient.getMyApolloClient().mutate(LogOutMutation.builder()
                .uid(dat[1])
                .token(dat[2])
                .client(dat[7]).build()).enqueue(new ApolloCall.Callback<LogOutMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<LogOutMutation.Data> response) {
                    context.deleteDatabase("db_usuarios");
                    Intent i = new Intent(context, LoginActivity.class);
                    context.startActivity(i);
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {

                }
            });
        }

    }
}

