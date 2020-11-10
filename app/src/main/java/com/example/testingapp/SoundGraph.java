package com.example.testingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class SoundGraph extends AppCompatActivity {

    private Double[] timeBuf = new Double[10];
    private TextView txtShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_graph);
        setTitle("Результаты тестирования");

        txtShow = (TextView) findViewById(R.id.myText2);
        Intent mIntent = getIntent();
        timeBuf[0] = mIntent.getDoubleExtra("timeBuf0", 0.0);
        timeBuf[1] = mIntent.getDoubleExtra("timeBuf1", 0.0);
        timeBuf[2] = mIntent.getDoubleExtra("timeBuf2", 0.0);
        timeBuf[3] = mIntent.getDoubleExtra("timeBuf3", 0.0);
        timeBuf[4] = mIntent.getDoubleExtra("timeBuf4", 0.0);
        timeBuf[5] = mIntent.getDoubleExtra("timeBuf5", 0.0);
        timeBuf[6] = mIntent.getDoubleExtra("timeBuf6", 0.0);
        timeBuf[7] = mIntent.getDoubleExtra("timeBuf7", 0.0);
        timeBuf[8] = mIntent.getDoubleExtra("timeBuf8", 0.0);
        timeBuf[9] = mIntent.getDoubleExtra("timeBuf9", 0.0);
        System.out.println(timeBuf[9]);

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(SoundOptions.PersonVolume, timeBuf[0]),
                new DataPoint(SoundOptions.PersonVolume + 0.05f, timeBuf[1]),
                new DataPoint(SoundOptions.PersonVolume + 0.1f, timeBuf[2]),
                new DataPoint(SoundOptions.PersonVolume + 0.15f, timeBuf[3]),
                new DataPoint(SoundOptions.PersonVolume + 0.2f, timeBuf[4]),
                new DataPoint(SoundOptions.PersonVolume + 0.25f, timeBuf[5]),
                new DataPoint(SoundOptions.PersonVolume + 0.3f, timeBuf[6]),
                new DataPoint(SoundOptions.PersonVolume + 0.35f, timeBuf[7]),
                new DataPoint(SoundOptions.PersonVolume + 0.4f, timeBuf[8]),
                new DataPoint(SoundOptions.PersonVolume + 0.45f, timeBuf[9]),
        });
        series1.setDrawDataPoints(true);
        series1.setColor(Color.GREEN);
        series1.setThickness(8);
        graph.addSeries(series1);

        /*LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0.3, 0),
        });
        series3.setDrawDataPoints(true);
        series3.setColor(Color.YELLOW);
        series3.setThickness(4);
        graph.addSeries(series3);*/

        /*LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1.2, 0),
        });
        series4.setDrawDataPoints(true);
        series4.setColor(Color.YELLOW);
        series4.setThickness(4);
        graph.addSeries(series4);*/

        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 2),
        });
        series5.setDrawDataPoints(true);
        series5.setColor(Color.YELLOW);
        series5.setThickness(4);
        graph.addSeries(series5);
    }
}