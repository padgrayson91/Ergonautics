package com.ergonautics.ergonautics.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.IBoardListUpdateListener;
import com.ergonautics.ergonautics.models.Board;

/**
 * Created by patrickgrayson on 9/7/16.
 */
public class AddBoardFragment extends Fragment {
    private static final String TAG = "ERGONAUT-BRDDLG";
    private IBoardListUpdateListener mBoardSubmissionListener;
    private EditText mBoardNameEdit;
    private Button mBoardSubmissionButton;
    private Board toSubmit;

    public AddBoardFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TODO: check if we are updating from an existing board, and if so set all of our data from that
        try {
            mBoardSubmissionListener = (IBoardListUpdateListener) getActivity();
        } catch (ClassCastException ex){
            throw new ClassCastException("Activity containing AddBoardFragment must implement IBoardListUpdateListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: should be storing this in onPause
        toSubmit = new Board();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_board, container, false);
        mBoardNameEdit = (EditText) root.findViewById(R.id.edit_board_name);
        mBoardSubmissionButton = (Button) root.findViewById(R.id.button_board_submit);
        mBoardSubmissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSubmit.setDisplayName(mBoardNameEdit.getText().toString());
                mBoardSubmissionListener.onBoardSubmitted(toSubmit);
            }
        });

        return root;
    }
}
