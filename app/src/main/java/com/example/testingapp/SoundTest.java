package com.example.testingapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.testingapp.MainActivity.PersonName;
import static com.example.testingapp.SoundOptions.soundsBuf;

public class SoundTest extends AppCompatActivity {

    private long startTime = 0;
    private long endTime = 0;
    int iterationCount = 0;
    private Double[] timeBuf = new Double[10];
    private double volume = SoundOptions.PersonVolume;

    Button btn;
    private MediaPlayer musicSound;

    //Переменные для сохранения в DropBox
    private String PathToFiles;
    private static final String ACCESS_TOKEN = "haDaYg0hwvAAAAAAAAAADE3SV927jPMj75uQrH_GJbmBWhVqQa1e29wVdCkGOBKG";

    //Переменные для сохранения в Excel
    Workbook book;
    Sheet sheet;
    Row row;
    Row row1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_test);
        setTitle("Слуховое сенсомоторное тестирование");

        Intent mIntent = getIntent();

        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume), 0);

        btn = (Button) findViewById(R.id.button5);

        for (int i = 0; i < timeBuf.length; i++)
            timeBuf[i] = 0.0;
    }

    public void onButtonClick(View view) {
        TextView timeText = (TextView) findViewById(R.id.textView12);

        if (iterationCount > 10) {
            return;
        }

        if (iterationCount == 10) {
            soundStop(musicSound);
            endTime = System.currentTimeMillis();
            double timeInterval = (endTime - startTime) / 1000.0;
            timeBuf[iterationCount - 1] = timeInterval;
            timeText.setText("Время: " + timeBuf[iterationCount - 1] + " секунд!");

            btn.setText("Тестирование окончено!");
            Button againBtn = (Button) findViewById(R.id.button6);
            againBtn.setVisibility(View.VISIBLE);
            Button grafBtn = (Button) findViewById(R.id.button8);
            grafBtn.setVisibility(View.VISIBLE);

            new AsyncRequest().execute();
            Toast.makeText(SoundTest.this, "Результаты успешно сохранены!", Toast.LENGTH_LONG).show();
            iterationCount++;

            return;
        }

        btn.setText("Нажмите, когда услышите звук!");

        if (iterationCount == 0) {
            selectMusicSound(0);
            long randTime = getRandNum(1, 5);

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                public void run() {
                    soundPlay(musicSound);
                    startTime = System.currentTimeMillis();
                }
            };
            executor.schedule(task, 3, TimeUnit.SECONDS); //Поставить RandTime
            executor.shutdown();
        } else {
            soundStop(musicSound);
            endTime = System.currentTimeMillis();
            double timeInterval = (endTime - startTime) / 1000.0;
            timeBuf[iterationCount - 1] = timeInterval;
            timeText.setText("Время: " + timeBuf[iterationCount - 1] + " секунд!");

            long randTime = getRandNum(1, 10);
            volume += 0.05f;

            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume), 0);

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            Runnable task = new Runnable() {
                public void run() {
                    soundPlay(musicSound);
                    startTime = System.currentTimeMillis();
                }
            };
            executor.schedule(task, 3, TimeUnit.SECONDS); //Поставить RandTime
            executor.shutdown();
        }

        iterationCount++;
    }

    public void onGraphButtonClick(View view) {
        System.out.println(timeBuf[9]);
        Intent intent = new Intent(".SoundGraph");
        intent.putExtra("timeBuf0", timeBuf[0]);
        intent.putExtra("timeBuf1", timeBuf[1]);
        intent.putExtra("timeBuf2", timeBuf[2]);
        intent.putExtra("timeBuf3", timeBuf[3]);
        intent.putExtra("timeBuf4", timeBuf[4]);
        intent.putExtra("timeBuf5", timeBuf[5]);
        intent.putExtra("timeBuf6", timeBuf[6]);
        intent.putExtra("timeBuf7", timeBuf[7]);
        intent.putExtra("timeBuf8", timeBuf[8]);
        intent.putExtra("timeBuf9", timeBuf[9]);
        startActivity(intent);
    }

    public void onRestartButtonClick(View view) {
        startTime = 0;
        endTime = 0;
        iterationCount = 0;
        btn.setText("Начать тестирование");
        volume = SoundOptions.PersonVolume;
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume), 0);
        Button AgainBtn = (Button) findViewById(R.id.button6);
        AgainBtn.setVisibility(View.INVISIBLE);
        Button GraphBtn = (Button) findViewById(R.id.button8);
        GraphBtn.setVisibility(View.INVISIBLE);
    }

    public void onNewClientButtonClick(View view) {
        Intent intent = new Intent(".MainActivity");
        startActivity(intent);
    }

    public void soundPlay(MediaPlayer sound) {
        sound.start();
    }

    public void soundStop(MediaPlayer sound) {
        sound.pause();
    }

    public void selectMusicSound(Integer i) {
        switch (soundsBuf[i]) {
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
    }

    public int getRandNum(int min, int max) {
        Random rnd = new Random(System.currentTimeMillis());
        return min + rnd.nextInt(max - min + 1);
    }

    class AsyncRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg) {
            try {
                DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
                DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
                Date date = new Date();
                String CurrentDate = dateFormat.format(date);
                book = new HSSFWorkbook();
                sheet = book.createSheet("Теппинг-тест");
                row = sheet.createRow(0);
                row1 = sheet.createRow(1);

                Cell name = row.createCell(0);
                name.setCellValue("ФИО");
                name = row1.createCell(0);
                name.setCellValue(PersonName);

                Cell iter1 = row.createCell(1);
                iter1.setCellValue("Ит.1" + " (" + roundAvoid(SoundOptions.PersonVolume,3) + ")");
                iter1 = row1.createCell(1);
                iter1.setCellValue(timeBuf[0]);
                Cell iter2 = row.createCell(2);
                iter2.setCellValue("Ит.2" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.05f,3) + ")");
                iter2 = row1.createCell(2);
                iter2.setCellValue(timeBuf[1]);
                Cell iter3 = row.createCell(3);
                iter3.setCellValue("Ит.3" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.1f,3) + ")");
                iter3 = row1.createCell(3);
                iter3.setCellValue(timeBuf[2]);
                Cell iter4 = row.createCell(4);
                iter4.setCellValue("Ит.4" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.15f,3) + ")");
                iter4 = row1.createCell(4);
                iter4.setCellValue(timeBuf[3]);
                Cell iter5 = row.createCell(5);
                iter5.setCellValue("Ит.5" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.2f,3) + ")");
                iter5 = row1.createCell(5);
                iter5.setCellValue(timeBuf[4]);
                Cell iter6 = row.createCell(6);
                iter6.setCellValue("Ит.6" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.25f,3) + ")");
                iter6 = row1.createCell(6);
                iter6.setCellValue(timeBuf[5]);
                Cell iter7 = row.createCell(7);
                iter7.setCellValue("Ит.7" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.3f,3) + ")");
                iter7 = row1.createCell(7);
                iter7.setCellValue(timeBuf[6]);
                Cell iter8 = row.createCell(8);
                iter8.setCellValue("Ит.8" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.35f,3) + ")");
                iter8 = row1.createCell(8);
                iter8.setCellValue(timeBuf[7]);
                Cell iter9 = row.createCell(9);
                iter9.setCellValue("Ит.9" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.4f,3) + ")");
                iter9 = row1.createCell(9);
                iter9.setCellValue(timeBuf[8]);
                Cell iter10 = row.createCell(10);
                iter10.setCellValue("Ит.10" + " (" + roundAvoid(SoundOptions.PersonVolume + 0.45f,3) + ")");
                iter10 = row1.createCell(10);
                iter10.setCellValue(timeBuf[9]);

                FileOutputStream fileOutput = openFileOutput("SoundTest_" + CurrentDate + ".xls", MODE_PRIVATE);
                book.write(fileOutput);
                book.close();

                PathToFiles = SoundTest.this.getFilesDir().getAbsolutePath();

                try {
                    FolderMetadata folder = client.files().createFolder("/" + PersonName);
                    System.out.println(folder.getName());
                } catch (CreateFolderErrorException err) {
                    if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
                        System.out.println("Something already exists at the path.");
                    } else {
                        System.out.print("Some other CreateFolderErrorException occurred...");
                        System.out.print(err.toString());
                    }
                } catch (Exception err) {
                    System.out.print("Some other Exception occurred...");
                    System.out.print(err.toString());
                }

                try (InputStream in = new FileInputStream(PathToFiles + "/SoundTest_" + CurrentDate + ".xls")) {
                    FileMetadata metadata = client.files().uploadBuilder("/" + PersonName + "/SoundTest_" + CurrentDate + ".xls")
                            .uploadAndFinish(in);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(".Choice");
        startActivity(intent);
        finish();
    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}