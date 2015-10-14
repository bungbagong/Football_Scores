package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;


        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
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


/*
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_app_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
  */
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_app_widget_provider);
        views.setOnClickPendingIntent(R.id.goButton, pendingIntent);

        String[] projection = new String[] {"home", "away", "time","home_goals","away_goals"};

        Cursor lastGameCursor = context.getContentResolver().query(DatabaseContract.BASE_CONTENT_URI,
                projection,null,null,null);

        Log.d("bungbagong",String.valueOf(lastGameCursor.getCount()));
        Log.d("bungbagong",lastGameCursor.toString());

        if(lastGameCursor.moveToLast()){
            views.setTextViewText(R.id.appwidget_text,lastGameCursor.getString(0));
            views.setTextViewText(R.id.appwidget_text_away,lastGameCursor.getString(1));
            views.setTextViewText(R.id.appwidget_text_date,lastGameCursor.getString(2));
            views.setTextViewText(R.id.appwidget_text_score, Utilies.getScores(lastGameCursor.getInt(3), lastGameCursor.getInt(4)));
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);



    }
}

