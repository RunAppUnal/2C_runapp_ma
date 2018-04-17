package com.runapp.runapp_ma;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApolloClient {

//    private static final String BASE_URL = "http://192.168.99.102:8000/graphql";
    //private static final String BASE_URL = "http://192.168.0.10:8001/graphql";
private static final String BASE_URL = "http://192.168.0.9:5000/graphql";

    private static ApolloClient myApolloClient;

    public static ApolloClient getMyApolloClient(){
        if(myApolloClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            myApolloClient = ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttpClient)
                    .build();
        }
        return myApolloClient;

    }
}
