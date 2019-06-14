package ru.stairenx.nvsutimetable.widget;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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

    private static List<PairItem> taskData = new ArrayList<>();

    public static class GetTimeTableTask extends AsyncTask<String, Integer, List<PairItem>>{
        @Override
        protected void onPostExecute(List<PairItem> s) {
            super.onPostExecute(s);
            MyFactory.data = taskData;

        }

        @Override
        protected List<PairItem> doInBackground(String... strings) {
            return  getJustTimeTable(strings[0],strings[1]);
        }
    }

    public static List<PairItem> getJustTimeTable(String group, String date){
        taskData.clear();
        String object = getObject(group,date);
        List<String> array = WebAction.token(object);
        int a = 0;
        for (int i = 0; i < array.size(); i++) {
            String json = array.get(a);
            try {
                JSONObject obj = new JSONObject(json);
                addPairOfList(obj);
                a++;
            } catch (JSONException e) {
                e.getStackTrace();
            }
        }
        if(taskData.size()==0){
            taskData.add(new PairItem("","","","Занятий нет","","","","","",true));
        }
        //MyFactory.data = taskData;
        return taskData;
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

    private static void addPairOfList(JSONObject obj){
        String group, pair, time, discipline, type, aud, subgroup, teacher, korp;
        String linkSubgroup = obj.optString(ConstantsJson.OBJ_SUBGRUP);

        group = obj.optString(ConstantsJson.OBJ_GRUP);
        pair = obj.optString(ConstantsJson.OBJ_PAIR);
        time = WebAction.getTime(obj.optString(ConstantsJson.OBJ_PAIR));
        discipline = obj.optString(ConstantsJson.OBJ_DISCIPLINE);
        type = WebAction.typePair(obj.optString(ConstantsJson.OBJ_VID));
        aud = obj.optString(ConstantsJson.OBJ_AUD);
        subgroup = linkSubgroup;
        teacher = obj.optString(ConstantsJson.OBJ_TEACHER);
        korp = obj.optString(ConstantsJson.OBJ_KORP);

        PairItem test = new PairItem(group, pair, time, discipline, type, aud, subgroup, teacher, korp, true);

        if (taskData.isEmpty()) {
            taskData.add(test);
        } else {
            boolean isFound = false;
            for (int i = 0; i < taskData.size(); i++)
                if (pair.equals(taskData.get(i).getPAIR())) {
                    isFound = true;
                    break;
                }
            if (!isFound) {
                taskData.add(test);
            }else
                return;
        }
    }
}
