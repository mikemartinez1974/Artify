package com.artifyai.converter;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Results extends AppCompatActivity {

    private int MaxFileCount;

    HorizontalScrollView hsvResults;
    LinearLayout llResults;
    ImageView ivResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
    }


}