package ru.stairenx.nvsutimetable.database;

import android.os.AsyncTask;

public class DatabaseActionTask {
    public static class changeUserGroupAndSubGroupTask extends AsyncTask <String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            DatabaseAction.changeUserGroupAndSubGroup(strings[0],strings[1]);
            return null;
        }
    }
}
