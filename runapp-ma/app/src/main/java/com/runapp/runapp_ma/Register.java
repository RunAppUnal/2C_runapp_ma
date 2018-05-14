package com.runapp.runapp_ma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class Register extends AppCompatActivity {

    String name, lastname, username, phone, email, password, password_conf;

    TextView t_name, t_lastname, t_username, t_phone, t_email, t_password, t_password_conf;

    Button createButton;

    Boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t_name = (TextView) findViewById(R.id.name);
        t_lastname = (TextView) findViewById(R.id.lastname);
        t_username = (TextView) findViewById(R.id.username);
        t_phone = (TextView) findViewById(R.id.phone);
        t_email = (TextView) findViewById(R.id.email);
        t_password = (TextView) findViewById(R.id.password);
        t_password_conf = (TextView) findViewById(R.id.password_conf);

        createButton = (Button) findViewById(R.id.b_register);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFull()==true){
                    setData();
                    createUser();
                    Intent intent = new Intent(Register.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this,"por favor revise los datos",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    private boolean createUser(){

        MyApolloClient.getMyApolloClient().mutate(
                CreateUserMutation.builder()
                .name(name)
                .lastname(lastname)
                .username(username)
                .cellphone(phone)
                .email(email)
                .password(password)
                .password_confirmation(password_conf).build())
                .enqueue(new ApolloCall.Callback<CreateUserMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CreateUserMutation.Data> response) {
                        if (response.data()!=null){
                            if (response.data().createUser().email().equals(email)){
                                status = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });

        return status;
    }

    private void setData(){
        name = t_name.getText().toString();
        lastname = t_lastname.getText().toString();
        username = t_username.getText().toString();
        phone = t_phone.getText().toString();
        email = t_email.getText().toString();
        password = t_password.getText().toString();
        password_conf = t_password_conf.getText().toString();
    }

    private boolean isFull(){
        if (t_name.getText().toString().matches("")){
            return false;
        }else if (t_lastname.getText().toString().matches("")){
            return false;
        }else if (t_username.getText().toString().matches("")){
            return false;
        }else if (t_phone.getText().toString().matches("")){
            return false;
        }else if (t_password.getText().toString().matches("")){
            return false;
        }else if (t_password_conf.getText().toString().matches("")){
            return false;
        }else if (t_password.getText().toString().matches(t_password_conf.getText().toString())){
            return true;
        }
        return false;
    }

}
