package com.example.notes3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    public static final String SHARED_PREF = "prefs";
    public static final String KEY_BUTTON_TEXT = "keyButtonText";
    public static final String COLOR_TEXT = "colorText";

    private String text = "";
    private int color_id = 0;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private ListView listView;
    private WidgetNoteAdapter adapter;
    private DatabaseHelper helper;
    ArrayList<Note> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        listView = findViewById(R.id.notes_list);
        helper = new DatabaseHelper(this);
        notes = new ArrayList<>();
        notes = helper.getNotes();
        adapter = new WidgetNoteAdapter(this, notes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text = notes.get(position).getText();
                color_id = notes.get(position).getCurrent_color();
                confirmConfiguration();
            }
        });

    }
    public void confirmConfiguration(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent  = new Intent(this, Main2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this ,0, intent,0);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.app_widget);
        remoteViews.setOnClickPendingIntent(R.id.imageView12, pendingIntent);
        remoteViews.setCharSequence(R.id.appwidget_text__, "setText", text);
        remoteViews.setInt(R.id.appwidget_layout, "setBackgroundColor",
                this.getResources().getColor(MainActivity.colors[color_id]));

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BUTTON_TEXT+appWidgetId, text);
        editor.putInt(COLOR_TEXT+appWidgetId, color_id);
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);

        finish();
    }
}
