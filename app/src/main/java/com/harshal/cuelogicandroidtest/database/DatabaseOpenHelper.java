package com.harshal.cuelogicandroidtest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.harshal.cuelogicandroidtest.database._CartItemsDB._Product;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String DATABASE_NAME = "CuelogicAndroidTest.sqlite";
    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(_Product.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:

        }
    }

    public void deleteAllTablesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(_Product.TABLE, null, null);
    }

}
