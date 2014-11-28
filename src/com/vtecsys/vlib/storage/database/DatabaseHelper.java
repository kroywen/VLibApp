package com.vtecsys.vlib.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "VLibApp";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_WORDS = "Words";
	public static final String TABLE_BOOKMARKS = "Bookmarks";
	
	public static final String KEY_ID = "id";
	public static final String KEY_WORD = "word";
	public static final String KEY_MEMBER_ID = "member_id";
	public static final String KEY_RID = "rid";
	public static final String KEY_ISBN = "isbn";
	public static final String KEY_TITLE = "title";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_PUBLICATION = "publication";
	public static final String KEY_CALL_NUMBER = "call_number";
	public static final String KEY_BOOK_COVER = "book_cover";
	public static final String KEY_EDITION = "edition";
	
	
	public static final String CREATE_TABLE_WORDS = 
		"create table if not exists " + TABLE_WORDS + " (" +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_WORD + " text);";
	
	public static final String CREATE_TABLE_BOOKMARKS = 
		"create table if not exists " + TABLE_BOOKMARKS + " (" +
		KEY_ID + " integer primary key autoincrement, " +
		KEY_MEMBER_ID + " text not null, " + 
		KEY_RID + " text not null, " +
		KEY_ISBN + " text, " +
		KEY_TITLE + " text, " +
		KEY_AUTHOR + " text, " +
		KEY_PUBLICATION + " text, " +
		KEY_CALL_NUMBER + " text, " +
		KEY_BOOK_COVER + " text, " +
		KEY_EDITION + " text);";
	
	public static final String DROP_TABLE_WORDS = 
		"drop table if exists " + TABLE_WORDS + ";";
	
	public static final String DROP_TABLE_BOOKMARKS = 
		"drop table if exists " + TABLE_BOOKMARKS;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_WORDS);
		db.execSQL(CREATE_TABLE_BOOKMARKS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_WORDS);
		db.execSQL(DROP_TABLE_BOOKMARKS);
		onCreate(db);
	}

}
