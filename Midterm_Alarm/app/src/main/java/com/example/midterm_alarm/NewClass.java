package com.example.midterm_alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class NewClass extends AppCompatActivity {
    EditText description;
    TimePicker time;
    Button add;
    DataBaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlayout);
        time = findViewById(R.id.timePicker);
        description= findViewById(R.id.editText);
        add =findViewById(R.id.button);
        databaseHelper =new DataBaseHelper(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data =  new Data(1,description.getText().toString(),time.getCurrentHour()+":"+time.getCurrentMinute());
                databaseHelper.insertContent(data);
                finish();
            }
        });
    }
}