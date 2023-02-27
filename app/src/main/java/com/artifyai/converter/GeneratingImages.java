package com.artifyai.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.service.autofill.TextValueSanitizer;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Text;

public class GeneratingImages extends AppCompatActivity {

    TextView txtTimer = (TextView) findViewById(R.id.txtTimer);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating_images);

        // Set the initial value of your countdown timer (in milliseconds)
        long countdownTimer = 15*60*1000; // 15 minutes

        // Create a countdown timer object that counts down from the specified value
        new CountDownTimer(countdownTimer, 1000) {

            public void onTick(long millisUntilFinished) {
                // Calculate the remaining time in hh:mm:ss format
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                // Update the textview with the remaining time
                txtTimer.setText(time);
            }

            public void onFinish() {
                // Do something when the countdown timer finishes
                txtTimer.setText("00:00:00");

                //Then go to the display page.
                switchToResults();
            }

        }.start();
    }

    private Intent resultsIntent = null;
    private void switchToResults(){
        if(resultsIntent == null) {
            resultsIntent = new Intent(this, Results.class);
        }
        startActivity(resultsIntent);
    }


}