package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.concurrent.TimeUnit;

public class SoundOptions extends AppCompatActivity {

    private boolean Colibrated = false;
    private float count = 0.2f;
    MyTask myTask;
    Button btn;

    public static float PersonVolume = 0.01f;

    private MediaPlayer musicSound;
    String[] sounds = {"До 1-ой октавы", "До 2-ой октавы", "До 3-ей октавы"};
    public static String[] soundsBuf = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_options);
        setTitle("Слуховое сенсомоторное тестирование");

        for (int i = 0; i < soundsBuf.length; i++)
            soundsBuf[i] = "";

        //musicSound = MediaPlayer.create(this, R.raw.do1);
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*0.5f),0);

        btn = (Button)findViewById(R.id.calibrateSound);

        Spinner spinner1 = (Spinner) findViewById(R.id.option1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sounds);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        AdapterView.OnItemSelectedListener itemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = (String)parent.getItemAtPosition(position);
                soundsBuf[0] = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String item = (String)parent.getItemAtPosition(0);
                soundsBuf[0] = item;
            }
        };

        spinner1.setOnItemSelectedListener(itemSelectedListener1);
    }

    public void CalibrationCheck(View view){
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Colibrated != true){
            Colibrated = true;
            btn.setText("Нажмите, когда услышите звук!");
            count = 0.2f;

            switch (soundsBuf[0]) {
                case "До 1-ой октавы":
                    musicSound = MediaPlayer.create(this, R.raw.do1);
                    break;
                case "До 2-ой октавы":
                    musicSound = MediaPlayer.create(this, R.raw.do2);
                    break;
                case "До 3-ей октавы":
                    musicSound = MediaPlayer.create(this, R.raw.do3);
                    break;
            }

            audio.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*count),0);
            soundPlay(musicSound);
            myTask = new MyTask();
            myTask.execute();
        }
        else{
            myTask.cancel(true);
            soundStop(musicSound);
            Intent intent = new Intent(".SoundTest");
            PersonVolume = count;
            startActivity(intent);
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                for(;count < 1.0f;) {
                    count += 0.01f;
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * count), 0);
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public void soundPlay(MediaPlayer sound){
        sound.start();
    }

    public void soundStop(MediaPlayer sound){
        sound.pause();
    }
}
