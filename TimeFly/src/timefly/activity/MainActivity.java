package timefly.activity;

import java.util.Calendar;
import timefly.adapters.MySimpleAdapter;
import timefly.database.DB;

import com.example.timefly.R;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<Cursor> {

	private ListView tasksList;
	private DB db;
	private MySimpleAdapter scAdapter;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_CALENDER = 2;
	private static final int DELETE_ID = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		db = new DB(this);
		db.open();
		fillData();
		todayDate();
		getSupportLoaderManager().initLoader(0, null, this);

		tasksList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor thisTask = db.getThisTask(id);
				final CheckBox ch = (CheckBox) view
						.findViewById(R.id.this_task_finished);
				/*
				 * final String title = ((TextView) view
				 * .findViewById(R.id.this_task_title)).getText() .toString();
				 * 
				 * final String desc = ((TextView) view
				 * .findViewById(R.id.this_task_description)).getText()
				 * .toString();
				 */

				final long thisId = id;
				TextView tt = (TextView) findViewById(R.id.today_title);
				// tt.setText(express + "");
				ch.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (ch.isChecked()) {
							db.updateTask(thisId, null, null, 1, -2, -2, -2);
						} else {
							db.updateTask(thisId, null, null, 0, -2, -2, -2);
						//	fillData();
							// bt.setText("true");
						}

					}
				});

			}
		});

	}

	public void fillData() {
		Cursor cursor = db.getAllData();
		// формируем столбцы сопоставления
		String[] from = new String[] { DB.COLUMN_TITLE, DB.COLUMN_DESCRIPTION };
		int[] to = new int[] { R.id.this_task_title, R.id.this_task_description };

		// создааем адаптер и настраиваем список
		scAdapter = new MySimpleAdapter(this, R.layout.this_task, cursor, from,
				to, 0);
		tasksList = (ListView) findViewById(R.id.task_list);
		tasksList.setAdapter(scAdapter);

		// добавляем контекстное меню к списку
		//registerForContextMenu(tasksList);

		// создаем лоадер для чтения данных
		
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.show_calender:
			initCalender();
			return true;

		case R.id.insert_task:
			createTask();
			return true;

		case R.id.show_today:
			todayDate();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void createTask() {
		// добавляем запись
		// db.addTask("ЗАДАЧА", "ОПИСАНИЕ", 0);
		// получаем новый курсор с данными

		Intent i = new Intent(this, EditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
		getSupportLoaderManager().getLoader(0).forceLoad();

	}

	private void todayDate() {

		Calendar c = Calendar.getInstance();
		int yearr = c.get(c.YEAR);
		int monthh = c.get(c.MONTH) + 1;
		int dayy = c.get(c.DAY_OF_MONTH);

		TextView today_title = (TextView) findViewById(R.id.today_title);
		today_title.setText(dayy + "." + monthh + "." + yearr);
	}

	private void initCalender() {
		Intent intent = new Intent(this, CalenderActivity.class);
		startActivityForResult(intent, ACTIVITY_CALENDER);
		getSupportLoaderManager().getLoader(0).forceLoad();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == ACTIVITY_CALENDER && resultCode == RESULT_OK) {
			TextView today_title = (TextView) findViewById(R.id.today_title);
			int thisDay = intent.getIntExtra("DAY", 0);
			int thisMonth = intent.getIntExtra("MONTH", 0);
			int thisYear = intent.getIntExtra("YEAR", 0);
			today_title.setText(thisDay + "." + thisMonth + "." + thisYear);
		}
		fillData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	protected void onDestroy() {
		super.onDestroy();
		// закрываем подключение при выходе
		db.close();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, db);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		scAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	static class MyCursorLoader extends CursorLoader {

		DB db;

		public MyCursorLoader(Context context, DB db) {
			super(context);
			this.db = db;
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = db.getAllData();
			return cursor;
		}

	}

}
