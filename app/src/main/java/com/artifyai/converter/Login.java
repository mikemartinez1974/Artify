package com.artifyai.converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.swagger.client.*;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;

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

        btnLogin = findViewById(R.id.btnLogin);
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            txtEmail.setText(extras.getString("email"));
            txtPassword.setText(extras.getString("password"));
            try {
                btnLogin_onClick(btnLogin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnLogin_onClick(View button) throws JSONException, ApiException {
        //code manual login procedure here.
        String loggedIn = login();

        if (Objects.equals(loggedIn, "200")) {
            switchToHome();
        } else {
            //display some sort of error.
        }
        //don't forget to handle google logins too.
    }

    private String login() throws JSONException, ApiException {

        boolean test = false;

        if (test) return "200";

        EditText txtPassword = findViewById(R.id.txtPassword);
        EditText txtEmail = findViewById(R.id.txtNewEmail);
        String user = txtEmail.getText().toString();
        String pass = txtPassword.getText().toString();

        DefaultApi api = new DefaultApi();

        Object loginResponse;
        try {
            loginResponse = api.loginAuthTokenPost("", user, pass,"","","" );
        } catch (ApiException e) {

            throw e;
        }


        String status;
        try {
            status = ((JSONObject)loginResponse).get("code").toString();
        } catch (JSONException e) {
            status = loginResponse.toString();
            throw(e);
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