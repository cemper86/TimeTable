package ru.stairenx.nvsutimetable.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.item.PairItem;

public class MyFactory implements RemoteViewsService.RemoteViewsFactory {

    List<PairItem> data = new ArrayList<>();
    Context context;
    int widgetID;

    public MyFactory(Context context, Intent intent) {
        this.context = context;
        data = ConstantsNVSU.getTestPair();
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        //data = WidgetWebAction.getJustTimeTable(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void onDataSetChanged() {
        //data.notifyAll();
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
