package timefly.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import timefly.adapters.DateHelper;
import timefly.adapters.MySimpleAdapter;
import timefly.database.DB;

import com.example.timefly.R;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Spanned;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
				/*
				 * TextView ttt = (TextView)
				 * view.findViewById(R.id.this_task_title); //Spanned spanText =
				 * android.text.Html.fromHtml(tt.getText().toString());
				 * //tt.setText(spanText);
				 * ttt.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				 */
				TextView desc = (TextView) view
						.findViewById(R.id.this_task_description);
				if (desc.getTag().toString().equals("2")) {
					desc.setTag("0");
					desc.setVisibility(desc.VISIBLE);
				} else {
					desc.setTag("2");
					desc.setVisibility(desc.GONE);
				}
				// tt.setText(express + "");
			/*	ch.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (ch.isChecked()) {
							db.updateTask(thisId, null, null, 1, -2, -2, -2,
									null);
						} else {
							db.updateTask(thisId, null, null, 0, -2, -2, -2,
									null);
							// fillData();
							// bt.setText("true");
						}

					}
				});*/

			}

		});

		/*
		 * tasksList.setOnItemLongClickListener(new OnItemLongClickListener() {
		 * Intent i = new Intent(null, null);
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> parent, View
		 * view, int position, long id) {
		 * 
		 * 
		 * startActivityForResult(i, ACTIVITY_CREATE);
		 * getSupportLoaderManager().getLoader(0).forceLoad();
		 * 
		 * return false; } });
		 */

	}

	public static void updater(int id, int checked) {
		if (checked == 1) {
			db.updateTask(id, null, null, 1, -2, -2, -2, -2);
		} else {
			db.updateTask(id, null, null, 0, -2, -2, -2, -2);
		}

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
		registerForContextMenu(tasksList);

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
		DateHelper dh = new DateHelper();
		// Date time = (Date) c.getTime();
		int yearr = c.get(Calendar.YEAR);
		int monthh = c.get(Calendar.MONTH) + 1;
		int dayy = c.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
		// db.addTask("1", "", 0, 1, 1, 1, 1394109081264L);
		TextView today_title = (TextView) findViewById(R.id.today_title);
		today_title.setText(dayy + " " + dh.getMonth(Calendar.MONTH) + " "
				+ yearr + ", " + dh.getWeek(Calendar.DAY_OF_WEEK));
		/*
		 * today_title.setText(Calendar.MONDAY+" " + Calendar.TUESDAY + " "
		 * +Calendar.WEDNESDAY + " " + Calendar.THURSDAY + " " + Calendar.FRIDAY
		 * + " " + Calendar.SUNDAY + " " + Calendar.SATURDAY);
		 */

		// Date time = new Date();
		// Calendar c2 = Calendar.getInstance();
		// c2.setTimeInMillis(1394109081264L);
		// int yr = c2.get(Calendar.YEAR);
		// today_title.setText(dayy + "." + monthh + "." + yearr + "**" + mo);
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
			Cursor cursor = db.getAllData();
			return cursor;
		}

	}

}
