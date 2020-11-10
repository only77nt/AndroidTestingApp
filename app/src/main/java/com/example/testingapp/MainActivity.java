package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String PersonName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Психофизиологическое тестирование");
    }

    public void onEnterButtonClick(View view){
        EditText PersonNameText = (EditText) findViewById(R.id.editText);
        PersonName = PersonNameText.getText().toString();

        if (PersonNameText.getText().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите идентификатор!", Toast.LENGTH_LONG);
            toast.show();

            return;
        }

        Intent intent = new Intent(".Choice");
        startActivity(intent);
    }
}
