package com.hhua.android.simpletodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hhua.android.simpletodo.R;
import com.hhua.android.simpletodo.models.Task;

import java.util.List;

/**
 * Created by ahua on 9/23/15.
 */
public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        // Lookup view for data population
        TextView taskTitle = (TextView) convertView.findViewById(R.id.taskTitle);

        // Populate the data into the template view using the data object
        taskTitle.setText(task.title);

        // Return the completed view to render on screen
        return convertView;
    }

}
