package ru.stairenx.nvsutimetable.server;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.activity.MainActivity;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
import ru.stairenx.nvsutimetable.item.PairItem;

/**
 * Created by viergo on 28.01.17.
 */
public class WebAction {

    public static class getTimeTable extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.RecyclerView.setVisibility(View.GONE);
            MainActivity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity.progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            MainActivity.data = new ArrayList<>();
           return getObject(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            MainActivity.progressBar.setVisibility(View.GONE);
            MainActivity.RecyclerView.setVisibility(View.VISIBLE);
            MainActivity.update();
        }
    }

    public static List<String> token(String json){
        List<String> array = new ArrayList<>();
        String jsonObject = "";
        StringTokenizer st = new StringTokenizer(json,"[]");
        while (st.hasMoreTokens()){
            jsonObject = st.nextToken();
            //System.out.println(jsonObject);
        }
        StringTokenizer ar = new StringTokenizer(jsonObject,"}");
        while (ar.hasMoreTokens()) {
            char[] chArray = ar.nextToken().toCharArray();
            if(chArray[0] ==','){
                chArray[0] = ' ';
            }
            array.add(String.valueOf(chArray)+"}");
        }
        return array;
    }

    private static String getObject(String group, String date){
        String result = "";
        String link = LinkAPI.URL+LinkAPI.GROUP + group + LinkAPI.AND + LinkAPI.DATE + date;
        Log.d("-----","Запрос на "+link);
        List<String> jsonArray = token(ConnectServer.getJSON(link));
        int a = 0;
        //System.out.println("Размер массива: "+jsonArray.size());
        for(int i=0;i<jsonArray.size();i++){
            String json = jsonArray.get(a);
            try{
                JSONObject obj = new JSONObject(json);
                addPairOfList(obj);
                a++;
            }catch (JSONException e){
                e.getStackTrace();
            }
        }
        return result;
    }

    public static boolean isNullKey(String json,String column){
        boolean result = false;
        try {
            JSONObject jo = new JSONObject(json);
            result = jo.isNull(column);
        }catch (JSONException e){
            e.getMessage();
        }
        Log.d("$$$**$$$",column+" "+String.valueOf(result));
        return result;
    }

    private static String typePair(String typePair){
    String type = "";
    switch (typePair){
        case "Лб" :
            type = "Лабораторная";
            break;
        case "Пр" :
            type = "Практика";
            break;
        case "Лек" :
            type = "Лекция";
            break;
        case "Эк" :
            type = "Экзамен";
            break;
    }
    return type;
    }

    private static String getTime(String pair){
        String time = "";
        switch (Integer.parseInt(pair)){
            case 1 :
                time = "8:30\n10:00";
                break;
            case 2 :
                time = "10:10\n11:40";
                break;
            case 3 :
                time = "12:10\n13:40";
                break;
            case 4 :
                time = "13:50\n15:20";
                break;
            case 5 :
                time = "15:30\n17:00";
                break;
            case 6 :
                time = "17:10\n18:40";
                break;
            case 7 :
                time = "18:50\n20:20";
                break;
        }
        return time;
    }

    private static String getSubgroup(String potok){
        String subgroup;
        String[] data = potok.split("/");
        if(data.length>1) {
            subgroup = data[1];
        }else{
            subgroup = "0";
        }
        return subgroup;
    }

    private static boolean checkFacultyIT(String group){
        boolean check = false;
        int q = 0;
        for(int i = 0;i<ConstantsNVSU.ARRAY_FACULTET_IT.length-1;i++) {
            if(group.equals(ConstantsNVSU.ARRAY_FACULTET_IT[i])){
                q = 1;
            }
        }
        if(q==1){
            check = true;
        }
        return check;
    }

    private static void addPairOfList(JSONObject obj){
        String group,pair,time,discipline,type,aud,subgroup,teacher,korp;
        String dbSubgroup = MainActivity.subGroup;
        String linkSubgroup = obj.optString(ConstantsJson.OBJ_SUBGRUP);
        if(linkSubgroup.equals("null")){
            linkSubgroup = getSubgroup(obj.optString(ConstantsJson.OBJ_POTOK));
        }

        if(dbSubgroup.equals(linkSubgroup) || dbSubgroup.equals("0") || linkSubgroup.equals("0")){
            group = obj.optString(ConstantsJson.OBJ_GRUP);
            pair = obj.optString(ConstantsJson.OBJ_PAIR);
            time = getTime(obj.optString(ConstantsJson.OBJ_PAIR));
            discipline = obj.optString(ConstantsJson.OBJ_DISCIPLINE);
            type = typePair(obj.optString(ConstantsJson.OBJ_VID));
            aud = obj.optString(ConstantsJson.OBJ_AUD);
            subgroup = linkSubgroup;
            teacher = obj.optString(ConstantsJson.OBJ_TEACHER);
            korp = obj.optString(ConstantsJson.OBJ_KORP);

            PairItem item = new PairItem(group,pair,time,discipline,type,aud,subgroup,teacher,korp);
            MainActivity.data.add(item);
            Log.d("-----","Фильтрация по подгруппам и общие пары");
        }else{
            Log.d("-----","Подгруппа не подходит");
        }
    }
}