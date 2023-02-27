package com.artifyai.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void onResume() {
        super.onResume();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        EditText password = (EditText) findViewById(R.id.txtPassword);
        EditText email = (EditText) findViewById(R.id.txtEmail);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            email.setText(extras.getString("email"));
            password.setText(extras.getString("password"));
            btnLogin_onClick(btnLogin);
        }
    }

    public void btnLogin_onClick(View button) {
        //code manual login procedure here.

        //don't forget to handle google logins too.

        double userid = login();

        if(userid > 0){
            //switch to home page.
            switchToHome();
        }
    }

    private double login(){

        boolean test = true;
        if (test) return 1;

        EditText password = findViewById(R.id.txtPassword);
        EditText email = findViewById(R.id.txtNewEmail);

        String url = "jdbc:mysql://localhost:3306/mydatabase"; // URL of the database
        String dbUser = "myuser"; // Username for the database
        String dbPass = "mypassword"; // Password for the database

        double userid = 1;

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Create a connection to the database
            Connection conn = DriverManager.getConnection(url, dbUser, dbPass);

//            // Create a statement
            Statement stmt = conn.createStatement();
//
//            // Execute a query
            ResultSet rs = stmt.executeQuery("SELECT * FROM mytable");
//
//            // Iterate over the result set and print the values
//            while (rs.next()) {
//                String column1 = rs.getString("column1");
//                int column2 = rs.getInt("column2");
//                System.out.println("column1: " + column1 + ", column2: " + column2);
//            }

            // Close the connection
            conn.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }



        return userid;
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