package com.zhr.cat.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TextInfoSQLiteOpenHelper extends SQLiteOpenHelper {

	public TextInfoSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table textinfo (time date primary key ,content varchar(20),type integer)");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}