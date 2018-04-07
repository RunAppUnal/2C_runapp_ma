package com.runapp.runapp_ma;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;

public class MyApolloClient {

    private static final String BASE_URL = "http://192.168.99.102:8000/graphiql";
    private static ApolloClient myApolloClient;

    public static ApolloClient getMyApolloClient(){

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();
        myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(httpClient)
                .build();
        return myApolloClient;
    }
}
