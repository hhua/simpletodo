package com.hhua.android.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hhua.android.simpletodo.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahua on 9/21/15.
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TASKS = "tasks";

    // Post Table Columns
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_TITLE = "title";
    private static final String KEY_TASK_DUE_DATE = "due_date";

    private static TasksDatabaseHelper sInstance;
    private static final String TAG = "TasksDatabaseHelper";

    public static synchronized TasksDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TasksDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TasksDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                KEY_TASK_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TASK_TITLE + " TEXT," +
                KEY_TASK_DUE_DATE + " TEXT" +
                ")";

        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    // Insert a task into the database
    public void addTask(Task task) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_TITLE, task.title);

            if (task.dueDate != null){
                values.put(KEY_TASK_DUE_DATE, task.getDueDate());
            }

            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch(Exception ex) {
            Log.d(TAG, "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }
    }

    // Get all tasks in the database
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();

        String TASKS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_TASKS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task newTask = new Task();
                    newTask.id = cursor.getLong(cursor.getColumnIndex(KEY_TASK_ID));
                    newTask.title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE));

                    newTask.setDueDate(cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE_DATE)));
                    tasks.add(newTask);
                }while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }

    // Update task
    public int updateTaskTitle(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_TITLE, task.title);

        if (task.dueDate != null){
            values.put(KEY_TASK_DUE_DATE, task.getDueDate());
        }

        return db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(task.id) });
    }

    // Delete task in the database
    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?",
                    new String[] { String.valueOf(task.id) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete task!");
        } finally {
            db.endTransaction();
        }
    }
}
