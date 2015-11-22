package com.gdtel.eshore.androidframework.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  youjw
 * @version  [版本号, 2013-10-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DataBaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String THIS_FILE = "DatabaseHelper";
	private boolean opened = false;
	private SQLiteDatabase mSQLiteDatabase ;
	public final static String MESSAGE_TABLE_NAME = "message";
	private final static String TABLE_MESSAGE_CREATE = "CREATE TABLE IF NOT EXISTS " + MESSAGE_TABLE_NAME +"" +
			"( id INTEGER PRIMARY KEY AUTOINCREMENT,uid INTEGER,uName TEXT,toId INTEGER,toName TEXT,groupId INTEGER," +
			" groupName TEXT,type INTEGER, content_type INTEGER,content TEXT, receiver INTEGER, isread INTEGER DEFAULT 0,visiable INTEGER DEFAULT 0," +
			" send_state INTEGER DEFAULT 1, ctime TEXT );";
//			+ " (" " id INTEGER PRIMARY KEY AUTOINCREMENT," + Message.FIELD_UID + " INTEGER,"
//			+ Message.FIELD_UC_UID + " INTEGER," + Message.FIELD_TYPE + " INTEGER," + Message.FIELD_CONTENT_TYPE
//			+ " INTEGER," + Message.FIELD_CONTENT + " TEXT," + Message.FIELD_RECEIVER + " INTEGER,"
//			+ Message.FIELD_ISREAD + " INTEGER DEFAULT 0," + Message.FIELD_VISIABLE + " INTEGER DEFAULT 0,"
//			+ Message.FIELD_SEND_STATE + " INTEGER DEFAULT 1," + Message.FIELD_CTIME + " TEXT" + ");";
	public DataBaseHelper(Context context,String db_name) {
		super(context, db_name, null, DATABASE_VERSION);
		Log.d(THIS_FILE, "DatabaseHelper------");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(THIS_FILE, "onCreate------");
		db.execSQL(TABLE_MESSAGE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(THIS_FILE, "Upgrading database from version " + oldVersion + " to " + newVersion);
		/*
		 * if(oldVersion < 1) { db.execSQL(TABLE_CONTACTS_CREATE); }
		 */
		onCreate(db);
	}
	public synchronized SQLiteDatabase open(){
		if (!opened){
			mSQLiteDatabase = this.getWritableDatabase();
			opened = true;
		}
		return mSQLiteDatabase;
	}
	public synchronized void close() {
		this.close();
		opened = false;
	}

	public  synchronized boolean isOpen() {
		return opened;
	}

}
