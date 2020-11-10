package com.example.testingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReactionOptions extends AppCompatActivity {
    private ArrayList<String> ColorsListMain = new ArrayList<>();
    private ArrayList<String> ColorsList = new ArrayList<>();
    private MyArrayAdapterMain mArrayAdapterMain;
    private MyArrayAdapter mArrayAdapter;
    public static String MainColor;
    public static List<String> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_options);
        setTitle("Настройка теста оперативного условного реагирования");

        ListView listViewMain = (ListView) findViewById(R.id.colorsList1);
        ListView listView = (ListView) findViewById(R.id.colorsList2);

        initMainList();
        initList();

        mArrayAdapterMain = new MyArrayAdapterMain(this, R.layout.list_item, android.R.id.text1, ColorsListMain);
        listViewMain.setAdapter(mArrayAdapterMain);
        listViewMain.setOnItemClickListener(myOnItemClickListenerMain);

        mArrayAdapter = new MyArrayAdapter(this, R.layout.list_item, android.R.id.text1, ColorsList);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(myOnItemClickListener);
    }

    private void initMainList() {
        ColorsListMain.add("Зелёный");
        ColorsListMain.add("Синий");
        ColorsListMain.add("Чёрный");
    }

    private void initList() {
        ColorsList.add("Зелёный");
        ColorsList.add("Красный");
        ColorsList.add("Синий");
        ColorsList.add("Чёрный");
        ColorsList.add("Жёлтый");
        ColorsList.add("Розовый");
    }

    AdapterView.OnItemClickListener myOnItemClickListenerMain = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mArrayAdapterMain.toggleChecked(position);
        }
    };

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mArrayAdapter.toggleChecked(position);
        }
    };

    private class MyArrayAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> mCheckedMap = new HashMap<>();

        MyArrayAdapter(Context context, int resource,
                       int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); i++) {
                mCheckedMap.put(i, false);
            }
        }

        void toggleChecked(int position) {
            if (mCheckedMap.get(position)) {
                mCheckedMap.put(position, false);
            } else {
                mCheckedMap.put(position, true);
            }

            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }

            return checkedItemPositions;
        }

        List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItems).add(ColorsList.get(i));
                }
            }

            return checkedItems;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_item, parent, false);

            ImageView iconImageView = (ImageView) row.findViewById(R.id.color);
            switch (ColorsList.get(position)){
                case "Зелёный":
                    iconImageView.setImageResource(R.drawable.circle_green);
                    break;
                case "Красный":
                    iconImageView.setImageResource(R.drawable.circle_red);
                    break;
                case "Чёрный":
                    iconImageView.setImageResource(R.drawable.circle_black);
                    break;
                case "Жёлтый":
                    iconImageView.setImageResource(R.drawable.circle_yellow);
                    break;
                case "Синий":
                    iconImageView.setImageResource(R.drawable.circle_blue);
                    break;
                case "Розовый":
                    iconImageView.setImageResource(R.drawable.circle_pink);
                    break;
            }

            CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(R.id.checkedTextView);
            checkedTextView.setText(ColorsList.get(position));

            Boolean checked = mCheckedMap.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }
    }

    private class MyArrayAdapterMain extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> mCheckedMap = new HashMap<>();

        MyArrayAdapterMain(Context context, int resource,
                       int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            for (int i = 0; i < objects.size(); i++) {
                mCheckedMap.put(i, false);
            }
        }

        void toggleChecked(int position) {
            if (mCheckedMap.get(position)) {
                mCheckedMap.put(position, false);
            } else {
                mCheckedMap.put(position, true);
            }

            notifyDataSetChanged();
        }

        public List<Integer> getCheckedItemPositions() {
            List<Integer> checkedItemPositions = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItemPositions).add(i);
                }
            }

            return checkedItemPositions;
        }

        List<String> getCheckedItems() {
            List<String> checkedItems = new ArrayList<>();

            for (int i = 0; i < mCheckedMap.size(); i++) {
                if (mCheckedMap.get(i)) {
                    (checkedItems).add(ColorsListMain.get(i));
                }
            }

            return checkedItems;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.list_item, parent, false);

            ImageView iconImageView = (ImageView) row.findViewById(R.id.color);
            switch (ColorsListMain.get(position)){
                case "Зелёный":
                    iconImageView.setImageResource(R.drawable.circle_green);
                    break;
                case "Красный":
                    iconImageView.setImageResource(R.drawable.circle_red);
                    break;
                case "Чёрный":
                    iconImageView.setImageResource(R.drawable.circle_black);
                    break;
                case "Жёлтый":
                    iconImageView.setImageResource(R.drawable.circle_yellow);
                    break;
                case "Синий":
                    iconImageView.setImageResource(R.drawable.circle_blue);
                    break;
                case "Розовый":
                    iconImageView.setImageResource(R.drawable.circle_pink);
                    break;
            }

            CheckedTextView checkedTextView = (CheckedTextView) row.findViewById(R.id.checkedTextView);
            checkedTextView.setText(ColorsListMain.get(position));

            Boolean checked = mCheckedMap.get(position);
            if (checked != null) {
                checkedTextView.setChecked(checked);
            }

            return row;
        }
    }

    public void onButtonClick(View view){
        List<String> resultListMain = mArrayAdapterMain.getCheckedItems();
        MainColor = resultListMain.get(0);

        resultList = mArrayAdapter.getCheckedItems();

        Intent intent = new Intent(".ReactionTest");
        startActivity(intent);
    }
}