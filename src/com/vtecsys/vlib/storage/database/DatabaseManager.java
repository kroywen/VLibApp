package com.vtecsys.vlib.storage.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DatabaseManager {
	
	private SQLiteDatabase db;
	
	private DatabaseManager(Context context) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public static DatabaseManager newInstance(Context context) {
		return new DatabaseManager(context);
	}
	
	public List<String> getWords(String word) {
		List<String> words = new ArrayList<String>();
		try {
			String selection = DatabaseHelper.KEY_WORD + " like '" + word + "%'";
			Cursor c = db.query(DatabaseHelper.TABLE_WORDS, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				do {
					words.add(c.getString(c.getColumnIndex(DatabaseHelper.KEY_WORD)));
				} while (c.moveToNext());
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return words;
	}
	
	public void saveWord(String word) {
		if (TextUtils.isEmpty(word)) {
			return;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_WORD, word);
		db.insert(DatabaseHelper.TABLE_WORDS, null, values);
	}
	
	public boolean hasWord(String word) {
		String sql = "select count(*) from " + DatabaseHelper.TABLE_WORDS +
			" where " + DatabaseHelper.KEY_WORD + "='" + word + "'";
		Cursor c = db.rawQuery(sql, null);
		boolean found = false;
		if (c != null && c.moveToFirst()) {
			found = c.getInt(0) > 0;
		}
		if (c != null && !c.isClosed()) {
			c.close();
		}
		return found;
	}

}
