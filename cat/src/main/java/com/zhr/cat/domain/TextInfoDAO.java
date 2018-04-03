package com.zhr.cat.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TextInfoDAO {
	private TextInfoSQLiteOpenHelper helper;

	public TextInfoDAO(Context context, String name) {
		helper = new TextInfoSQLiteOpenHelper(context, name, null, 1);
	}

	/**
	 * @param content
	 * @param type
	 * @param time
	 * @return ���һ����Ϣ
	 */
	public long add(String content, int type, long time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", content);
		values.put("type", type);
		values.put("time", time);
		long num = db.insert("textinfo", null, values);
		db.close();
		return num;
	}

	/**
	 * @param time
	 * @return ɾ����ʱ�����Ϣ
	 */
	public int delete(long time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int number = db.delete("textinfo", "time=?", new String[] { Long.toString(time) });
		db.close();
		return number;
	}

	/**
	 * ��Ϣ��¼�������޸�
	 */
	public void update(String content, int type, long time) {

	}

	/**
	 * @param time
	 * @return ���ظ�ʱ�����Ϣ
	 */
	public TextInfo find(long time) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("textinfo", null, "time=?", new String[] { Long.toString(time) }, null, null, null);
		TextInfo textInfo = new TextInfo();
		if (cursor.moveToNext()) {
			textInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			textInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			textInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
		}
		db.close();
		return textInfo;
	}

	/**
	 * @param time
	 * @return ��������ʱ��timeǰ�����м�¼
	 */
	public List<TextInfo> findBeforeTime(long time) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("textinfo", null, "time>?", new String[] { Long.toString(time) }, null, null, null);
		List<TextInfo> textInfos = new ArrayList<TextInfo>();
	
		while (cursor.moveToNext()) {
			TextInfo textInfo = new TextInfo();
			textInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			textInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			textInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			textInfos.add(textInfo);
			textInfo = null;
		}
		db.close();
		return textInfos;
	}

	/**
	 * @param time
	 * @return ��������ʱ��timeǰ��һ����¼
	 */
	public TextInfo findOneBeforeTime(long time) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("textinfo", null, "time>?", new String[] { Long.toString(time) }, null, null, null);
		if (cursor.moveToNext()) {
			TextInfo textInfo = new TextInfo();
			textInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			textInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			textInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			db.close();
			return textInfo;
		} else {
			db.close();
			return null;
		}
	}

	/**
	 * @param time
	 * @return ���ظ���ʱ��time���������Ϣ
	 */
	public List<TextInfo> findAfterTime(long time) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("textinfo", null, "time>?", new String[] { Long.toString(time) }, null, null, null);
		List<TextInfo> textInfos = new ArrayList<TextInfo>();
		while (cursor.moveToNext()) {
			TextInfo textInfo = new TextInfo();
			textInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			textInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			textInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			textInfos.add(textInfo);
			textInfo = null;
		}
		db.close();
		return textInfos;
	}

	/**
	 * @param time
	 * @return ���ظ�ʱ����һ����¼
	 */
	public TextInfo findOneAfterTime(long time) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("textinfo", null, "time>?", new String[] { Long.toString(time) }, null, null, null);
		if (cursor.moveToNext()) {
			TextInfo textInfo = new TextInfo();
			textInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			textInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			textInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			db.close();
			return textInfo;
		} else {
			db.close();
			return null;
		}

	}

	/**
	 * @return ���ر������ݿ��е�������Ϣ��¼
	 */
	public List<TextInfo> findAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("textinfo", null, null, null, null, null, null);
		List<TextInfo> textInfos = new ArrayList<TextInfo>();
		while (cursor.moveToNext()) {
			TextInfo textInfo = new TextInfo();
			textInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			textInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			textInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			textInfos.add(textInfo);
			textInfo = null;
		}
		db.close();
		return textInfos;
	}
}
