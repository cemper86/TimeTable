package ru.stairenx.nvsutimetable.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetServiceList extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyFactory(getApplicationContext(), intent);
    }
}
