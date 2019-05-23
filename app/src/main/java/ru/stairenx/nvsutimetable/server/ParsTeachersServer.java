package ru.stairenx.nvsutimetable.server;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.stairenx.nvsutimetable.activity.MainActivity;
import ru.stairenx.nvsutimetable.database.DatabaseAction;
import ru.stairenx.nvsutimetable.database.TeachersTable;
import static ru.stairenx.nvsutimetable.activity.MainActivity.updateDateNamesTeachers;

public class ParsTeachersServer {

    private Document HTMLSource;
    private static List<TeachersTable> listTeachers = new ArrayList<TeachersTable>();

    public class getInfoOfTeachersFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... strings) {
            try {
                HTMLSource = Jsoup.connect(LinkAPI.TeachersURL).get();
                Log.e("------------------", "HTML успешно загруженно");
            } catch (Exception e) {
                Log.e("------------------", "Ошибка в загрузке HTML");
                return null;
            }
            Elements element = HTMLSource.select("table.table.gradient-table");
            element = element.select("td");
            listTeachers.clear();
            for (int i = 1; i != element.size(); i++) {
                if (!element.get(i).text().isEmpty())
                    listTeachers.add(new TeachersTable(getNameTeacherFromSource(element.get(i).text()), getStaffTeacherFromSource(element.get(i).text())));
            }
            DatabaseAction.addTeacherCollection(listTeachers);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateDateNamesTeachers();
        }

        private String getNameTeacherFromSource(String source) {
            source = source.substring(0, source.lastIndexOf("(") - 1);
            return source;
        }

        private String getStaffTeacherFromSource(String source) {
            source = source.substring(source.lastIndexOf("("),source.lastIndexOf(")")+1);
            return source;
        }
    }

    public void getTeachers() {
        new getInfoOfTeachersFromServer().execute();
    }
}
