package com.harshal.cuelogicandroidtest.manager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.harshal.cuelogicandroidtest.database.DatabaseOpenHelper;
import com.harshal.cuelogicandroidtest.database._CartItemsDB._Product;

public class DatabaseManager {
	private static DatabaseOpenHelper DBHelper;
	private SQLiteDatabase db;
	private Context context;

	private static DatabaseManager databaseManager = null;

	protected DatabaseManager(Context ctx) {
		if (DBHelper == null)
			DBHelper = new DatabaseOpenHelper(ctx);
		this.context = ctx;
	}

	public static DatabaseManager getInstance(Context ctx) {
		if (databaseManager == null) {
			databaseManager = new DatabaseManager(ctx);
		}

		return databaseManager;
	}

	protected SQLiteDatabase open() throws SQLException {
		if (DBHelper != null)
			db = DBHelper.getWritableDatabase();
		else {
			DBHelper = new DatabaseOpenHelper(context);
			db = DBHelper.getWritableDatabase();
		}
		return db;
	}

	@SuppressWarnings("unused")
	private void close() {
		DBHelper.close();
	}

	public void clearDatabase() {
		open();
		db.delete(_Product.TABLE, null, null);
	}

}