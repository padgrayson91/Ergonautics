package com.ergonautics.ergonautics.app;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.presenter.BoardPresenter;
import com.ergonautics.ergonautics.presenter.IPresenterCallback;
import com.ergonautics.ergonautics.view.BoardViewHolder;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 9/6/16.
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardViewHolder> implements IPresenterCallback, BoardViewHolder.IBoardViewHolderClickCallback{
    private static final String TAG = "ERGONAUT-ADAPT";
    private static BoardPresenter mPresenter;
    private ArrayList<Integer> mSelectedBoards;
    private BoardSelectedListener mListener;
    private int mLayoutResId;

    public BoardRecyclerAdapter(Context c, @LayoutRes  int layoutRes){
        if(mPresenter == null) {
            mPresenter = new BoardPresenter(c, this);
        }
        mLayoutResId = layoutRes;
    }

    public void addBoardSelectedListener(BoardSelectedListener listener){
        mListener = listener;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mSelectedBoards = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);
        View boardView = inflater.inflate(mLayoutResId, parent, false);
        BoardViewHolder viewHolder = new BoardViewHolder(boardView, context, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        boolean isSelected = mSelectedBoards.contains(position);
        holder.setViews(mPresenter.getData(position), isSelected);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getCount();
    }

    @Override
    public void notifyDataAdded(String id) {
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataUpdated() {
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataRemoved(Object data) {
        notifyDataSetChanged();
        //TODO: keep a copy of the removed board so it can be restored if needed
    }

    public void refresh(){
        mPresenter.refresh();
    }

    @Override
    public void onClickBoard(int position) {
        if(mListener != null){
            Board b = mPresenter.getData(position);
            mListener.onBoardSelected(b);
        }
    }

    @Override
    public void onLongClickBoard(int position) {
        if(mSelectedBoards.contains(position)){
            mSelectedBoards.remove(Integer.valueOf(position));
        } else {
            mSelectedBoards.add(position);
        }

    }

    public interface BoardSelectedListener {
        void onBoardSelected(Board b);
    }
}
