package com.hhua.android.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhua.android.simpletodo.adapters.TasksAdapter;
import com.hhua.android.simpletodo.models.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    List<Task> items;
    TasksAdapter itemsAdapter;
    ListView lvItems;
    // REQUEST_CODE can be any value we like, used to determine the result type later
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        if (items == null){
            items = new ArrayList<Task>();
        }

        itemsAdapter = new TasksAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String text = data.getExtras().getString("text");
            int pos = data.getExtras().getInt("pos", 0);
            Task task = items.get(pos);
            task.title = text;
            items.set(pos, task);
            itemsAdapter.notifyDataSetChanged();

            // Update database
            updateItem(task);
        }
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString().trim();

        if (itemText.equals("")) {
            // Pop up a message: Task cannot be empty
            Toast.makeText(this, "Task cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(itemText);
        addItem(task);
        itemsAdapter.add(task);
        etNewItem.setText("");

        readItems();

        itemsAdapter = new TasksAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        // delete task from database
                        Task task = items.get(pos);
                        deleteItem(task);
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        TextView taskTitle = (TextView) item.findViewById(R.id.taskTitle);
                        String text = taskTitle.getText().toString();
                        i.putExtra("text", text);
                        i.putExtra("pos", pos);
                        startActivityForResult(i, REQUEST_CODE); // brings up the second activity
                    }
                }
        );
    }

    private void addItem(Task task){
        TasksDatabaseHelper tasksDatabaseHelper = TasksDatabaseHelper.getInstance(this);
        tasksDatabaseHelper.addTask(task);
    }

    private void readItems(){
        TasksDatabaseHelper tasksDatabaseHelper = TasksDatabaseHelper.getInstance(this);
        items = tasksDatabaseHelper.getAllTasks();
    }

    private void updateItem(Task task){
        TasksDatabaseHelper tasksDatabaseHelper = TasksDatabaseHelper.getInstance(this);
        tasksDatabaseHelper.updateTaskTitle(task);
    }

    private void deleteItem(Task task){
        TasksDatabaseHelper tasksDatabaseHelper = TasksDatabaseHelper.getInstance(this);
        tasksDatabaseHelper.deleteTask(task);
    }
}
