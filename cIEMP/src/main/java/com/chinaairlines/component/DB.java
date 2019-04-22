package com.chinaairlines.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chinaairlines.ciemp.CIEMPApplication;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;
import android.util.Log;

public class DB {		
	
	public class DBHelper extends SQLiteOpenHelper {
		
		private static final String DBName = "EMP.db";
		private static final int DBVersion = 1;
		
		/** log */	
		private final String TBCreate_Log = 
				"CREATE TABLE IF NOT EXISTS " + Variable.TBName_Log + "( " +
				Variable.L_ERR_VERSION + " TEXT NOT NULL, " +
				Variable.L_ERR_FUNC + " TEXT NOT NULL, " +
				Variable.L_ERR_DESC + " TEXT," +
				Variable.L_ERR_TIME + " TEXT, " +
				Variable.L_LAST_UPDATED_DATE + " REAL NOT NULL " +				
				");";
		private final String TBDrop_Log = 
				"DROP TABLE IF EXISTS " + Variable.TBName_Log;
		private final String TBDrop_Message = 
				"DROP TABLE IF EXISTS " + Variable.TBName_Message;
		/** user_info */
		private final String TBCreate_UserInfo = 
				"CREATE TABLE IF NOT EXISTS " + Variable.TBName_UserInfo + "( " +
				Variable.U_TESTUSER + " TEXT, " +
				Variable.U_SHAREUSER + " TEXT, " +
				Variable.U_USER_NAME_TW + " TEXT, " +
				Variable.U_USER_NAME_US + " TEXT, " +
				Variable.U_USER_ID + " TEXT NOT NULL PRIMARY KEY," +
				Variable.U_USER_UNIT_TW + " TEXT, " +
				Variable.U_USER_UNIT_US + " TEXT, " +
				Variable.U_USER_LEAD_UT + " TEXT, " +
				Variable.U_USER_UNIT_CD + " TEXT, " +
				Variable.U_USER_TRD_UNT + " TEXT, " +
				Variable.U_USER_AUTH_LVF + " TEXT, " +
				Variable.U_USER_AUTH_LVS + " TEXT, " +
				Variable.U_USER_AUTH_SYSCD + " TEXT, " +
				Variable.U_USER_FIRSTNAME_EN + " TEXT, " +
				Variable.U_USER_LASTNAME_EN + " TEXT, " +
				Variable.U_USER_FIRSTNAME_TW + " TEXT, " +
				Variable.U_USER_LASTNAME_TW + " TEXT, " +
				Variable.U_USER_PHONE_NUMBER_1 + " TEXT, " +
				Variable.U_USER_PHONE_NUMBER_2 + " TEXT, " +
				Variable.U_USER_MOBILE_NUMBER_1 + " TEXT, " +
				Variable.U_USER_MOBILE_NUMBER_2 + " TEXT, " +
				Variable.U_USER_EMAIL_1 + " TEXT, " +
				Variable.U_USER_EMAIL_2 + " TEXT, " +
				Variable.U_REGISTRANTION_TIME + " REAL NOT NULL, " +
				Variable.U_LAST_UPDATED_DATE + " REAL NOT NULL, " +
				Variable.U_USER_STATUS + " TEXT NOT NULL, " +
				Variable.U_DEVID + " TEXT NOT NULL " +
				");";
		private final String TBCreate_Message = 
				"CREATE TABLE IF NOT EXISTS " + Variable.TBName_Message + "( " +
				Variable.M_MSG_ID + " TEXT NOT NULL PRIMARY KEY," +
				Variable.M_MSG_ORDER + " TEXT, " +
				Variable.M_MSG_STATUS + " TEXT, " +
				Variable.M_MSG_TIME + " TEXT, " +
				Variable.M_MSG_TITLE + " TEXT, " +
				Variable.M_MSG_HTML_CONTENT + " TEXT, " +
				Variable.M_MSG_URL + " TEXT" +
				");";

