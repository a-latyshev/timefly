package timefly.activity;

import com.example.timefly.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalenderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calender_view);
		CalendarView CV = (CalendarView) findViewById(R.id.calendar);

		CV.setOnDateChangeListener(new OnDateChangeListener() {

			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				Intent intent = getIntent();
				intent.putExtra("DAY", dayOfMonth);
				intent.putExtra("MONTH", month + 1);
				intent.putExtra("YEAR", year);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.show_today:
			Intent intent = getIntent();
			setResult(2, intent);
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
