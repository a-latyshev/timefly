package timefly.activity;

import java.util.Calendar;

import timefly.database.DB;

import com.example.timefly.R;
import com.example.timefly.R.layout;
import com.example.timefly.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditActivity extends FragmentActivity {

	private EditText edTitle;
	private EditText edDescription;
	private CheckBox edImportent;
	private CheckBox edExpress;
	private CheckBox edRoutine;
	private Button Confirm;
	private DB db;

	private TextView edDuedate;
	private TextView edDuetime;

	private long rowId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_edit);
		edTitle = (EditText) findViewById(R.id.edit_task_title);
		edDescription = (EditText) findViewById(R.id.edit_task_description);
		edImportent = (CheckBox) findViewById(R.id.edit_task_importent);
		edExpress = (CheckBox) findViewById(R.id.edit_task_express);
		edRoutine = (CheckBox) findViewById(R.id.edit_task_routine);

		edDuedate = (TextView) findViewById(R.id.edit_date);
		edDuetime = (TextView) findViewById(R.id.edit_time);

		Confirm = (Button) findViewById(R.id.edit_button_confirm);
		db = new DB(this);
		db.open();

		Intent intent = getIntent();
		rowId = intent.getLongExtra(DB.COLUMN_ID, -1);
		if (rowId != -1) {
			populateFields();

		}

		Confirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (((edTitle.getText().toString().equals("")) || (edTitle
						.getText().toString().equals(" ")))) {
					Toast.makeText(getApplicationContext(),
							"У задачи должно быть название!", Toast.LENGTH_LONG)
							.show();

				} else {
					setResult(RESULT_OK);
					if (rowId != -1) {
						updateTask();
					} else {
						createTask();
					}
					finish();
				}
			}

		});

		edTitle.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					edTitle.setSelected(false);
					return true;
				}
				return false;
			}
		});

	}

	int DIALOG_DATE = 1;
	int DIALOG_TIME = 2;
	String edDuedate1 = "";
	String edDuedate2 = "";

	public void onEditDate(View view) {
		showDialog(DIALOG_DATE);
	}

	public void onEditTime(View view) {
		showDialog(DIALOG_TIME);
	}

	protected Dialog onCreateDialog(int id) {

		Calendar c = Calendar.getInstance();
		if (id == DIALOG_TIME) {
			TimePickerDialog tpd = new TimePickerDialog(this,
					callTimeDatePicker, c.get(Calendar.HOUR_OF_DAY),
					c.get(Calendar.MINUTE), true);
			return tpd;
		}

		if (id == DIALOG_DATE) {
			DatePickerDialog dpd = new DatePickerDialog(this,
					callBackDatePicker, c.get(Calendar.YEAR),
					c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
			return dpd;

		}
		return super.onCreateDialog(id);
	}

	OnDateSetListener callBackDatePicker = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			edDuedate.setText(day + "/" + month + "/" + year);
			edDuedate1 = day + ":" + month + ":" + year;
		}
	};

	OnTimeSetListener callTimeDatePicker = new OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			edDuetime.setText(hourOfDay + ":" + minute);
			edDuedate2 = ":" + hourOfDay + ":" + minute;
		}
	};

	public void onEditImportentTask(View view) {
		boolean checked = ((CheckBox) view).isChecked();
		edImportent.setChecked(checked);
	}

	public void onEditExpressTask(View view) {
		boolean checked = ((CheckBox) view).isChecked();
		edExpress.setChecked(checked);
	}

	public void onEditRoutineTask(View view) {
		boolean checked = ((CheckBox) view).isChecked();
		edRoutine.setChecked(checked);
	}

	private void createTask() {
		String title = edTitle.getText().toString();
		String description = edDescription.getText().toString();
		long finished = 0;
		long important = 0;
		long express = 0;
		long routine = 0;
		if (edImportent.isChecked()) {
			important = 1;
		}
		if (edExpress.isChecked()) {
			express = 1;
		}
		if (edRoutine.isChecked()) {
			routine = 1;
		}
		String duedate = edDuedate1 + edDuedate2;
		db.addTask(title, description, finished, important, express, routine,
				duedate);
	}

	private void populateFields() {
		Cursor task = db.getThisTask(rowId);
		String title = task.getString(task.getColumnIndex(DB.COLUMN_TITLE));
		String description = task.getString(task
				.getColumnIndex(DB.COLUMN_DESCRIPTION));
		long important = task.getLong(task.getColumnIndex(DB.COLUMN_IMPORTANT));
		long express = task.getLong(task.getColumnIndex(DB.COLUMN_EXPRESS));
		long routine = task.getLong(task.getColumnIndex(DB.COLUMN_ROUTINE));
		String duedate = task.getString(task.getColumnIndex(DB.COLUMN_DUEDATE));

		edTitle.setText(title);
		edDescription.setText(description);
		if (important == 1) {
			edImportent.setChecked(true);
		}
		if (express == 1) {
			edExpress.setChecked(true);
		}
		if (routine == 1) {
			edRoutine.setChecked(true);
		}
		edDuedate.setText(duedate);
		edDuetime.setText(duedate);
	}

	private void updateTask() {
		String title = edTitle.getText().toString();
		String description = edDescription.getText().toString();
		long finished = 0;
		long important = 0;
		long express = 0;
		long routine = 0;
		if (edImportent.isChecked()) {
			important = 1;
		}
		if (edExpress.isChecked()) {
			express = 1;
		}
		if (edRoutine.isChecked()) {
			routine = 1;
		}
		String duedate = edDuedate1 + edDuedate2;
		db.updateTask(rowId, title, description, finished, important, express,
				routine, duedate);
	}

}
