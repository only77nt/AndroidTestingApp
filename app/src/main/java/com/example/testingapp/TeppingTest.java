package com.example.testingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import static com.example.testingapp.MainActivity.PersonName;

public class TeppingTest extends AppCompatActivity {

    private TextView timetxt;
    private Button btn, grafbtn, againbtn, resbtn, newclientbtn;
    private int ClickCount;
    private String Test;
    private boolean startApp = true;
    private Integer[] tapBuf = new Integer[6];

    private ProgressBar mProgressBar;
    private TextView EndText;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    private int res = 0;
    private String resStr;

    private long seconds;
    double power = (Math.pow(10, 9));
    long powerTime = (new Double(power)).longValue();

    int TimeMul;
    boolean RightArm;

    Double KSNS = 0.0;
    Double Akoef = 0.0;
    Integer tapSum1 = 0;
    Integer tapSum2 = 0;

    //Переменные для сохранения в DropBox
    int TestPassedCount = 0;
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
        setContentView(R.layout.activity_tepping_test);
        setTitle("Теппинг-тест");
        addListenerOnButton();

        Intent mIntent = getIntent();
        TimeMul = mIntent.getIntExtra("Time60sec", 1);
        RightArm = mIntent.getBooleanExtra("RightArm", false);

        for (int i = 0; i < tapBuf.length; i++)
            tapBuf[i] = 0;
    }

    public Integer maxArr(Integer[] array) {

        Integer maximum = array[0];

        for (int i = 0; i < array.length; i++)
            if (maximum < array[i]) maximum = array[i];

        return maximum;
    }

    public boolean apex(Integer[] array) {

        boolean res = false;

        for (int i = 0; i < array.length - 1; i++)
            if ((-2 < array[i] - array[i + 1]) && (array[i] - array[i + 1] < 2)) res = true;
            else return false;

        return res;
    }

    public void addListenerOnButton() {
        timetxt = (TextView) findViewById(R.id.textTime);
        btn = (Button) findViewById(R.id.button1);
        grafbtn = (Button) findViewById(R.id.grafbutton);
        againbtn = (Button) findViewById(R.id.againbutton);
        resbtn = (Button) findViewById(R.id.resbutton);
        newclientbtn = (Button) findViewById(R.id.newclientbutton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        EndText = (TextView) findViewById(R.id.endtext);
        ClickCount = 0;

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (startApp == true) {
                            startApp = false;
                            seconds = System.nanoTime() / powerTime;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (mProgressStatus < 100) {
                                        mProgressStatus++;
                                        if (TimeMul == 2) {
                                            android.os.SystemClock.sleep(600);
                                        } else {
                                            android.os.SystemClock.sleep(300);
                                        }
                                        Test = String.format("Время: %s секунд", String.valueOf(System.nanoTime() / powerTime - seconds));
                                        timetxt.setText(Test);
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(mProgressStatus);
                                            }
                                        });
                                    }
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn.setText("Тест окончен!");
                                            grafbtn.setVisibility(View.VISIBLE);
                                            againbtn.setVisibility(View.VISIBLE);
                                            resbtn.setVisibility(View.VISIBLE);
                                            newclientbtn.setVisibility(View.VISIBLE);
                                            TestPassedCount += 1;
                                            new AsyncRequest().execute();
                                            if (TestPassedCount == 1) {
                                                Toast.makeText(TeppingTest.this, "Пройдите тест другой рукой!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(TeppingTest.this, "Результаты успешно сохранены!", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                                }
                            }).start();
                        }
                        if (System.nanoTime() / 1000000000 - seconds >= 30 * TimeMul) {
                            return;
                        }
                        btn.setText("Нажимайте на кнопку как можно чаще!");
                        if (System.nanoTime() / 1000000000 - seconds <= 5 * TimeMul) {
                            tapBuf[0] = ClickCount;
                        }
                        if (5 < System.nanoTime() / 1000000000 - seconds && System.nanoTime() / 1000000000 - seconds <= 10 * TimeMul) {
                            tapBuf[1] = ClickCount - tapBuf[0];
                        }
                        if (10 < System.nanoTime() / 1000000000 - seconds && System.nanoTime() / 1000000000 - seconds <= 15 * TimeMul) {
                            tapBuf[2] = ClickCount - tapBuf[0] - tapBuf[1];
                        }
                        if (15 < System.nanoTime() / 1000000000 - seconds && System.nanoTime() / 1000000000 - seconds <= 20 * TimeMul) {
                            tapBuf[3] = ClickCount - tapBuf[0] - tapBuf[1] - tapBuf[2];
                        }
                        if (20 < System.nanoTime() / 1000000000 - seconds && System.nanoTime() / 1000000000 - seconds <= 25 * TimeMul) {
                            tapBuf[4] = ClickCount - tapBuf[0] - tapBuf[1] - tapBuf[2] - tapBuf[3];
                        }
                        if (25 < System.nanoTime() / 1000000000 - seconds && System.nanoTime() / 1000000000 - seconds <= 30 * TimeMul) {
                            tapBuf[5] = ClickCount - tapBuf[0] - tapBuf[1] - tapBuf[2] - tapBuf[3] - tapBuf[4];
                        }
                        if (System.nanoTime() / 1000000000 - seconds < 30 * TimeMul) {
                            ClickCount = ClickCount + 1;
                        }
                    }
                }
        );

        grafbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (startApp) {
                            resStr = "Для построения графика пройдите тестирование!";
                            AlertDialog.Builder a_builder2 = new AlertDialog.Builder(TeppingTest.this);
                            a_builder2.setMessage(resStr)
                                    .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = a_builder2.create();
                            alert.setTitle("Ваш график: ");
                            alert.show();
                        } else {
                            Intent intent = new Intent(".TeppingGraph");
                            intent.putExtra("tapBuf0", tapBuf[0]);
                            intent.putExtra("tapBuf1", tapBuf[1]);
                            intent.putExtra("tapBuf2", tapBuf[2]);
                            intent.putExtra("tapBuf3", tapBuf[3]);
                            intent.putExtra("tapBuf4", tapBuf[4]);
                            intent.putExtra("tapBuf5", tapBuf[5]);
                            intent.putExtra("RightArm", RightArm);
                            startActivity(intent);
                        }
                    }
                }
        );

        againbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RightArm = !RightArm;
                        ClickCount = 0;
                        startApp = true;
                        mProgressStatus = 0;
                        mProgressBar.setProgress(0);
                        timetxt.setText("Время: 0 секунд");
                        btn.setText("Начать тестирование");
                        grafbtn.setVisibility(View.INVISIBLE);
                        againbtn.setVisibility(View.INVISIBLE);
                        resbtn.setVisibility(View.INVISIBLE);
                        newclientbtn.setVisibility(View.INVISIBLE);
                    }
                }
        );

        resbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (startApp) {
                            resStr = "Для определения типа нервной системы пройдите тестирование!";
                        } else {
                            res = maxArr(tapBuf);

                            //Промежуточный тип - средне-слабая нервная система.
                            if (res == tapBuf[2] && (tapBuf[3] - tapBuf[4] > 0))
                                resStr = "Промежуточный тип - средне-слабая нервная система";
                            else
                                //Выпуклый тип - сильная нервная система.
                                if (res == tapBuf[1] || res == tapBuf[2])
                                    resStr = "Выпуклый тип - сильная нервная система";
                                else
                                    //Вогнутый тип - средне-слабая нервная система.
                                    if (res == tapBuf[0] && (tapBuf[4] - tapBuf[3] > 0))
                                        resStr = "Вогнутый тип - средне-слабая нервная система";
                                    else
                                        //Ровный тип - нервная система средней силы.
                                        if (apex(tapBuf))
                                            resStr = "Ровный тип - нервная система средней силы";
                                        else
                                            //Низходящий тип - слабая нервная система.
                                            if (res == tapBuf[0])
                                                resStr = "Нисходящий тип - слабая нервная система";
                                            else
                                                resStr = "Тип нервной системы не определён, попробуйте заного";
                        }
                        AlertDialog.Builder a_builder1 = new AlertDialog.Builder(TeppingTest.this);
                        resStr += "\nКСНС = " + KSNS;

                        if (TestPassedCount != 1) {
                            resStr += ", КФа = " + Akoef;
                        }

                        a_builder1.setMessage(resStr)
                                .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = a_builder1.create();
                        alert.setTitle("Результат тестирования: ");
                        alert.show();
                    }
                }
        );

        newclientbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".MainActivity");
                        startActivity(intent);
                    }
                }
        );
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

                System.out.println(TestPassedCount);

                if (TestPassedCount == 1) {
                    for (Integer i = 0; i <= 5; i++) {
                        tapSum1 += tapBuf[i];
                    }

                    for (Integer i = 1; i <= 5; i++) {
                        KSNS += tapBuf[i] - tapBuf[0];
                    }
                    KSNS = (KSNS / (double) tapBuf[0]) * 100;

                    book = new HSSFWorkbook();
                    sheet = book.createSheet("Теппинг-тест");
                    row = sheet.createRow(0);
                    row1 = sheet.createRow(1);

                    Cell name = row.createCell(0);
                    name.setCellValue("ФИО");
                    name = row1.createCell(0);
                    name.setCellValue(PersonName);

                    Cell arm = row.createCell(1);
                    arm.setCellValue("Рука");
                    arm = row1.createCell(1);
                    if (RightArm == true) {
                        arm.setCellValue("Правая");
                    } else {
                        arm.setCellValue("Левая");
                    }

                    Cell rightArm1 = row.createCell(2);
                    rightArm1.setCellValue("Пр1");
                    rightArm1 = row1.createCell(2);
                    rightArm1.setCellValue(tapBuf[0]);
                    Cell rightArm2 = row.createCell(3);
                    rightArm2.setCellValue("Пр2");
                    rightArm2 = row1.createCell(3);
                    rightArm2.setCellValue(tapBuf[1]);
                    Cell rightArm3 = row.createCell(4);
                    rightArm3.setCellValue("Пр3");
                    rightArm3 = row1.createCell(4);
                    rightArm3.setCellValue(tapBuf[2]);
                    Cell rightArm4 = row.createCell(5);
                    rightArm4.setCellValue("Пр4");
                    rightArm4 = row1.createCell(5);
                    rightArm4.setCellValue(tapBuf[3]);
                    Cell rightArm5 = row.createCell(6);
                    rightArm5.setCellValue("Пр5");
                    rightArm5 = row1.createCell(6);
                    rightArm5.setCellValue(tapBuf[4]);
                    Cell rightArm6 = row.createCell(7);
                    rightArm6.setCellValue("Пр6");
                    rightArm6 = row1.createCell(7);
                    rightArm6.setCellValue(tapBuf[5]);
                } else {
                    for (Integer i = 0; i <= 5; i++) {
                        tapSum2 += tapBuf[i];
                    }

                    Cell leftArm1 = row.createCell(8);
                    leftArm1.setCellValue("Лев1");
                    leftArm1 = row1.createCell(8);
                    leftArm1.setCellValue(tapBuf[0]);
                    Cell leftArm2 = row.createCell(9);
                    leftArm2.setCellValue("Лев2");
                    leftArm2 = row1.createCell(9);
                    leftArm2.setCellValue(tapBuf[1]);
                    Cell leftArm3 = row.createCell(10);
                    leftArm3.setCellValue("Лев3");
                    leftArm3 = row1.createCell(10);
                    leftArm3.setCellValue(tapBuf[2]);
                    Cell leftArm4 = row.createCell(11);
                    leftArm4.setCellValue("Лев4");
                    leftArm4 = row1.createCell(11);
                    leftArm4.setCellValue(tapBuf[3]);
                    Cell leftArm5 = row.createCell(12);
                    leftArm5.setCellValue("Лев5");
                    leftArm5 = row1.createCell(12);
                    leftArm5.setCellValue(tapBuf[4]);
                    Cell leftArm6 = row.createCell(13);
                    leftArm6.setCellValue("Лев6");
                    leftArm6 = row1.createCell(13);
                    leftArm6.setCellValue(tapBuf[5]);

                    Akoef = ((double) (tapSum1 - tapSum2) / (double) (tapSum1 + tapSum2)) * 100;
                    Akoef = roundAvoid(Akoef, 3);

                    Cell koefNS = row.createCell(14);
                    koefNS.setCellValue("КСНС");
                    koefNS = row1.createCell(14);
                    koefNS.setCellValue(KSNS);
                    Cell koefA = row.createCell(15);
                    koefA.setCellValue("КФа");
                    koefA = row1.createCell(15);
                    koefA.setCellValue(Akoef);
                    Cell timeCell = row.createCell(16);
                    timeCell.setCellValue("Время");
                    timeCell = row1.createCell(16);
                    if (TimeMul == 1) {
                        timeCell.setCellValue("30");
                    } else {
                        timeCell.setCellValue("60");
                    }

                    FileOutputStream fileOutput = openFileOutput("TeppingTest_" + CurrentDate + ".xls", MODE_PRIVATE);
                    book.write(fileOutput);
                    book.close();

                    PathToFiles = TeppingTest.this.getFilesDir().getAbsolutePath();

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

                    try (InputStream in = new FileInputStream(PathToFiles + "/TeppingTest_" + CurrentDate + ".xls")) {
                        FileMetadata metadata = client.files().uploadBuilder("/" + PersonName + "/TeppingTest_" + CurrentDate + ".xls")
                                .uploadAndFinish(in);
                    }
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
