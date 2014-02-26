package timefly.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimeFlyDatabaseInit extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "TimeFlydatabase";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table timeflytasks (_id integer primary keyautoincrement, "
			+ "finished integer, "
			+ "important integer, "
			+ "express integer, "
			+ "routine integer, "
			+ "description text not null, "
			+ "title text not null, "
			+ "duedate text not null);";

	public TimeFlyDatabaseInit(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(TimeFlyDatabaseInit.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS ***todo***");
		onCreate(database);
	}

}
