package com.example.testingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class TeppingGraph extends AppCompatActivity {

    private Integer[] tapBuf = new Integer[6];
    boolean RightArm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tepping_graph);
        setTitle("Результаты тестирования");

        Intent mIntent = getIntent();
        tapBuf[0] = mIntent.getIntExtra("tapBuf0", 0);
        tapBuf[1] = mIntent.getIntExtra("tapBuf1", 0);
        tapBuf[2] = mIntent.getIntExtra("tapBuf2", 0);
        tapBuf[3] = mIntent.getIntExtra("tapBuf3", 0);
        tapBuf[4] = mIntent.getIntExtra("tapBuf4", 0);
        tapBuf[5] = mIntent.getIntExtra("tapBuf5", 0);
        RightArm = mIntent.getBooleanExtra("RightArm", false);
        TextView armText = (TextView) findViewById(R.id.armText);
        if (RightArm == true) {
            armText.setText("Рука: правая");
        } else {
            armText.setText("Рука: левая");
        }

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, tapBuf[0]),
                new DataPoint(1, tapBuf[0]),
                new DataPoint(2, tapBuf[0]),
                new DataPoint(3, tapBuf[0]),
                new DataPoint(4, tapBuf[0]),
                new DataPoint(5, tapBuf[0]),
                new DataPoint(6, tapBuf[0])
        });
        series2.setDrawDataPoints(true);
        series2.setColor(Color.RED);
        series2.setThickness(4);
        graph.addSeries(series2);

        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, tapBuf[0]),
                new DataPoint(1, tapBuf[0]),
                new DataPoint(2, tapBuf[1]),
                new DataPoint(3, tapBuf[2]),
                new DataPoint(4, tapBuf[3]),
                new DataPoint(5, tapBuf[4]),
                new DataPoint(6, tapBuf[5])
        });
        series1.setDrawDataPoints(true);
        series1.setColor(Color.GREEN);
        series1.setThickness(8);
        graph.addSeries(series1);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
        });
        series3.setDrawDataPoints(true);
        series3.setColor(Color.YELLOW);
        series3.setThickness(4);
        graph.addSeries(series3);

        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 40),
        });
        series4.setDrawDataPoints(true);
        series4.setColor(Color.YELLOW);
        series4.setThickness(4);
        graph.addSeries(series4);
    }
}
