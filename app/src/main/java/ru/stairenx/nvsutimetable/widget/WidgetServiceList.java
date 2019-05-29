package ru.stairenx.nvsutimetable.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetServiceList extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("-----","Запрос на MyFactory");
        return new MyFactory(getApplicationContext(), intent);
    }
}
