package com.example.android.bakingtime.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.example.android.bakingtime.R;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UpdateWidgetService extends IntentService {


    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MyService","onHandleIntent starts");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingwidget_lv);
        for (int appWidgetId : appWidgetIds) {
            Log.i("MyService","onHandleIntent widget id "+appWidgetId);
            IngredientWidgetProvider.updateAppWidget(this,appWidgetManager,appWidgetId );
        }

        Log.i("MyService","onHandleIntent ends");
    }



}
