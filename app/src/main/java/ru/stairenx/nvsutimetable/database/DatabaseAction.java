package ru.stairenx.nvsutimetable.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.UnicodeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.stairenx.nvsutimetable.ConstantsNVSU;
import ru.stairenx.nvsutimetable.item.UserItem;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class DatabaseAction {

    private static SQLiteDatabase sql;
    private static Context context;

    public static void addUser(String faculty, String mGroup){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        UserTable user = new UserTable(faculty, mGroup);
        long id = cupboard().withDatabase(sql).put(user);
        Log.d("===========", "id its: "+id);
    }

    public static void addUser(String faculty, String group, String subgroup){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        UserTable user = new UserTable(faculty, group, subgroup);
        cupboard().withDatabase(sql).put(user);
    }

    public static void changeUserGroup(String mGroup){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        UserTable user = (UserTable) cupboard().withDatabase(sql).get(UserTable.class, 1L);
        user.myGroup=mGroup;
        cupboard().withDatabase(sql).put(user);
    }

    public static void changeUserSubGroup(String subGroup){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        UserTable user = (UserTable) cupboard().withDatabase(sql).get(UserTable.class, 1L);
        user.mySubGroup=subGroup;
        cupboard().withDatabase(sql).put(user);
    }

    public static String getUserGroup(){
        String group = new String();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();

        String table = getNameTable(UserTable.class);
        if (getCountTable(table)){
            String textQuery = "SELECT * FROM " + table + " ORDER BY " + DatabaseConstants.COLUMN_ID + " DESC";
            Cursor cursor = sql.rawQuery(textQuery, null);
            while (cursor.moveToNext()) {
                group = cursor.getString(cursor.getColumnIndex("myGroup"));
            }
            cursor.close();
        }else{
            group = ConstantsNVSU.NONE;
        }
        sql.close();

        return group;
    }

    public static boolean getCountTable(String table){
        String textQuery = "SELECT * FROM " + table;
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery(textQuery, null);
        int q = cursor.getCount();
        if(q>0){
            return true;
        }
        else{
            return false;
        }
    }

    public static List<UserItem> getUserInfo(){
        List<UserItem> user = new ArrayList<>();

        return user;
    }

    public static String getNameTable(Class table){
        String nameTable = cupboard().getTable(table);
        return nameTable;
    }

    public static List<UserItem> getUserInformation(String group){
        int id;
        List<UserItem> info = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        String table = getNameTable(UserTable.class);
        String textQuery = "SELECT * FROM "+table+" WHERE myGroup='"+group+"';";
        Cursor cursor = sql.rawQuery(textQuery, null);
        cursor.moveToFirst();
        id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
        cursor.close();
        Long idL = Long.valueOf(id);
        UserTable item = cupboard().withDatabase(sql).get(UserTable.class, idL);
        info.add(new UserItem(item.faculty, item.myGroup, item.mySubGroup));
        return info;
    }

    public static void deleteUser(int id){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        Long idL = Long.valueOf(id);
        cupboard().withDatabase(sql).delete(UserTable.class,idL);
    }

    public static String getUserSubgroup(){
        String subgroup = "";
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sql = dbHelper.getWritableDatabase();
        String table = getNameTable(UserTable.class);
        if (getCountTable(table)){
            String textQuery = "SELECT * FROM " + table + " ORDER BY " + DatabaseConstants.COLUMN_ID + " DESC";
            Cursor cursor = sql.rawQuery(textQuery, null);
            while (cursor.moveToNext()) {
                subgroup = cursor.getString(cursor.getColumnIndex("mySubGroup"));
            }
            cursor.close();
        }else{
            subgroup = ConstantsNVSU.NONE;
        }
        sql.close();

        return subgroup;
    }

    public static Context getContext(){
        return context;
    }

    public static void setContext(Context c){
        context = c;
    }

}
