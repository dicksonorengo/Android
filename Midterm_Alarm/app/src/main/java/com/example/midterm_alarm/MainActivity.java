package com.example.midterm_alarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<Data> contents;
    DataAdapter adapter;;
    DataBaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv= findViewById(R.id.listView1);
        databaseHelper = new DataBaseHelper(this);
        contents = new ArrayList<>();
        contents = databaseHelper.getContents();
        adapter = new DataAdapter(this,contents);
        lv.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(),NewClass.class);
                startActivity(intent);
                break;
        }
        return  true;
    }
    public void newAlarmClick(View view){
        Intent intent = new Intent(getApplicationContext(),NewClass.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        contents = databaseHelper.getContents();
        adapter = new DataAdapter(this,contents);
        lv.setAdapter(adapter);
    }
}

