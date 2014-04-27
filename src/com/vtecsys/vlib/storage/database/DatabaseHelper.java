package com.vtecsys.vlib.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "VLibApp";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_WORDS = "Words";
	
	public static final String KEY_ID = "id";
	public static final String KEY_WORD = "word";
	
	public static final String CREATE_TABLE_WORDS = 
		"create table if not exists " + TABLE_WORDS + " (" +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_WORD + " text);";
	
	public static final String DROP_TABLE_WORDS = 
		"drop table if exists " + TABLE_WORDS + ";";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_WORDS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_WORDS);
		onCreate(db);
	}

}
