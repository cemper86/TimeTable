package ru.stairenx.nvsutimetable.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("---","Сработал WidgetService");
        return new MyFactory(getApplicationContext(),intent);
    }
}
