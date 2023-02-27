package com.artifyai.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.DefaultApi;

public class Login extends AppCompatActivity {

    Button btnLogin;
    EditText txtPassword;
    EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void onResume() {
        super.onResume();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            txtEmail.setText(extras.getString("email"));
            txtPassword.setText(extras.getString("password"));
            try {
                btnLogin_onClick(btnLogin);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnLogin_onClick(View button) throws JSONException {

        String user = txtEmail.getText().toString();
        String pass = txtPassword.getText().toString();

        //code manual login procedure here.
        String loggedIn = login();

        if (Objects.equals(loggedIn, "200")) {
            switchToHome();
        } else {
            //display some sort of error.
        }
        //don't forget to handle google logins too.
    }

    private String login() throws JSONException {

        boolean test = true;
        if (test) return "200";

        EditText txtPassword = findViewById(R.id.txtPassword);
        EditText txtEmail = findViewById(R.id.txtNewEmail);
        String user = txtEmail.getText().toString();
        String pass = txtPassword.getText().toString();

        DefaultApi api = new DefaultApi();

        JSONObject loginResponse = api.loginAuthTokenPost(user,pass);
        String status;
        try {
            status = loginResponse.get("code").toString();
        } catch (JSONException e) {
            status = loginResponse.toString();
        }

        return status;
    }

    private Intent signupIntent = null;
    public void tvSignUpLink_onClick(View textview) {
        if(signupIntent == null) {
            signupIntent = new Intent(this, Signup.class);
        }

        startActivity(signupIntent);

    }

    public void tvForgotPassword_onClick(View textview) {
        //code forgot password here.
    }


    private Intent homeIntent = null;
    private void switchToHome(){
        if(homeIntent == null) {
            homeIntent = new Intent(this, Home.class);
        }

        startActivity(homeIntent);
    }
}