package timefly.adapters;

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
		//db = new DB(this);
		//db.open();
		long checkeed = cursor.getLong(cursor.getColumnIndex(DB.COLUMN_FINISHED));
		final int thisId = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_ID));

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
					MainActivity.updater(thisId,1);
				}else{
					MainActivity.updater(thisId,0);
					//Toast.makeText(getApplicationContext(), "***", Toast.LENGTH_LONG);
				}
			//	cBox.setChecked(cBox.isChecked());
				/*if (cBox.isChecked()) {
					cBox.setTag("1");
				//	db.updateTask(thisId, null, null, 1, -2, -2, -2,
					//		null);
				} else {
				//	db.updateTask(thisId, null, null, 0, -2, -2, -2,
				//			null);
					cBox.setTag("1");
					// fillData();
					// bt.setText("true");
				}*/
			}
		});
		
		

		return view;
	}


}
