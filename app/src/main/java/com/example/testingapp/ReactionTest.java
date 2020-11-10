package com.example.testingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.testingapp.ReactionOptions.MainColor;
import static com.example.testingapp.ReactionOptions.resultList;

public class ReactionTest extends AppCompatActivity {

    private int TestCondition = 1;
    private int ClickCount = 0;
    ObjectAnimator animator;
    Button myButton;
    ImageView orbit, screen;
    Button central;
    Path path;

    //Переменные для отсечения времени
    private long startTime = 0;
    private long endTime = 0;
    boolean firstTap = true;

    int screenWidth;
    int screenHeight;

    int orbitRadius = 455;
    int orbitXcenter;
    int orbitYcenter;

    private Integer placeInArray = 0;
    private Integer[] tapBuf = new Integer[4];
    private Double[] timeBuf = new Double[4];
    private Integer[] correctTapBuf = new Integer[4];
    private Integer[] wrongTapBuf = new Integer[4];
    private Integer[] orbitTapBuf = new Integer[4];
    private Integer[] centerTapBuf = new Integer[4];
    private Integer[] leadErrorsBuf = new Integer[4];
    private Integer[] lagErrorsBuf = new Integer[4];
    private Integer[] ErrorsBuf = new Integer[4];

    private String[] colorsBuf;
    List<String> colorsList;

    private static final String ACCESS_TOKEN = "haDaYg0hwvAAAAAAAAAADE3SV927jPMj75uQrH_GJbmBWhVqQa1e29wVdCkGOBKG";
    private String PathToFiles;

