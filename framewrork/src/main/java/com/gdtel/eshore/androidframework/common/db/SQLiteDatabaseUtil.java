package com.gdtel.eshore.androidframework.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
public class SQLiteDatabaseUtil {

	private DataBaseHelper mOpenHelper;
	private Context context;
//	private MessageDao messageDao;
	public SQLiteDatabaseUtil(Context mContext , String db_name) {
		context = mContext;
		mOpenHelper = new DataBaseHelper(context,db_name);
//		messageDao = MessageDao.getInstance(mContext,db_name);
//		messageDao.setDbHelper(mOpenHelper);
	}
	/************ 对数据库的操作 ***********************/

	// 获取所有数据库的名称
	public String[] getDatabasesList() {

		return context.databaseList();
	}

	// 创建一个数据库
	public void createDatabase(String db) {
		context.openOrCreateDatabase(db, SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}
	// 删除一个数据库
	public void dropDatabase(String db) {
		try {
			context.deleteDatabase(db);
		} catch (SQLException e) {
		}
	}
	// 获取某个数据库的表的名称
	public String getTablesList(SQLiteDatabase mDb) {
		Cursor c = mDb.rawQuery("select name from sqlite_master where type='table' order by name",null);
		String str="";
		while (c.moveToNext()) {
			 str+=c.getString(0)+"\n";
			 
		}
		return "表的名称为:\n"+str;
	}
}
