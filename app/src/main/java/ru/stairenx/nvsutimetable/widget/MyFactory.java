package ru.stairenx.nvsutimetable.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
import ru.stairenx.nvsutimetable.item.PairItem;

public class MyFactory implements RemoteViewsService.RemoteViewsFactory {

    private static DateTimeFormatter dateTimeFormatterServer = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    private static CalendarDay day = CalendarDay.today();
    private static String nowDay;

    public static List<PairItem> data = new ArrayList<>();
    Context context;
    int widgetID;

    public MyFactory(Context context, Intent intent) {
        this.context = context;
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        nowDay = day.getDate().format(dateTimeFormatterServer);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void onDataSetChanged() {
        data.clear();
        DatabaseAction.setContext(context);
        data = WidgetWebAction.getJustTimeTable(DatabaseAction.getUserGroup(),nowDay);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.item_pair_for_widget);
        rView.setTextViewText(R.id.time,data.get(position).getTIME());
        rView.setTextViewText(R.id.discipline,data.get(position).getDISCIPLINE());
        return rView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
