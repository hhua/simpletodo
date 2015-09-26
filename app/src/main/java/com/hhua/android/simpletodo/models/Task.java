package com.hhua.android.simpletodo.models;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahua on 9/21/15.
 */
public class Task {
    public long id;
    public String title;
    public Date dueDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final String TAG = "Task";

    public Task() {}

    public Task(String title){
        this.title = title;
    }

    public Task(String title, long id, Date dueDate){
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
    }

    public String getDueDate(){
        //this.dueDate.toString();
        if (this.dueDate != null)
            return dateFormat.format(this.dueDate);
        else
            return null;
    }

    public boolean setDueDate(String dueDate){
        if (dueDate != null) {
            try {
                this.dueDate = dateFormat.parse(dueDate);
                return true;
            }catch (Exception ex) {
                Log.d(TAG, "Error while saving due date!");
            }
        }

        return false;
    }
}
