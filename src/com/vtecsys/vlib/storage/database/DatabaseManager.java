package com.vtecsys.vlib.storage.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.vtecsys.vlib.model.Book;

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
	
	public List<Book> getBookmarks(String memberId) {
		if (TextUtils.isEmpty(memberId)) {
			return null;
		}
		
		List<Book> bookmarks = new ArrayList<Book>();
		try {
			String selection = DatabaseHelper.KEY_MEMBER_ID + "='" + memberId + "'";
			Cursor c = db.query(DatabaseHelper.TABLE_BOOKMARKS, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				do {
					bookmarks.add(new Book(
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_RID)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_ISBN)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_AUTHOR)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLICATION)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_CALL_NUMBER)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_BOOK_COVER)),
						c.getString(c.getColumnIndex(DatabaseHelper.KEY_EDITION))
					));
				} while (c.moveToNext());
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookmarks;
	}
	
	public boolean isBookmarked(String memberId, Book book) {
		if (TextUtils.isEmpty(memberId) || book == null) {
			return false;
		}
		boolean result = false;
		try {
			String selection = DatabaseHelper.KEY_MEMBER_ID + "='" + memberId + "' AND " + DatabaseHelper.KEY_RID + "='" + book.getRID() + "'";
			Cursor c = db.query(DatabaseHelper.TABLE_BOOKMARKS, null, 
				selection, null, null, null, null);
			if (c != null && c.moveToFirst()) {
				result = true;
			}
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void addBookmark(String memberId, Book book) {
		if (TextUtils.isEmpty(memberId) || book == null) {
			return;
		}	
		if (!isBookmarked(memberId, book)) {
			ContentValues values = prepareBookContentValues(memberId, book);
			db.insert(DatabaseHelper.TABLE_BOOKMARKS, null, values);
		}
	}
	
	public void removeBookmark(String memberId, Book book) {
		if (TextUtils.isEmpty(memberId) || book == null) {
			return;
		}
		String whereClause = DatabaseHelper.KEY_MEMBER_ID + "='" + memberId + "' AND " +
			DatabaseHelper.KEY_RID + "='" + book.getRID() + "'";
		db.delete(DatabaseHelper.TABLE_BOOKMARKS, whereClause, null);
	}
	
	public void removeAllBookmarks(String memberId) {
		if (TextUtils.isEmpty(memberId)) {
			return;
		}
		String whereClause = DatabaseHelper.KEY_MEMBER_ID + "='" + memberId + "'";
		db.delete(DatabaseHelper.TABLE_BOOKMARKS, whereClause, null);
	}
	
	private ContentValues prepareBookContentValues(String memberId, Book book) {
		if (TextUtils.isEmpty(memberId) || book == null) {
			return null;
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBER_ID, memberId);
		values.put(DatabaseHelper.KEY_RID, book.getRID());
		values.put(DatabaseHelper.KEY_ISBN, book.getISBN());
		values.put(DatabaseHelper.KEY_TITLE, book.getTitle());
		values.put(DatabaseHelper.KEY_AUTHOR, book.getAuthor());
		values.put(DatabaseHelper.KEY_PUBLICATION, book.getPublication());
		values.put(DatabaseHelper.KEY_CALL_NUMBER, book.getCallNumber());
		values.put(DatabaseHelper.KEY_BOOK_COVER, book.getBookCover());
		values.put(DatabaseHelper.KEY_EDITION, book.getEdition());
		return values;
	}
}
