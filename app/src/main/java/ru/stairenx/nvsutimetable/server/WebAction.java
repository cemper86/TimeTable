package ru.stairenx.nvsutimetable.server;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ru.stairenx.nvsutimetable.activity.MainActivity;
import ru.stairenx.nvsutimetable.item.PairItem;

/**
 * Created by viergo on 28.01.17.
 */
public class WebAction {

    public static class getBook extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            /*String gr = new String();
            for(String p : params){
                gr = p;
            }*/
            MainActivity.data = new ArrayList<>();
            String result_json = getObject(params[0], params[1]);
           return result_json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
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
        Log.d("-----",link);
        List<String> jsonArray = token(ConnectServer.getJSON(link));
        int a = 0;
        //System.out.println("Размер массива: "+jsonArray.size());
        for(int i=0;i<jsonArray.size();i++){
            String json = jsonArray.get(a);
            try{
                JSONObject obj = new JSONObject(json);
                MainActivity.data.add(new PairItem(
                        obj.optString(ConstantsJson.OBJ_GRUP),
                        obj.optString(ConstantsJson.OBJ_PAIR),
                        getTime(obj.optString(ConstantsJson.OBJ_PAIR)),
                        obj.optString(ConstantsJson.OBJ_DISCIPLINE),
                        typePair(obj.optString(ConstantsJson.OBJ_VID)),
                        obj.optString(ConstantsJson.OBJ_AUD),
                        obj.optString(ConstantsJson.OBJ_POTOK),
                        obj.optString(ConstantsJson.OBJ_TEACHER)
                ));
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
                time = "8:30 10:00";
                break;
            case 2 :
                time = "10:10 11:40";
                break;
            case 3 :
                time = "12:10 13:40";
                break;
            case 4 :
                time = "13:50 15:20";
                break;
            case 5 :
                time = "15:30 17:00";
                break;
            case 6 :
                time = "17:10 18:40";
                break;
            case 7 :
                time = "18:50 20:20";
                break;
        }
        return time;
    }
}
