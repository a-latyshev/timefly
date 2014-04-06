package timefly.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timefly.adapters.DateHelper;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		LoaderCallbacks<Cursor> {

	private ListView tasksList;
	private static DB db;
	private MySimpleAdapter scAdapter;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_CALENDER = 2;
	private static final int ACTIVITY_EDIT_CREATED_TASK = 3;
	private static final int CONTEXT_DELETE_TASK = 1;
	private static final int CONTEXT_EDIT_TASK = 2;

	public static Cursor dataCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		db = new DB(this);
		db.open();

		getSupportLoaderManager().initLoader(0, null, this);
		showInbox();
		tasksList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView desc = (TextView) view
						.findViewById(R.id.this_task_description);
				if (desc.getTag().toString().equals("2")) {
					desc.setTag("0");
					desc.setVisibility(desc.VISIBLE);
				} else {
					desc.setTag("2");
					desc.setVisibility(desc.GONE);
				}

			}

		});
	}

	public static void updater(int id, int checked, int type) {

		if (type == 0) {
			if (checked == 1) {
				db.updateTask(id, null, null, 1, -2, -2, -2, -2);
			} else {
				db.updateTask(id, null, null, 0, -2, -2, -2, -2);
			}
		}

		if (type == 1) {
			if (checked == 1) {
				db.updateTask(id, null, null, -2, 1, -2, -2, -2);
			} else {
				db.updateTask(id, null, null, -2, 0, -2, -2, -2);
			}
		}

		if (type == 2) {
			if (checked == 1) {
				db.updateTask(id, null, null, -2, -2, 1, -2, -2);
			} else {
				db.updateTask(id, null, null, -2, -2, 0, -2, -2);
			}
		}

		if (type == 3) {
			if (checked == 1) {
				db.updateTask(id, null, null, -2, -2, -2, 1, -2);
			} else {
				db.updateTask(id, null, null, -2, -2, -2, 0, -2);
			}
		}

	}

	public void fillData() {
		TextView title = (TextView) findViewById(R.id.nothing_to_do);
		title.setVisibility(title.GONE);
		dataCursor = db.getAllData();
		// формируем столбцы сопоставления
		String[] from = new String[] { DB.COLUMN_TITLE, DB.COLUMN_DESCRIPTION };
		int[] to = new int[] { R.id.this_task_title, R.id.this_task_description };

		// создааем адаптер и настраиваем список
		scAdapter = new MySimpleAdapter(this, R.layout.this_task, dataCursor,
				from, to, 0);
		tasksList = (ListView) findViewById(R.id.task_list);
		tasksList.setAdapter(scAdapter);

		// добавляем контекстное меню к списку
		registerForContextMenu(tasksList);

		// создаем лоадер для чтения данных
	}

	public void fillDataByDateTasks(long date) {
		TextView title = (TextView) findViewById(R.id.nothing_to_do);
		dataCursor = db.getTasksFromDate(date);
		title.setVisibility(title.GONE);
		if (dataCursor.getCount() < 1) {
			title.setVisibility(title.VISIBLE);
		}
		String[] from = new String[] { DB.COLUMN_TITLE, DB.COLUMN_DESCRIPTION };
		int[] to = new int[] { R.id.this_task_title, R.id.this_task_description };

		// создааем адаптер и настраиваем список
		scAdapter = new MySimpleAdapter(this, R.layout.this_task, dataCursor,
				from, to, 0);
		tasksList = (ListView) findViewById(R.id.task_list);
		tasksList.setAdapter(scAdapter);

		// добавляем контекстное меню к списку
		registerForContextMenu(tasksList);

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

		case R.id.show_inbox:
			showInbox();
			return true;

		case R.id.exit:
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void showInbox() {
		TextView title = (TextView) findViewById(R.id.today_title);
		title.setText("Входящие");
		fillData();
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
		fillData();
		Calendar c = Calendar.getInstance();
		DateHelper dh = new DateHelper();
		int yearr = c.get(Calendar.YEAR);
		int monthh = c.get(Calendar.MONTH) + 1;
		int dayy = c.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");

		TextView today_title = (TextView) findViewById(R.id.today_title);

		today_title.setText("Сегодня " + dayy + " " + dh.getMonth(monthh - 1)
				+ " " + yearr + ", " + dh.getWeek(c.get(Calendar.DAY_OF_WEEK)));

		long date = dh.getLongTime(dayy + "-" + monthh + "-" + yearr, "");

		fillDataByDateTasks(date);

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
			String ChoseDate = intent.getStringExtra("ChoseDate");
			showThisChoseDateTasks(ChoseDate);
		} else {
			// ###############ВОЗМОЖНО ОШИБКА№№№№№№№№№№№№№№№№№№№№№№№№№№№№
			showInbox();
		}

	}

	public void showThisChoseDateTasks(String choseDate) {
		DateHelper dh = new DateHelper();
		TextView today_title = (TextView) findViewById(R.id.today_title);
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("d");
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		SimpleDateFormat dayOfWeak = new SimpleDateFormat("EE");
		Date thisDate = new Date(dh.getLongTime(choseDate, ""));
		String title = day.format(thisDate) + " "
				+ dh.getMonth(Integer.parseInt(month.format(thisDate)) - 1)
				+ " " + year.format(thisDate) + ", "
				+ dh.getWeek(dayOfWeak.format(thisDate));
		today_title.setText(title);

		fillDataByDateTasks(dh.getLongTime(choseDate, ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXT_EDIT_TASK, 0, "Изменить");
		menu.add(0, CONTEXT_DELETE_TASK, 0, "Удалить");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CONTEXT_EDIT_TASK:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Intent intent = new Intent(this, EditActivity.class);
			intent.putExtra(DB.COLUMN_ID, info.id);
			startActivityForResult(intent, ACTIVITY_EDIT_CREATED_TASK);
			return true;

		case CONTEXT_DELETE_TASK:
			AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item
					.getMenuInfo();
			db.deleteTask(info1.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
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
			return dataCursor;
		}

	}

}
