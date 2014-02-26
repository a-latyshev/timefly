package timefly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TimeFlyDatabaseAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_DUEDATE = "duedate";
	public static final String KEY_FINISHED = "finished";
	public static final String KEY_IMPORTANT = "important";
	public static final String KEY_EXPRESS = "express";
	public static final String KEY_ROUTINE = "routine";
	private static final String DATABASE_TABLE = "timeflytasks";
	private Context context;
	private SQLiteDatabase database;
	private TimeFlyDatabaseInit databaseInit;

	public TimeFlyDatabaseAdapter(Context context) {
		this.context = context;
	}

	public TimeFlyDatabaseAdapter open() throws SQLException {
		databaseInit = new TimeFlyDatabaseInit(context);
		database = databaseInit.getWritableDatabase();
		return this;
	}

	public void close() {
		databaseInit.close();
	}

	public long createTask(String title, String description, String duedate,
			long finished, long important, long express, long routine) {
		ContentValues initialValues = createContentValues(title, description,
				duedate, finished, important, express, routine);
		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean updateTask(long rowId, String title, String description,
			String duedate, long finished, long important, long express,
			long routine) {
		ContentValues updateValues = createContentValues(title, description,
				duedate, finished, important, express, routine);
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	public boolean deleteTask(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllTasks() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_TITLE, KEY_FINISHED, KEY_IMPORTANT, KEY_ROUTINE,
				KEY_EXPRESS, KEY_DESCRIPTION, KEY_DUEDATE }, null, null, null,
				null, null);
	}

	public Cursor fetchTasks(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_TITLE, KEY_FINISHED, KEY_IMPORTANT, KEY_ROUTINE,
				KEY_EXPRESS, KEY_DESCRIPTION, KEY_DUEDATE }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	private ContentValues createContentValues(String title, String description,
			String duedate, long finished, long important, long express,
			long routine) {
		ContentValues values = new ContentValues();
		values.put(KEY_DUEDATE, duedate);
		values.put(KEY_TITLE, title);
		values.put(KEY_DESCRIPTION, description);
		values.put(KEY_IMPORTANT, important);
		values.put(KEY_EXPRESS, express);
		values.put(KEY_ROUTINE, routine);
		values.put(KEY_FINISHED, finished);
		return values;
	}

}
