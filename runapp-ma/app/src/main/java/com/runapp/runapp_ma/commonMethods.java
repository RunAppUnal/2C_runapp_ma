package com.runapp.runapp_ma;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
    }

