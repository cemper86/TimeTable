package ru.stairenx.nvsutimetable.server;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.stairenx.nvsutimetable.ConstantsNVSU;

import static ru.stairenx.nvsutimetable.server.LinkAPI.ACTION;

/**
 * Created by viergo on 05.06.16.
 */
public class ConnectServer {
    private static HttpURLConnection httpURLConnection = null;
    private static BufferedReader reader = null;
    private static String resultJson = "";

    public static String getJSON(String textUrl){
        try{
            URL url = new URL(textUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(7500);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!=null){
                buffer.append(line);
            }
            resultJson = buffer.toString();
        }catch (Exception e){
            Log.e("-----","отсвуствует интернет подключение!");
            return "404";
        }
        finally {
            httpURLConnection.disconnect();
        }
        return resultJson;
    }

    public static void sendStatistic(String group, String dataTimeTable, String key){
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String response  = "";
        String link = LinkAPI.SEND_STATISTIC + ACTION
                + LinkAPI.AND +  LinkAPI.USER_KEY + key
                + LinkAPI.AND + LinkAPI.GROUP + group
                + LinkAPI.AND + LinkAPI.DATE + dataTimeTable;
        try{
            Log.d("-----","Запрос на STAIRENX API "+ link);
            URL url = new URL(link);
            conn = (HttpURLConnection)  url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5500);
            conn.connect();

            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!=null){
                buffer.append(line);
            }
            response = buffer.toString();
            JSONObject obj = new JSONObject(response);
            switch (obj.optString("response")){
                case "error" :
                    Log.e("Status sending ^^^","Ошибка на сервере");
                    break;
                case "true" :
                    Log.e("Status sending ^^^", "Отправка успешна");
                    break;
                case "false" :
                    Log.e("Status sending ^^^","Ошибка, ответ false");
                    break;
            }
        }catch (Exception e){
            e.getStackTrace();
            Log.e("-----","API не работает");

        }
        finally {
            conn.disconnect();
        }
    }

}
