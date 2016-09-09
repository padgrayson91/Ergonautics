package com.ergonautics.ergonautics.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.ergonautics.ergonautics.ErgonautAPI;
import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.app.BoardRecyclerAdapter;
import com.ergonautics.ergonautics.app.MainPageAdapter;
import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;
import com.ergonautics.ergonautics.storage.LocalStorage;
import com.ergonautics.ergonautics.storage.UriHelper;

import java.util.ArrayList;

/**
 * App main page where user can view active tasks, current boards, notifications, etc.
 */
public class MainActivity extends AppCompatActivity implements BoardRecyclerAdapter.BoardSelectedListener {
    private static final String TAG = "ERGONAUT-MAIN";

    //Request codes for starting activities
    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final int REQUEST_CODE_ADD_TASK = 1002;
    private static final int REQUEST_CODE_ADD_BOARD = 1003;

    private TabOnlyViewPager mPager;
    private MainPageAdapter mPageAdapter;
    private TabLayout mTabs;
    private FloatingActionButton mAddButton;
    private Button mRecyclerAddButton;
    private RecyclerView mDrawerRecycler;
    private BoardRecyclerAdapter mDrawerAdapter;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if user is logged in
        ErgonautAPI api = new ErgonautAPI(this);
        mPager = (TabOnlyViewPager) findViewById(R.id.pager_main);
        mPageAdapter = new MainPageAdapter(getSupportFragmentManager(), getFragmentsForPageAdapter());
        mPager.setAdapter(mPageAdapter);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_main);
        mTabs = (TabLayout) findViewById(R.id.tabs_main);
        mTabs.setupWithViewPager(mPager);
        mDrawerRecycler = (RecyclerView) findViewById(R.id.recycler_main_drawer);
        mDrawerRecycler.setLayoutManager(new LinearLayoutManager(this));
        mDrawerAdapter = new BoardRecyclerAdapter(this);
        mDrawerAdapter.addBoardSelectedListener(this);
        mDrawerRecycler.setAdapter(mDrawerAdapter);
        mRecyclerAddButton = (Button) findViewById(R.id.button_create_new_board);
        mRecyclerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNew(view);
            }
        });
        if(!api.isLoggedIn()){
            //If the user is not logged in, go to the login activity
            switchToLoginActivity();
        } else {
            switchToLandingPage();
        }
        mAddButton = (FloatingActionButton) findViewById(R.id.button_create_new);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNew(view);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_LOGIN:
                if(resultCode == RESULT_OK){
                    //Login was successful
                    switchToLandingPage();
                } else {
                    //User hit back
                    finish();
                }
                break;
            case REQUEST_CODE_ADD_TASK:
                LocalStorage storage = LocalStorage.getInstance(this);
                storage.setTaskInProgress(null);
                mDrawerAdapter.refresh();
                switchToTaskList(storage.getSelectedBoard().getBoardId());
                break;
            case REQUEST_CODE_ADD_BOARD:
                mDrawerAdapter.refresh();
                break;
            default:
                Log.d(TAG, "onActivityResult: unknown activity result");
        }
    }

    private void switchToLandingPage(){
        //TODO: switch to different pages based on user preferences
        mPager.setCurrentItem(MainPageAdapter.PAGE_TASKS);
    }

    //Show a task list for a specific board or (if forBoard is null) show all tasks in the db
    private void switchToTaskList(@Nullable String forBoard){
        String queryString;
        if(forBoard == null){
            queryString = ErgonautContentProvider.TASKS_QUERY_URI.toString();
        } else {
            queryString = UriHelper.getTaskForBoardQueryUri(forBoard).toString();
        }
        try {
            TaskListFragment tlf = (TaskListFragment) mPageAdapter.getItem(MainPageAdapter.PAGE_TASKS);
            tlf.newQuery(queryString);
            mPageAdapter.notifyDataSetChanged();
        } catch (ClassCastException ex) {
            Log.e(TAG, "Main pager order was incorrect!");
        }
        mPager.setCurrentItem(MainPageAdapter.PAGE_TASKS);
    }

    private void switchToLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
    }

    private ArrayList<Fragment> getFragmentsForPageAdapter(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        //TODO: get all fragments that should be used for the main page
        String queryString = ErgonautContentProvider.TASKS_QUERY_URI.toString();
        TaskListFragment taskListFragment = TaskListFragment.newInstance(queryString);
        fragments.add(taskListFragment);
        return fragments;
    }

    public void createNew(View selected) {
        if(selected.getId() == R.id.button_create_new) {
            switch (mPager.getCurrentItem()) {
                case MainPageAdapter.PAGE_TASKS:
                    Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                    startActivityForResult(addTaskIntent, REQUEST_CODE_ADD_TASK);
                    break;
            }
        } else if(selected.getId() == R.id.button_create_new_board) {
            Intent addBoardIntent = new Intent(MainActivity.this, AddBoardActivity.class);
            startActivityForResult(addBoardIntent, REQUEST_CODE_ADD_BOARD);
        }
    }

    @Override
    public void onBoardSelected(Board b) {
        switchToTaskList(b.getBoardId());
        LocalStorage.getInstance(this).setSelectedBoard(b);
        mDrawer.closeDrawer(Gravity.LEFT);
        //TODO set the page title to the selected board
    }
}
