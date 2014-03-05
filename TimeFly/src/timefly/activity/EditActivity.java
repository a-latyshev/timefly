package timefly.activity;

import timefly.database.DB;

import com.example.timefly.R;
import com.example.timefly.R.layout;
import com.example.timefly.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class EditActivity extends Activity {

	private EditText edTitle;
	private EditText edDescription;
	private CheckBox edImportent;
	private CheckBox edExpress;
	private CheckBox edRoutine;
	private Button Confirm;
	private DB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_edit);
		edTitle = (EditText) findViewById(R.id.edit_task_title);
		edDescription = (EditText) findViewById(R.id.edit_task_description);
		edImportent = (CheckBox) findViewById(R.id.edit_task_importent);
		edExpress = (CheckBox) findViewById(R.id.edit_task_express);
		edRoutine = (CheckBox) findViewById(R.id.edit_task_routine);
		Confirm = (Button) findViewById(R.id.edit_button_confirm);
		db = new DB(this);
		db.open();
		Confirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_OK);
				createTask();
				finish();
			}

		});
	}

	public void onEditImportentTask(View view){
		boolean checked = ((CheckBox) view).isChecked();
		edImportent.setChecked(checked);
	}
	
	public void onEditExpressTask(View view){
		boolean checked = ((CheckBox) view).isChecked();
		edExpress.setChecked(checked);
	}
	public void onEditRoutineTask(View view){
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
		if(edImportent.isChecked()){
			important = 1;
		}
		if(edExpress.isChecked()){
			express = 1;
		}
		if(edRoutine.isChecked()){
			routine = 1;
		}
		db.addTask(title, description, finished, important, express, routine);
	}

	private void updateTask() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
