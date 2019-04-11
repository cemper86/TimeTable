package ru.stairenx.nvsutimetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATEBASE_NAME = "nvsu_timetable.db";
    private static final int DATEBASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATEBASE_NAME, null, DATEBASE_VERSION);
    }
    static {
        cupboard().register(UserTable.class);
        cupboard().register(TimetableTable.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }


}
