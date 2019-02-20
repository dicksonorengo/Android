package com.example.notes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.notes.view.MainActivity;

import io.paperdb.Paper;


public class MyWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Paper.init(context);
        String title = Paper.book().read("title");
        //String time = Paper.book().read("time");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        views.setTextViewText(R.id.appwidget_text, title);
        //views.setTextViewText(R.id.timestamp, time);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            views.setOnClickPendingIntent(R.id.press, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            updateAppWidget(context,appWidgetManager,appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

