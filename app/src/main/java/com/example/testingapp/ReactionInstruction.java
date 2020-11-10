package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ReactionInstruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_instruction);
        setTitle("Инструкция теста оперативного условного реагирования");
    }

    public void onButtonClick(View view){
        Intent intent = new Intent(".ReactionOptions");
        startActivity(intent);
    }
}
