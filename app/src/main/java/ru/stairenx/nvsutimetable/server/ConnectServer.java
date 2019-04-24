package ru.stairenx.nvsutimetable.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
}
