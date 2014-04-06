package timefly.activity;

import java.util.Calendar;

import com.example.timefly.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class CalenderActivity extends Activity {

	public long timmm = 0;
	private int choseYear = 0;
	private int choseMonth = 0;
	private int choseDay = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calender_view);
		final CalendarView CV = (CalendarView) findViewById(R.id.calendar);
		final Button bt = (Button) findViewById(R.id.button1);

		bt.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				if (choseDay == 0) {
					Calendar c = Calendar.getInstance();
					choseDay = c.get(Calendar.DAY_OF_MONTH);
					choseMonth = c.get(Calendar.MONTH) + 1;
					choseYear = c.get(Calendar.YEAR);
				}
				Intent intent = getIntent();
				intent.putExtra("ChoseDate", choseDay + "-" + choseMonth + "-"
						+ choseYear);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		CV.setOnDateChangeListener(new OnDateChangeListener() {

			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				choseDay = dayOfMonth;
				choseMonth = month + 1;
				choseYear = year;
			}

		});

	}

}
