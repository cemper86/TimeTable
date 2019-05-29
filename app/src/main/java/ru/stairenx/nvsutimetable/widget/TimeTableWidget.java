package ru.stairenx.nvsutimetable.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.format.DateTimeFormatter;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.database.DatabaseAction;

/**
 * Implementation of App Widget functionality.
 */
public class TimeTableWidget extends AppWidgetProvider {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EE dd");
    private static DateTimeFormatter dateTimeFormatterServer = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private static CalendarDay day;
    private static String today;
    private static CharSequence widgetTitile;

    public static void update(){

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        day = CalendarDay.today();
        today = day.getDate().format(dateTimeFormatter);
        widgetTitile = "Ceгодня, " + today;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_table_widget);
        views.setTextViewText(R.id.appwidget_text, widgetTitile);
        Intent adapter = new Intent(context, WidgetServiceList.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.rv_pair, adapter);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        day = CalendarDay.today();
        today = day.getDate().format(dateTimeFormatter);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("----"," вызов метода Receive");
        if(intent.getAction().equals("update_widget")) {
            Log.d("-----","Обновление Виджета");
            day = CalendarDay.today();
            today = day.getDate().format(dateTimeFormatter);
            widgetTitile = "Ceгодня, " + today;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context, TimeTableWidget.class);
            int appWidgetId[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_table_widget);
            views.setTextViewText(R.id.appwidget_text, widgetTitile);
            Intent adapter = new Intent(context, WidgetServiceList.class);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.rv_pair);

            adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.rv_pair, adapter);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

