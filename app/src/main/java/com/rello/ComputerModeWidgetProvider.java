package com.rello;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Developed by > Harsh Kumar Singh (snbApps)
 * UI/UX Designer > Harsh Kumar Singh (snbApps)
 * Date > 11/04/2021
 */

public class ComputerModeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, NameActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.computer_mode_widget);
            views.setOnClickPendingIntent(R.id.goto_computer_wdgets, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
