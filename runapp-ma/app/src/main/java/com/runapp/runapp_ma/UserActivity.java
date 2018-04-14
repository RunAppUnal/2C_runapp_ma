package com.runapp.runapp_ma;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActvity";
    Context context = this;
    private TextView uname, name, email, cellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        uname = (TextView) findViewById(R.id.txtUserName);
        name = (TextView) findViewById(R.id.txtName);
        email = (TextView) findViewById(R.id.txtEmail);
        cellphone = (TextView) findViewById(R.id.txtCellphone);

        getUserByUserName();
    }

    private void getUserByUserName(){
        SharedPreferences sharedPrefes = getSharedPreferences("userData",context.MODE_PRIVATE);
        String username = sharedPrefes.getString("username", "");
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
}