    private Timer timer;
    String testState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_test);
        setTitle("Тест оперативного условного реагирования");

        myButton = (Button) findViewById(R.id.reaction_button);
        orbit = (ImageView) findViewById(R.id.orbit);
        central = (Button) findViewById(R.id.central);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        orbitXcenter = screenWidth / 2;
        orbitYcenter = 790;

        screen = (ImageView) findViewById(R.id.Screen);
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                System.out.println(x);
                System.out.println(y);

                double distance = Math.sqrt((x - orbitXcenter) * (x - orbitXcenter) + (y - orbitYcenter) * (y - orbitYcenter));
                System.out.println(distance);

                if ((distance >= orbitRadius - 10) && (distance <= orbitRadius + 10)) {
                    orbitTapBuf[placeInArray] += 1;
                    System.out.println("Ok!");

                    int[] location = new int[2];
                    myButton.getLocationOnScreen(location);
                    System.out.println(location[0]);
                    System.out.println(location[1]);

                    if ((x > location[0]) && (y > location[1])) {
                        //По часовой: Опережение(верх право), запаздывание (низ лево)
                        if (y > orbitYcenter) {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                lagErrorsBuf[placeInArray] += 1;
                            } else {
                                leadErrorsBuf[placeInArray] += 1;
                            }
                        } else {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                leadErrorsBuf[placeInArray] += 1;
                            } else {
                                lagErrorsBuf[placeInArray] += 1;
                            }
                        }
                    } else if ((x > location[0]) && (y < location[1])) {
                        //По часовой: Опережение (верх лево), запаздывание  (низ право)
                        if (y > orbitYcenter) {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                lagErrorsBuf[placeInArray] += 1;
                            } else {
                                leadErrorsBuf[placeInArray] += 1;
                            }
                        } else {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                leadErrorsBuf[placeInArray] += 1;
                            } else {
                                lagErrorsBuf[placeInArray] += 1;
                            }
                        }
                    } else if ((x < location[0]) && (y > location[1])) {
                        //По часовой: Опережение (низ право), запаздывание (верх лево)
                        if (y > orbitYcenter) {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                leadErrorsBuf[placeInArray] += 1;
                            } else {
                                lagErrorsBuf[placeInArray] += 1;
                            }
                        } else {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                lagErrorsBuf[placeInArray] += 1;
                            } else {
                                leadErrorsBuf[placeInArray] += 1;
                            }
                        }
                    } else if ((x < location[0]) && (y < location[1])) {
                        //По часовой: Опережение (низ лево), запаздывание (верх право)
                        if (y > orbitYcenter) {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                leadErrorsBuf[placeInArray] += 1;
                            } else {
                                lagErrorsBuf[placeInArray] += 1;
                            }
                        } else {
                            if ((placeInArray == 0) || (placeInArray == 2)) {
                                lagErrorsBuf[placeInArray] += 1;
                            } else {
                                leadErrorsBuf[placeInArray] += 1;
                            }
                        }
                    }
                } else {
                    ErrorsBuf[placeInArray] += 1;
                }

                System.out.println(leadErrorsBuf[placeInArray]);
                System.out.println(lagErrorsBuf[placeInArray]);

                return false;
            }
        });

        TextView myText = (TextView) findViewById(R.id.textView9);
        myText.setText("Основной цвет: " + MainColor);

        for (int i = 0; i < tapBuf.length; i++) {
            tapBuf[i] = 0;
            timeBuf[i] = 0.0;
            correctTapBuf[i] = 0;
            wrongTapBuf[i] = 0;
            centerTapBuf[i] = 0;
            orbitTapBuf[i] = 0;
            leadErrorsBuf[i] = 0;
            lagErrorsBuf[i] = 0;
            ErrorsBuf[i] = 0;
        }
        testState = "Серый";

        colorsBuf = new String[]{"Зелёный", "Красный", "Жёлтый", "Синий", "Чёрный", "Розовый"};
        colorsList = new ArrayList<>(Arrays.asList(colorsBuf));
    }

    public void onCenterClick(View view) {
        centerTapBuf[placeInArray] += 1;
    }

    public void onTestButtonClick(View view) {
        switch (TestCondition) {
            case 1:
                colorsList = new ArrayList<String>(resultList);
                colorsList.add(MainColor);
                firstTap = true;
                timer = new Timer(new Runnable() {
                    public void run() {
                        double randInd = Math.random() * colorsList.size();
                        System.out.println(colorsList);
                        System.out.println(randInd);
                        if (colorsList.size() != 0) {
                            String color = colorsList.get((int) randInd);
                            switch (color) {
                                case "Зелёный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                    testState = "Зелёный";
                                    if (MainColor == "Зелёный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Зелёный");
                                    break;
                                case "Красный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_red));
                                    testState = "Красный";
                                    if (MainColor == "Красный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Красный");
                                    break;
                                case "Жёлтый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_yellow));
                                    testState = "Жёлтый";
                                    if (MainColor == "Жёлтый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Жёлтый");
                                    break;
                                case "Синий":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                                    testState = "Синий";
                                    if (MainColor == "Синий") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Синий");
                                    break;
                                case "Чёрный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_black));
                                    testState = "Чёрный";
                                    if (MainColor == "Чёрный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Чёрный");
                                    break;
                                case "Розовый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_pink));
                                    testState = "Розовый";
                                    if (MainColor == "Розовый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Розовый");
                                    break;
                            }
                        }
                    }
                }, 2000, true);

                path = new Path();
                //path.arcTo(25f, 300f, 950f, 1175f, 0f, 359f, true);
                path.addCircle(view.getX() - 450, view.getY(), 450, Path.Direction.CW);
                animator = ObjectAnimator.ofFloat(myButton, View.X, View.Y, path);
                animator.setDuration(2000 * (colorsList.size() + 1));
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        TestCondition = 2;
                        placeInArray++;
                        timer.stopTimer();
                        central.setBackground(getResources().getDrawable(R.drawable.circle));
                        testState = "Серый";
                    }
                });
                TestCondition = 5;
                //setAnimValues(animator,6000,ValueAnimator.RESTART);
                animator.start();

                break;
            case 2:
                colorsList = new ArrayList<String>(resultList);
                colorsList.add(MainColor);
                firstTap = true;
                timer = new Timer(new Runnable() {
                    public void run() {
                        double randInd = Math.random() * colorsList.size();
                        System.out.println(colorsList);
                        System.out.println(randInd);
                        if (colorsList.size() != 0) {
                            String color = colorsList.get((int) randInd);
                            switch (color) {
                                case "Зелёный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                    testState = "Зелёный";
                                    if (MainColor == "Зелёный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Зелёный");
                                    break;
                                case "Красный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_red));
                                    testState = "Красный";
                                    if (MainColor == "Красный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Красный");
                                    break;
                                case "Жёлтый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_yellow));
                                    testState = "Жёлтый";
                                    if (MainColor == "Жёлтый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Жёлтый");
                                    break;
                                case "Синий":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                                    testState = "Синий";
                                    if (MainColor == "Синий") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Синий");
                                    break;
                                case "Чёрный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_black));
                                    testState = "Чёрный";
                                    if (MainColor == "Чёрный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Чёрный");
                                    break;
                                case "Розовый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_pink));
                                    testState = "Розовый";
                                    if (MainColor == "Розовый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Розовый");
                                    break;
                            }
                        }
                    }
                }, 2000, true);

                path = new Path();
                //path.arcTo(25f, 300f, 950f, 1175f, 0f, -359f, true);
                path.addCircle(view.getX() - 450, view.getY(), 450, Path.Direction.CCW);
                animator = ObjectAnimator.ofFloat(myButton, View.X, View.Y, path);
                animator.setDuration(2000 * (colorsList.size() + 1));
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        TestCondition = 3;
                        placeInArray++;
                        timer.stopTimer();
                        central.setBackground(getResources().getDrawable(R.drawable.circle));
                        testState = "Серый";
                    }
                });
                TestCondition = 5;
                animator.start();

                break;
            case 3:
                colorsList = new ArrayList<String>(resultList);
                colorsList.add(MainColor);
                firstTap = true;
                timer = new Timer(new Runnable() {
                    public void run() {
                        double randInd = Math.random() * colorsList.size();
                        System.out.println(colorsList);
                        System.out.println(randInd);
                        if (colorsList.size() != 0) {
                            String color = colorsList.get((int) randInd);
                            switch (color) {
                                case "Зелёный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                    testState = "Зелёный";
                                    if (MainColor == "Зелёный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Зелёный");
                                    break;
                                case "Красный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_red));
                                    testState = "Красный";
                                    if (MainColor == "Красный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Красный");
                                    break;
                                case "Жёлтый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_yellow));
                                    testState = "Жёлтый";
                                    if (MainColor == "Жёлтый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Жёлтый");
                                    break;
                                case "Синий":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                                    testState = "Синий";
                                    if (MainColor == "Синий") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Синий");
                                    break;
                                case "Чёрный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_black));
                                    testState = "Чёрный";
                                    if (MainColor == "Чёрный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Чёрный");
                                    break;
                                case "Розовый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_pink));
                                    testState = "Розовый";
                                    if (MainColor == "Розовый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Розовый");
                                    break;
                            }
                        }
                    }
                }, 2000, true);

                orbit.setVisibility(View.INVISIBLE);
                path = new Path();
                //path.arcTo(25f, 300f, 950f, 1175f, 0f, 359f, true);
                path.addCircle(view.getX() - 450, view.getY(), 450, Path.Direction.CW);
                animator = ObjectAnimator.ofFloat(myButton, View.X, View.Y, path);
                animator.setDuration(2000 * (colorsList.size() + 1));
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        TestCondition = 4;
                        placeInArray++;
                        timer.stopTimer();
                        central.setBackground(getResources().getDrawable(R.drawable.circle));
                        testState = "Серый";
                    }
                });
                TestCondition = 5;
                animator.start();

                break;
            case 4:
                colorsList = new ArrayList<String>(resultList);
                colorsList.add(MainColor);
                firstTap = true;
                timer = new Timer(new Runnable() {
                    public void run() {
                        double randInd = Math.random() * colorsList.size();
                        System.out.println(colorsList);
                        System.out.println(randInd);
                        if (colorsList.size() != 0) {
                            String color = colorsList.get((int) randInd);
                            switch (color) {
                                case "Зелёный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_green));
                                    testState = "Зелёный";
                                    if (MainColor == "Зелёный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Зелёный");
                                    break;
                                case "Красный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_red));
                                    testState = "Красный";
                                    if (MainColor == "Красный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Красный");
                                    break;
                                case "Жёлтый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_yellow));
                                    testState = "Жёлтый";
                                    if (MainColor == "Жёлтый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Жёлтый");
                                    break;
                                case "Синий":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                                    testState = "Синий";
                                    if (MainColor == "Синий") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Синий");
                                    break;
                                case "Чёрный":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_black));
                                    testState = "Чёрный";
                                    if (MainColor == "Чёрный") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Чёрный");
                                    break;
                                case "Розовый":
                                    central.setBackground(getResources().getDrawable(R.drawable.circle_pink));
                                    testState = "Розовый";
                                    if (MainColor == "Розовый") {
                                        startTime = System.currentTimeMillis();
                                    }
                                    colorsList.remove("Розовый");
                                    break;
                            }
                        }
                    }
                }, 2000, true);

                path = new Path();
                //path.arcTo(25f, 300f, 950f, 1175f, 0f, -359f, true);
                path.addCircle(view.getX() - 450, view.getY(), 450, Path.Direction.CCW);
                animator = ObjectAnimator.ofFloat(myButton, View.X, View.Y, path);
                animator.setDuration(2000 * (colorsList.size() + 1));
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        TestCondition = 6;
                        timer.stopTimer();
                        central.setBackground(getResources().getDrawable(R.drawable.circle));
                        testState = "Серый";
                    }
                });
                TestCondition = 5;
                animator.start();

                break;
            case 5:
                if (testState == MainColor) {
                    correctTapBuf[placeInArray] += 1;
                    if (firstTap == true) {
                        endTime = System.currentTimeMillis();
                        double timeInterval = (endTime - startTime) / 1000.0;
                        timeBuf[placeInArray] = timeInterval;
                        System.out.println(timeBuf[placeInArray]);
                    }
                    firstTap = false;
                } else {
                    wrongTapBuf[placeInArray] += 1;
                    break;
                }
                tapBuf[placeInArray]++;
                break;
            case 6:
                TextView endOfTestText = (TextView) findViewById(R.id.textView9);
                endOfTestText.setText("Тестирование окончено!");

                new AsyncRequest().execute();
                Toast.makeText(ReactionTest.this, "Результаты успешно сохранены!", Toast.LENGTH_LONG).show();
                TestCondition = 7;
        }

        return;
    }

    public void setAnimValues(ObjectAnimator objectAnimator, int duration, int repeatMode) {
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(repeatMode);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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

                String myTxt = "";

                for (int i = 0; i < tapBuf.length; i++) {
                    myTxt = myTxt + Integer.toString(i + 1) + ":\n"
                            + "Время первого корректного нажатия: " + timeBuf[i].toString() + "\n"
                            + "Верных нажатий: " + correctTapBuf[i].toString() + "\n"
                            + "Неверных нажатий (когда центральный стимул другого цвета): " + wrongTapBuf[i].toString() + "\n"
                            + "Нажатий куда-то за пределы рабочей зоны: " + ErrorsBuf[i].toString() + "\n"
                            + "Нажатий на орбиту: " + orbitTapBuf[i].toString() + "\n"
                            + "Нажатий на центральный стимул: " + centerTapBuf[i].toString() + "\n"
                            + "Ошибок опережения: " + leadErrorsBuf[i].toString() + "\n"
                            + "Ошибок запаздывания: " + lagErrorsBuf[i].toString() + "\n"
                            + "\n";
                }
                try {
                    FileOutputStream fileOutput = openFileOutput("ReactionTest_" + CurrentDate + ".txt", MODE_PRIVATE);
                    fileOutput.write(myTxt.getBytes());
                    fileOutput.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                PathToFiles = ReactionTest.this.getFilesDir().getAbsolutePath();

                try {
                    FolderMetadata folder = client.files().createFolder("/" + MainActivity.PersonName);
                    System.out.println(folder.getName());
                } catch (CreateFolderErrorException err) {
                    if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
                        System.out.println("Something already exists at the path.");
                    } else {
                        System.out.print("Some other CreateFolderErrorException occurКрасный...");
                        System.out.print(err.toString());
                    }
                } catch (Exception err) {
                    System.out.print("Some other Exception occurКрасный...");
                    System.out.print(err.toString());
                }

                try (InputStream in = new FileInputStream(PathToFiles + "/ReactionTest_" + CurrentDate + ".txt")) {
                    FileMetadata metadata = client.files().uploadBuilder("/" + MainActivity.PersonName + "/ReactionTest_" + CurrentDate + ".txt")
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

    public class Timer {
        private Handler handler;
        private boolean paused;

        private int interval;

        private Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!paused) {
                    runnable.run();
                    Timer.this.handler.postDelayed(this, interval);
                }
            }
        };

        private Runnable runnable;

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public void startTimer() {
            paused = false;
            handler.postDelayed(task, interval);
        }

        public void stopTimer() {
            paused = true;
        }

        public Timer(Runnable runnable, int interval, boolean started) {
            handler = new Handler();
            this.runnable = runnable;
            this.interval = interval;
            if (started)
                startTimer();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(".Choice");
        startActivity(intent);
        finish();
    }
}