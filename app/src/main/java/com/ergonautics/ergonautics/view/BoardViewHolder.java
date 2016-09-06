package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.models.Board;

/**
 * Created by patrickgrayson on 9/6/16.
 */
public class BoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private Context mContext;
    private IBoardViewHolderClickCallback mCallback;
    private TextView mDisplayNameText;
    private TextView mTaskCountText;

    public BoardViewHolder(View itemView, Context c, IBoardViewHolderClickCallback callback) {
        super(itemView);
        mContext = c;
        mCallback = callback;
        mDisplayNameText = (TextView) itemView.findViewById(R.id.text_display_name);
        mTaskCountText = (TextView) itemView.findViewById(R.id.text_task_count);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    //TODO: use the parameter `selected` to change the background color of the whole view
    public void setViews(Board b, boolean selected){
        mDisplayNameText.setText(b.getDisplayName());
        mTaskCountText.setText(String.valueOf(b.getTasks().size()));
    }

    @Override
    public void onClick(View view) {
        mCallback.onClickBoard(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        mCallback.onLongClickBoard(getAdapterPosition());
        return true;
    }

    public interface IBoardViewHolderClickCallback {
        void onClickBoard(int position);
        void onLongClickBoard(int position);
    }
}
