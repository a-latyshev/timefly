package timefly.adapters;

import com.example.timefly.R;

import timefly.database.DB;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class MySimpleAdapter extends SimpleCursorAdapter {

	private Cursor cursor;
	
	public MySimpleAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		this.cursor = c;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		View view = super.getView(pos, convertView, parent);
		this.cursor.moveToPosition(pos);

		long checkeed = cursor.getLong(cursor.getColumnIndex(DB.COLUMN_FINISHED));

		final CheckBox cBox = (CheckBox) view
				.findViewById(R.id.this_task_finished);

		if (checkeed == 1) {
			cBox.setChecked(true);
		} else {
			cBox.setChecked(false);
		}

		return view;
	}


}
