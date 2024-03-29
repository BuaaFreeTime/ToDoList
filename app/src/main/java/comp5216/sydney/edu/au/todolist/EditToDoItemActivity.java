/*
 * Copyright 2019 by BuaaFreeTime
 */

package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class EditToDoItemActivity extends Activity  {
	// Edit page Class

	// Define variables
	public int position=0;
	EditText etTitle;
	EditText etText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		
		//Get the data from the main screen
		String editTitle = getIntent().getStringExtra("title");
		String editText = getIntent().getStringExtra("text");
		position = getIntent().getIntExtra("position",-1);
		
		// show original content in the text field
		etTitle = (EditText)findViewById(R.id.etEditTitle);
		etTitle.setText(editTitle);
		etText = (EditText)findViewById(R.id.etEditText);
		etText.setText(editText);
	}

	public void onSubmit(View v) {
		// A method handle the save button
		etTitle = (EditText) findViewById(R.id.etEditTitle);
		etText = (EditText)findViewById(R.id.etEditText);

		// Prepare data intent for sending it back
		Intent data = new Intent();

		// Pass relevant data back as a result
		data.putExtra("title", etTitle.getText().toString());
		data.putExtra("text", etText.getText().toString());
		data.putExtra("position", position);
		data.putExtra("id", getIntent().getIntExtra("id", -1));

		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}

	public void onCancel(View v) {
		// A method handle the cancel button
		// Setting a dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
		builder.setTitle(R.string.dialog_cancel_title)
				.setMessage(R.string.dialog_cancel_msg)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						// Cancel what have done
						// set result code and bundle data for response
						setResult(RESULT_CANCELED, getIntent());
						finish(); // closes the activity, pass data to parent
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						// User cancelled the dialog
						// Nothing happens
					}
				});
		builder.create().show();
	}

}
