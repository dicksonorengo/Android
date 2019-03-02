package com.example.notes3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static com.example.notes3.Main2Activity.COLOR_TEXT;
import static com.example.notes3.Main2Activity.KEY_BUTTON_TEXT;
import static com.example.notes3.Main2Activity.SHARED_PREF;

public class NoteAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
            String text = prefs.getString(KEY_BUTTON_TEXT+appWidgetId, "No Note");
            int color_id = prefs.getInt(COLOR_TEXT+appWidgetId, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            remoteViews.setOnClickPendingIntent(R.id.imageView12, pendingIntent);
            remoteViews.setCharSequence(R.id.appwidget_text__, "setText", text);
            remoteViews.setInt(R.id.appwidget_text__, "setBackgroundColor",
                    context.getResources().getColor(MainActivity.colors[color_id]));

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
