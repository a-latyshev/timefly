package timefly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

	public static final String TABLE_NAME = "Tasks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_FINISHED = "finished";
	public static final String COLUMN_IMPORTANT = "important";
	public static final String COLUMN_EXPRESS = "express";
	public static final String COLUMN_ROUTINE = "routine";
	public static final String COLUMN_DUEDATE= "duedate";

	private static final String DATABASE_NAME = "TimeFly.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
			+ " (" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TITLE + " text, " + COLUMN_FINISHED + " integer, "
			+ COLUMN_IMPORTANT + " integer, " + COLUMN_EXPRESS + " integer, "
			+ COLUMN_ROUTINE + " integer, " 
			+ COLUMN_DUEDATE + " text, " 
			+ COLUMN_DESCRIPTION + " text "
			+ ");";

	private final Context mCtx;

	private DBHelper mDBHelper;
	private SQLiteDatabase db;

	public DB(Context ctx) {
		mCtx = ctx;
	}

	public void open() {
		mDBHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		db = mDBHelper.getWritableDatabase();
	}

	public void close() {
		if (mDBHelper != null)
			mDBHelper.close();
	}

	public Cursor getAllData() {
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public Cursor getThisTask(long rowId) throws SQLException {
		String[] columns = new String[] { COLUMN_DESCRIPTION, COLUMN_FINISHED,
				COLUMN_TITLE, COLUMN_EXPRESS, COLUMN_IMPORTANT, COLUMN_ROUTINE, COLUMN_DUEDATE };
		Cursor mCursor = db.query(true, TABLE_NAME, columns, COLUMN_ID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// добавить запись в DB_TABLE
	public long addTask(String title, String decription, long finished,
			long important, long express, long routine, String duedate) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TITLE, title);
		cv.put(COLUMN_DESCRIPTION, decription);
		cv.put(COLUMN_FINISHED, finished);
		cv.put(COLUMN_IMPORTANT, important);
		cv.put(COLUMN_EXPRESS, express);
		cv.put(COLUMN_ROUTINE, routine);
		cv.put(COLUMN_DUEDATE, duedate);
		return db.insert(TABLE_NAME, null, cv);
	}

	public boolean updateTask(long rowId, String title, String description,
			long finished, long important, long express, long routine, String duedate) {
		ContentValues cv = new ContentValues();
		if (title != null) {
			cv.put(COLUMN_TITLE, title);
		}
		if (description != null) {
			cv.put(COLUMN_DESCRIPTION, description);
		}
		if (finished != -2) {
			cv.put(COLUMN_FINISHED, finished);
		}
		if (important != -2) {
			cv.put(COLUMN_IMPORTANT, important);
		}
		if (express != -2) {
			cv.put(COLUMN_EXPRESS, express);
		}
		if (routine != -2) {
			cv.put(COLUMN_ROUTINE, routine);
		}
		if (duedate != null) {
			cv.put(COLUMN_DUEDATE, duedate);
		}
		return db.update(TABLE_NAME, cv, COLUMN_ID + "=" + rowId, null) > 0;
	}

	public boolean deleteTask(long rowId) {
		return db.delete(TABLE_NAME, COLUMN_ID + "=" + rowId, null) > 0;
	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		// создаем и заполняем БД
		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(database);
		}
	}

}
