package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class TeppingOptions extends AppCompatActivity {

    public int Check = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tepping_options);
        setTitle("Теппинг-тест");

        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Check = 2;
                }
            }
        });
    }

    public void onLeftButtonClick(View view){
        Intent intent = new Intent(".TeppingTest");
        intent.putExtra("Time60sec", Check);
        intent.putExtra("RightArm", false);
        startActivity(intent);
    }

    public void onRightButtonClick(View view){
        Intent intent = new Intent(".TeppingTest");
        intent.putExtra("Time60sec", Check);
        intent.putExtra("RightArm", true);
        startActivity(intent);
    }
}