package ru.stairenx.nvsutimetable.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.adapter.AppSharedPreferences;
import ru.stairenx.nvsutimetable.database.DatabaseAction;

/**
 * Implementation of App Widget functionality.
 */
public class TimeTableWidget extends AppWidgetProvider {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EE dd");
    private static DateTimeFormatter dateTimeFormatterTime = DateTimeFormatter.ofPattern("HH:mm");
    private static CalendarDay day;
    private static String today;
    private static String justTime;
    private static CharSequence widgetTitile;
    private static String strUpdateTime;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        onUpdateWidgetTimeTable(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        onUpdateWidgetTimeTable(context);
        }

    public static void onUpdateWidgetTimeTable(Context context){
        Log.d("---","Метод для обновления виджета");
        day = CalendarDay.today();
        today = day.getDate().format(dateTimeFormatter);
        justTime = ZonedDateTime.now().format(dateTimeFormatterTime);
        widgetTitile = "Ceгодня, " + today;
        strUpdateTime = justTime;
        String group = AppSharedPreferences.Group.loadGroup(context);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetId[] = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(),TimeTableWidget.class.getName()));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_table_widget);
        views.setTextViewText(R.id.appwidget_text, widgetTitile);
        Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.rv_pair, adapter);
        views.setTextViewText(R.id.tv_widget_group, group);
        views.setTextViewText(R.id.tv_time_update, strUpdateTime);
        appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(),TimeTableWidget.class.getName()),views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.rv_pair);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

