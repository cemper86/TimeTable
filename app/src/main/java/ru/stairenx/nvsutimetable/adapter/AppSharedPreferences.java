package ru.stairenx.nvsutimetable.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppSharedPreferences {

    private static SharedPreferences sPref;
    private static final String PREF_NAME = "NVSU_DATA";

    public static class Group{

        private static final String GROUP = "tt_group";

        public static boolean saveGroup(Context context, String strGroup){
            sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(GROUP,strGroup);
            Log.d("---","Used Preferences");
            if (editor.commit())return true;
            else return false;
        }
        public static String loadGroup(Context context){
            sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
            Log.d("---","Used Preferences");
            return sPref.getString(GROUP,null);
        }

    }

    public static class Subgroup{

        private static final String SUBGROUP = "tt_subgroup";

        public static boolean saveSubgroup(Context context, String strSubgroup){
            sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(SUBGROUP,strSubgroup);
            Log.d("---","Used Preferences");
            if (editor.commit())return true;
            else return false;
        }

        public static String loadSubgroup(Context context){
            sPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
            Log.d("---","Used Preferences");
            return sPref.getString(SUBGROUP,null);
        }

    }
}
