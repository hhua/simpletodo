package com.hhua.android.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends Activity {
    private static final String TAG = "EditItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String text = getIntent().getStringExtra("text");
        String dueDate = getIntent().getStringExtra("dueDate");
        EditText editItem = (EditText) findViewById(R.id.editItem);
        DatePicker editDueDate = (DatePicker) findViewById(R.id.editDueDate);
        editItem.setText(text);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Calendar cal = Calendar.getInstance();

            Date dueDateInfo = dateFormat.parse(dueDate);
            cal.setTime(dueDateInfo);
            editDueDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }catch (Exception ex) {
            Log.d(TAG, "Error while parsing due date!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmit(View v) {

        // Prepare data intent
        Intent data = new Intent();

        // Pass relevant data back as a result
        EditText editItem = (EditText) findViewById(R.id.editItem);
        String text = editItem.getText().toString().trim();
        DatePicker editDueDate = (DatePicker) findViewById(R.id.editDueDate);
        String dueDate = editDueDate.getDayOfMonth() + "-" + (editDueDate.getMonth() + 1) + "-" + editDueDate.getYear();

        // Pop up a message: Task cannot be empty
        if (text.equals("")) {
            Toast.makeText(this, "Task cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        data.putExtra("text", editItem.getText().toString());
        int pos = getIntent().getIntExtra("pos", 0);
        data.putExtra("pos", pos);
        data.putExtra("dueDate", dueDate);

        setResult(RESULT_OK, data); // set result code and bundle data for response
        // closes the activity and returns to first screen
        this.finish();
    }
}
