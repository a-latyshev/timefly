package timefly.activity;

import java.util.Calendar;

import com.example.timefly.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		todayDate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.show_calender:
			initCalender();
			return true;

		case R.id.show_today:
			todayDate();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
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
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == RESULT_OK) {
			TextView today_title = (TextView) findViewById(R.id.today_title);
			int thisDay = data.getIntExtra("DAY", 0);
			int thisMonth = data.getIntExtra("MONTH", 0);
			int thisYear = data.getIntExtra("YEAR", 0);
			today_title.setText(thisDay + "." + thisMonth + "." + thisYear);
		}

		if (requestCode == 1 && resultCode == 2) {
			todayDate();
		}
	}

}
