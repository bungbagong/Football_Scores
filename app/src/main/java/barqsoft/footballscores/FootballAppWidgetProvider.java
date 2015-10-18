package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.service.myFetchService;


public class FootballAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;


        //Log.d("bungbagong", "app widget onUpdate()");

        Intent service_start = new Intent(context, myFetchService.class);
        context.startService(service_start);

        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), FootballAppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        if (intent.getAction().equalsIgnoreCase("barqsoft.footballscores.UPDATE_INTENT")) {

            //Log.d("bungbagong", "app widget onReceive()");
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                //onUpdate(context, appWidgetManager, appWidgetIds);

                final int N = appWidgetIds.length;
                for (int i = 0; i < N; i++) {
                    updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
                }

            }
        } else if (intent.getAction().equalsIgnoreCase("android.appwidget.action.APPWIDGET_UPDATE")) {

            if (appWidgetIds != null && appWidgetIds.length > 0) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
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

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.football_app_widget_provider);
        views.setOnClickPendingIntent(R.id.goButton, pendingIntent);

        String[] projection = new String[]{"home", "away", "time", "home_goals", "away_goals"};

        String[] dateString = new String[2];
        Date widgetDate = new Date(System.currentTimeMillis() + ((0) * 86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        dateString[0] = mformat.format(widgetDate);
        dateString[1] = "-1";

        String selection = "date = ? AND home_goals != ?";

        Cursor lastGameCursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreForWidget(),
                projection, selection, dateString, " time DESC");

        //Log.d("bungbagong",String.valueOf(lastGameCursor.getCount()));


        if (lastGameCursor.moveToFirst()) {
            views.setTextViewText(R.id.appwidget_text, lastGameCursor.getString(0));
            views.setTextViewText(R.id.appwidget_text_away, lastGameCursor.getString(1));
            //views.setTextViewText(R.id.appwidget_text_date,lastGameCursor.getString(2));
            views.setTextViewText(R.id.appwidget_text_score, Utilies.getScores(lastGameCursor.getInt(3), lastGameCursor.getInt(4)));
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);


    }
}

