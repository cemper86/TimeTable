package ru.stairenx.nvsutimetable.widget;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.stairenx.nvsutimetable.item.PairItem;
import ru.stairenx.nvsutimetable.server.ConnectServer;
import ru.stairenx.nvsutimetable.server.ConstantsJson;
import ru.stairenx.nvsutimetable.server.LinkAPI;
import ru.stairenx.nvsutimetable.server.WebAction;

public class WidgetWebAction {

    public static class GetTimeTableTask extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            getJustTimeTable(strings[0],strings[1]);
            return null;
        }
    }

    public static void getJustTimeTable(String group, String date){
        String object = getObject(group,date);

        List<String> array = WebAction.token(object);
        int a = 0;
        for (int i = 0; i < array.size(); i++) {
            String json = array.get(a);
            try {
                JSONObject obj = new JSONObject(json);

                MyFactory.data.add(new PairItem(
                                    obj.optString(ConstantsJson.OBJ_GRUP),
                                    obj.optString(ConstantsJson.OBJ_PAIR),
                                    WebAction.getTime(obj.optString(ConstantsJson.OBJ_PAIR)),
                                    obj.optString(ConstantsJson.OBJ_DISCIPLINE),
                                    WebAction.typePair(obj.optString(ConstantsJson.OBJ_VID)),
                                    obj.optString(ConstantsJson.OBJ_AUD),
                                    obj.optString(ConstantsJson.OBJ_SUBGRUP),
                                    obj.optString(ConstantsJson.OBJ_TEACHER),
                                    obj.optString(ConstantsJson.OBJ_KORP),
                            true
                ));

                a++;
            } catch (JSONException e) {
                e.getStackTrace();
            }
        }
    }

    private static String getObject(String group, String date){
        String result = "";
        String link;
        if (group.length() > 4) {
            String teacher = Uri.encode(group);
            link = LinkAPI.URL + LinkAPI.LECTUR + teacher + LinkAPI.AND + LinkAPI.DATE + date;
        } else {
            link = LinkAPI.URL + LinkAPI.GROUP + group + LinkAPI.AND + LinkAPI.DATE + date;
        }
        result = ConnectServer.getJSON(link);
        return result;
    }
}
