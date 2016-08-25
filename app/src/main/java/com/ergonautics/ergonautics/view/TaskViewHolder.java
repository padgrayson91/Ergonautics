package com.ergonautics.ergonautics.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.models.Task;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TaskViewHolder extends RecyclerView.ViewHolder {
    private TextView mDisplayNameText;
    public TaskViewHolder(View itemView) {
        super(itemView);
        mDisplayNameText = (TextView) itemView.findViewById(R.id.text_display_name);
    }

    public void setViews(Task t){
        mDisplayNameText.setText(t.getDisplayName());
    }
}
