package com.ergonautics.ergonautics.app;

import com.ergonautics.ergonautics.models.Task;

/**
 * Created by patrickgrayson on 8/30/16.
 */
public interface ITaskListUpdateListener {

    void onAddTaskSelected();
    void onTaskSubmitted(Task t);
}
