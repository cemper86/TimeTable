package ru.stairenx.nvsutimetable.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        day = CalendarDay.today();
        today = day.getDate().format(dateTimeFormatter);
        widgetTitile = "Ceгодня, " + today;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_table_widget);
        views.setTextViewText(R.id.appwidget_text, widgetTitile);

        Intent adapter = new Intent(context, WidgetServiceList.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.rv_pair, adapter);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        day = CalendarDay.today();
        today = day.getDate().format(dateTimeFormatter);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