		private final String TBDrop_UserInfo = 
				"DROP TABLE IF EXISTS " + Variable.TBName_UserInfo;
		
		public DBHelper(Context context) {
			super(context, DBName, null, DBVersion);			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TBDrop_Log);
			db.execSQL(TBDrop_UserInfo);
			db.execSQL(TBDrop_Message);
			db.execSQL(TBCreate_Log);
			db.execSQL(TBCreate_UserInfo);
			db.execSQL(TBCreate_Message);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(TBDrop_Log);
			db.execSQL(TBDrop_UserInfo);
			db.execSQL(TBDrop_Message);
			onCreate(db);
		}
        public void createMessageTable(){
        		db.execSQL(TBCreate_Message);  
        }
		 
	}	
	
	private Context mContext = null;
	private static DBHelper dbHelper;
	private static SQLiteDatabase db;
	private static DB mDbHelper;
	private final String TBCreate_Message = 
			"CREATE TABLE IF NOT EXISTS " + Variable.TBName_Message + "( " +
			Variable.M_MSG_ID + " TEXT NOT NULL PRIMARY KEY," +
			Variable.M_MSG_ORDER + " TEXT, " +
			Variable.M_MSG_STATUS + " TEXT, " +
			Variable.M_MSG_TIME + " TEXT, " +
			Variable.M_MSG_TITLE + " TEXT, " +
			Variable.M_MSG_HTML_CONTENT + " TEXT, " +
			Variable.M_MSG_URL + " TEXT" +
			");";
	
	
	
	public DB(Context context) {
		this.mContext = context;
	}

	
	
	
	public DB openDB() throws SQLException{
		if(Looper.myLooper()==Looper.getMainLooper()){
		};
		  dbHelper = new DBHelper(mContext);
		if(db!=null){
			if(!db.isOpen()){
				db = dbHelper.getReadableDatabase();
			}
		}
		return this;
	}
	
	public void closeDB() {
//		if(db.isOpen()){
//			db.close();
			dbHelper.close();
//		}
	}

	/** log */
	//get all entries from TB-Log
	public String[][] getLog(Context context) {
		mDbHelper = new DB(context);
		
		mDbHelper.openDB();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(Variable.TBName_Log, 
								 new String[]{Variable.L_ERR_VERSION, Variable.L_ERR_FUNC, Variable.L_ERR_DESC, Variable.L_ERR_TIME, Variable.L_LAST_UPDATED_DATE}, 
								 null, 
								 null, 
								 null, 
								 null, 
								 null);
		
		//((Activity) context).startManagingCursor(cursor);--->This line should be mark,otherwise it will start managing cusor and it will close when activity onPause!!
		int rowCount = cursor.getCount();
		int colCount = cursor.getColumnCount();
		String[][] value = new String[rowCount][colCount]; 
		int i = 0;
		while(cursor.moveToNext()){
			for (int j = 0; j < colCount; j++) {	
				value[i][j] = cursor.getString(j);	
				
				if (j == colCount-1) {
					i = i + 1;
				}
			}
		}
		//cursor.close();
		//mDbHelper.closeDB();
		return value;
	}
	
	//add an entry to TB-Log
	public void addLog(Context context, String func, String desc) {
		Activity activity=(Activity)context;
		CIEMPApplication application=(CIEMPApplication)activity.getApplication();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		mDbHelper = new DB(context);
		mDbHelper.openDB();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Variable.L_ERR_VERSION, SplashScreen.AppVersion); 
        values.put(Variable.L_ERR_VERSION, application.getAppVersion()); 
        values.put(Variable.L_ERR_FUNC, func);
        values.put(Variable.L_ERR_DESC, desc);
        values.put(Variable.L_ERR_TIME, df.format(new Date()));
        values.put(Variable.L_LAST_UPDATED_DATE, df.format(new Date()));
        db.insert(Variable.TBName_Log, null, values);
        //mDbHelper.closeDB();
    }		
	
	//delete all entry from TB-Log
	public void dropLog(Context context) {
		mDbHelper = new DB(context);
		mDbHelper.openDB();
        db = dbHelper.getWritableDatabase();
        db.delete(Variable.TBName_Log, null, null);        
        //mDbHelper.closeDB();
    }
	
	/** user_info */
	//get all entries from TB-UserInfo
	public String[][] getUserInfo(Context context) {
		Activity activity=(Activity)context;
		CIEMPApplication application=(CIEMPApplication)activity.getApplication();
		mDbHelper = new DB(context);
		mDbHelper.openDB();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(Variable.TBName_UserInfo, 
								 new String[]{Variable.U_TESTUSER, Variable.U_SHAREUSER, Variable.U_USER_NAME_TW, Variable.U_USER_NAME_US, Variable.U_USER_ID, Variable.U_USER_UNIT_TW, Variable.U_USER_UNIT_US,
									 		  Variable.U_USER_LEAD_UT, Variable.U_USER_UNIT_CD, Variable.U_USER_TRD_UNT, Variable.U_USER_AUTH_LVF, Variable.U_USER_AUTH_LVS,
									 		  Variable.U_USER_AUTH_SYSCD, Variable.U_USER_FIRSTNAME_EN, Variable.U_USER_LASTNAME_EN, Variable.U_USER_FIRSTNAME_TW, Variable.U_USER_LASTNAME_TW,
									 		  Variable.U_USER_PHONE_NUMBER_1, Variable.U_USER_PHONE_NUMBER_2, Variable.U_USER_MOBILE_NUMBER_1, Variable.U_USER_MOBILE_NUMBER_2, Variable.U_USER_EMAIL_1,
									 		  Variable.U_USER_EMAIL_2, Variable.U_REGISTRANTION_TIME, Variable.U_LAST_UPDATED_DATE, Variable.U_USER_STATUS, Variable.U_DEVID}, 
								 Variable.U_USER_ID + "=" + application.getUserId(), 
								 null, 
								 null, 
								 null, 
								 null);
		//((Activity) context).startManagingCursor(cursor);--->This line should be mark,otherwise it will start managing cusor and it will close when activity onPause!!
		int rowCount = cursor.getCount();
		int colCount = cursor.getColumnCount();
		String[][] value = new String[rowCount][colCount]; 
		int i = 0;
		while(cursor.moveToNext()){
			for (int j = 0; j < colCount; j++) {	
				value[i][j] = cursor.getString(j);	
				
				if (j == colCount-1) {
					i = i + 1;
				}
			}
		}		
		//mDbHelper.closeDB();
		return value;
	}
	

	public void inserHtmlToMsg(Context context, String msg_id, String msg) {
		// TODO Auto-generated method stub
		mDbHelper = new DB(context);
		mDbHelper.openDB();
		
        db = dbHelper.getWritableDatabase();
        dbHelper.createMessageTable();
        ContentValues values = new ContentValues();
        values.put(Variable.M_MSG_HTML_CONTENT, msg);  
        //db.update(Variable.TBName_Message, "WHERE msg_id="+item.getMsgId(), values);
//        db.update(Variable.TBName_Message, values, "msg_id="+item.getMsgId(), null);
        db.update(Variable.TBName_Message, values, "msg_id=?", new String[]{msg_id});
        mDbHelper.closeDB();
        
	}
		
	public String queryHtmlMsg(Context context, String msg_id) {
		mDbHelper = new DB(context);
		mDbHelper.openDB();
        db = dbHelper.getWritableDatabase();
        dbHelper.createMessageTable();
        Cursor cursor = db.rawQuery("SELECT * FROM message WHERE msg_id=?", new String[]{msg_id});
        String msg = null;
        while (cursor.moveToNext()) {
            msg = cursor.getString(5);
        }
        cursor.close();
        return msg;
	}
	
}
