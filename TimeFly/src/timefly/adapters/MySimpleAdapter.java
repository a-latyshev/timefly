package timefly.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.timefly.R;

import timefly.activity.MainActivity;
import timefly.database.DB;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MySimpleAdapter extends SimpleCursorAdapter {

	private Cursor cursor;
	private DB db;

	public MySimpleAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.cursor = c;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view = super.getView(pos, convertView, parent);
		this.cursor.moveToPosition(pos);
		final TextView title = (TextView) view
				.findViewById(R.id.this_task_title);

		final int thisId = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_ID));

		long checkeed = cursor.getLong(cursor
				.getColumnIndex(DB.COLUMN_FINISHED));

		final CheckBox cBox = (CheckBox) view
				.findViewById(R.id.this_task_finished);
		if (checkeed == 1) {
			cBox.setChecked(true);
		} else {
			cBox.setChecked(false);
		}

		cBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (cBox.isChecked()) {
					MainActivity.updater(thisId, 1, 0);
				} else {
					MainActivity.updater(thisId, 0, 0);
					// Toast.makeText(getApplicationContext(), "***",
					// Toast.LENGTH_LONG);
				}
				// cBox.setChecked(cBox.isChecked());
				/*
				 * if (cBox.isChecked()) { cBox.setTag("1"); //
				 * db.updateTask(thisId, null, null, 1, -2, -2, -2, // null); }
				 * else { // db.updateTask(thisId, null, null, 0, -2, -2, -2, //
				 * null); cBox.setTag("1"); // fillData(); //
				 * bt.setText("true"); }
				 */
			}
		});

		final long importanted = cursor.getLong(cursor
				.getColumnIndex(DB.COLUMN_IMPORTANT));
		final ImageView important = (ImageView) view
				.findViewById(R.id.this_task_important);

		long expressed = cursor.getLong(cursor
				.getColumnIndex(DB.COLUMN_EXPRESS));
		final ImageView express = (ImageView) view
				.findViewById(R.id.this_task_express);

		long routined = cursor
				.getLong(cursor.getColumnIndex(DB.COLUMN_ROUTINE));
		final ImageView routine = (ImageView) view
				.findViewById(R.id.this_task_routine);

		if (importanted == 1) {
			important.setImageResource(R.drawable.imp1);
			important.setTag("checked");
		} else {
			important.setImageResource(R.drawable.imp0);
			important.setTag("not checked");
		}

		if (expressed == 1) {
			express.setImageResource(R.drawable.exp1);
			express.setTag("checked");
		} else {
			express.setImageResource(R.drawable.exp0);
			express.setTag("not checked");
		}

		if (routined == 1) {
			routine.setImageResource(R.drawable.rout1);
			routine.setTag("checked");
		} else {
			routine.setImageResource(R.drawable.rout0);
			routine.setTag("not checked");
		}

		important.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (important.getTag().equals("not checked")) {
					important.setTag("checked");
					MainActivity.updater(thisId, 1, 1);
					important.setImageResource(R.drawable.imp1);
				} else {
					important.setTag("not checked");
					MainActivity.updater(thisId, 0, 1);
					important.setImageResource(R.drawable.imp0);
				}
			}
		});

		express.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (express.getTag().equals("not checked")) {
					express.setTag("checked");
					MainActivity.updater(thisId, 1, 2);
					express.setImageResource(R.drawable.exp1);
				} else {
					express.setTag("not checked");
					MainActivity.updater(thisId, 0, 2);
					express.setImageResource(R.drawable.exp0);
				}
			}
		});

		routine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (routine.getTag().equals("not checked")) {
					routine.setTag("checked");
					MainActivity.updater(thisId, 1, 3);
					routine.setImageResource(R.drawable.rout1);
				} else {
					routine.setTag("not checked");
					MainActivity.updater(thisId, 0, 3);
					routine.setImageResource(R.drawable.rout0);
				}
			}
		});

		long date = cursor.getLong(cursor.getColumnIndex(DB.COLUMN_DUEDATE));
		TextView Duedate = (TextView) view.findViewById(R.id.this_task_date);

		if (date != 0) {
			Duedate.setVisibility(Duedate.VISIBLE);
			Date thisDate = new Date(date);
			SimpleDateFormat month = new SimpleDateFormat("MM");
			SimpleDateFormat day = new SimpleDateFormat("d");
			SimpleDateFormat year = new SimpleDateFormat("yy");
			SimpleDateFormat time = new SimpleDateFormat("HH:mm");
			String taskTime = "";
			if ((thisDate.getTime() % 1000000l) != 0) {
				taskTime = time.format(thisDate);
			}
			String taskDate = day.format(thisDate) + "-"
					+ Integer.parseInt(month.format(thisDate)) + "-"
					+ year.format(thisDate);
			Duedate.setText(taskDate+" " +taskTime);
		}

		return view;
	}

}
