package com.android.notes.sample.manager;

import android.content.Context;

public class DbManager {

    private static final String DB_NAME = "getfiplys-db";

    private Context context;
//    private DaoSession masterSession;

    public DbManager(Context context) {
        this.context = context;
//        setupDb();
    }

    private void setupDb() {
//        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
//        SQLiteDatabase db = masterHelper.getWritableDatabase();
//        DaoMaster master = new DaoMaster(db);
//        masterSession = master.newSession();
    }

    public static void create(Context context) {
//        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
//        SQLiteDatabase db = masterHelper.getWritableDatabase();
//        DaoMaster.createAllTables(db, true);
    }

    public static void clear(Context context) {
//        try {
//            DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
//            SQLiteDatabase db = masterHelper.getWritableDatabase();
//            DaoMaster.dropAllTables(db, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
