package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Choice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        setTitle("Психофизиологическое тестирование");
    }

    public void onTeppingTestButtonClick(View view){
        Intent intent = new Intent(".TeppingOptions");
        startActivity(intent);
    }

    public void onSoundTestButtonClick(View view){
        Intent intent = new Intent(".SoundOptions");
        startActivity(intent);
    }

    public void onReactionTestButtonClick(View view){
        Intent intent = new Intent(".ReactionInstruction");
        startActivity(intent);
    }
}
