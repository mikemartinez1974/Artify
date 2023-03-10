package com.artifyai.converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.DefaultApi;


public class Signup extends AppCompatActivity {

    private boolean formIsValid() {

        EditText email = findViewById(R.id.txtNewEmail);
        String ema = email.getText().toString().trim();
        Pattern validEmailRegex = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
        Matcher emailvalidator = validEmailRegex.matcher(ema);
        boolean emailIsValid = emailvalidator.find();
        if (ema.length() == 0) {
            email.setError("This field is required.");
            return false;
        }
        else if (!emailIsValid) {
            email.setError("Email is invalid.");
            return false;
        }
        else {
            //email.setError("");
        }



        EditText birthdate = findViewById(R.id.txtBirthdate);
        String dob = birthdate.getText().toString().trim();
        System.out.println("dob: " + dob);
        Pattern validDate = Pattern.compile("(?=\\d)^(?:(?!(?:10\\D(?:0?[5-9]|1[0-4])\\D(?:1582))|(?:0?9\\D(?:0?[3-9]|1[0-3])\\D(?:1752)))((?:0?[13578]|1[02])|(?:0?[469]|11)(?!\\/31)(?!-31)(?!\\.31)|(?:0?2(?=.?(?:(?:29.(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:(?:\\d\\d)(?:[02468][048]|[13579][26])(?!\\x20BC))|(?:00(?:42|3[0369]|2[147]|1[258]|09)\\x20BC))))))|(?:0?2(?=.(?:(?:\\d\\D)|(?:[01]\\d)|(?:2[0-8])))))([-.\\/])(0?[1-9]|[12]\\d|3[01])\\2(?!0000)((?=(?:00(?:4[0-5]|[0-3]?\\d)\\x20BC)|(?:\\d{4}(?!\\x20BC)))\\d{4}(?:\\x20BC)?)(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$");
        Matcher datevalidator = validDate.matcher(dob);
        boolean dateIsValid = datevalidator.find();
        if (!dateIsValid) {
            birthdate.setError(("Invalid date."));
            return false;
        }
        else {
            int age = 13;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate birth = LocalDate.parse(dob, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                LocalDate today = LocalDate.now();
                Period lifetime = Period.between(birth,today);
                age = lifetime.getYears();
            }

            if (age < 13){
                birthdate.setError("Minimum age is 13.");
                return false;
            }
            else {
                //birthdate.setError("");
            }
        }



        EditText password = findViewById(R.id.txtPassword);
        String pw = password.getText().toString().trim();
        Pattern validPasswordRegex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])((?=.*\\d)|(?=.*[!@#$%^&*()'\"]))[A-Za-z\\d!@#$%^&*()'\"](?!\\s).{5,21}$");
        Matcher passwordvalidator = validPasswordRegex.matcher(pw);
        boolean regexMatches = passwordvalidator.find();
        if(!regexMatches) {
            password.setError("Password must be at least 6 characters, no more than 20 characters, and must include at least one upper case letter, one lower case letter, and one numeric digit or one special character.");
            return false;
        }
        else {
            //password.setError("");
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    private Intent loginIntent = null;

    public void tvSignInLink_onClick(View textview) {
        switchToLogin();
    }

    public void btnSignup_onClick(View button) {
        //account creation code here.

        Button btnSignup = findViewById(R.id.btnSignup);

        if(formIsValid()){
            boolean accountCreated = createAccount();
            if(accountCreated) {
                switchToLogin();
            }
            else {
                alert("Account creation failed.");
            }
        }
    }

    private boolean createAccount(){
        Button btnSignup = findViewById(R.id.btnSignup);
        EditText password = findViewById(R.id.txtPassword);
        EditText birthdate = findViewById(R.id.txtBirthdate);
        EditText email = findViewById(R.id.txtNewEmail);

        String ema = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        DefaultApi api = new DefaultApi();
        UserCreate newUser = new UserCreate();
        newUser.setEmail(ema);
        newUser.setPassword(p);

        boolean bResult = false;
        try {
            Object result = api.registerAuthRegisterPost(newUser);
            bResult = ((JSONObject) result).getInt("status") == 200;
        } catch (Exception e) {
            alert(e.toString());
            System.err.println("Exception when calling DefaultApi#registerAuthRegisterPost");
            e.printStackTrace();
        }

        return bResult;
    }

    private void switchToLogin() {
        Button btnSignup = (Button) findViewById(R.id.btnSignup);
        EditText password = findViewById(R.id.txtPassword);
        EditText birthdate = findViewById(R.id.txtBirthdate);
        EditText email = findViewById(R.id.txtNewEmail);

        if(loginIntent == null) {
            loginIntent = new Intent (this, Login.class);
        }

        String emailText = String.valueOf(email.getText());
        String passwordText = String.valueOf(password.getText());

        if((emailText.length() > 0) && (passwordText.length() > 0)) {
            loginIntent.putExtra("email", email.getText());
            loginIntent.putExtra("password",password.getText());
        }

        startActivity(loginIntent);
    }

    private void alert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}