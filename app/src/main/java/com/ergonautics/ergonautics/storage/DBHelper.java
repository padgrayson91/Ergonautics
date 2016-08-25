package com.ergonautics.ergonautics.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by patrickgrayson on 8/18/16.
 * Realm database for persistent storage on device to allow less frequent API communication
 * I chose Realm because the initial structure using SQLite was not conducive to the many-to-many
 * Relationship of boards to tasks
 */
public class DBHelper {
    private static final String TAG = "ERGONAUT-DB";

    private static final String DATABASE_NAME = "ergonautics";
    private static final int DATABASE_VERSION = 1;

    private Realm mRealm;
    private Context mContext;


    //For Tasks
    public static abstract class TasksTable {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_DISPLAY_NAME = "displayName"; //Name of task as displayed to user
        public static final String COLUMN_TASK_ID = "taskId"; //id of task obtained when it was added to the backend db

        //NOTE: if adding a column, you must also update some methods in DBModelHelper
        public static final String [] COLUMNS = {COLUMN_TASK_ID, COLUMN_DISPLAY_NAME};

    }

    //For Boards
    public static abstract class BoardsTable {
        public static final String TABLE_NAME = "boards";
        public static final String COLUMN_DISPLAY_NAME = "displayName"; //Name of board as displayed to user
        public static final String COLUMN_BOARD_ID = "boardId"; //id of board obtained when it was added to the backend db
        public static final String COLUMN_TASKS = "tasks";

        //NOTE: if adding a column, you must also update some methods in DBModelHelper
        public static final String [] COLUMNS = {COLUMN_BOARD_ID, COLUMN_DISPLAY_NAME, COLUMN_TASKS};

    }

    public DBHelper(Context context) {
        mContext = context;
        RealmConfiguration mRealmConfig = new RealmConfiguration.Builder(mContext).build();
        mRealm = Realm.getInstance(mRealmConfig);
    }


    //insert methods

    /**
     *
     * @param taskVals ContentValues for the task to add
     */
    public String createTask(final ContentValues taskVals, final String boardId) {
        //Since this is a new task, it needs a new id
        String id = UUID.randomUUID().toString();
        taskVals.put(TasksTable.COLUMN_TASK_ID, id);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task t = realm.createObject(Task.class);
                DBModelHelper.getTaskFromContentValues(taskVals, t);
            }
        });
        addTaskToBoard(boardId, taskVals);
        return id;

    }

    public String createBoard(final ContentValues vals) {
        //Since this is a new board, it needs a new id
        String id = UUID.randomUUID().toString();
        vals.put(BoardsTable.COLUMN_BOARD_ID, id);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Board b = realm.createObject(Board.class);
                DBModelHelper.getBoardFromContentValues(vals, b);
            }
        });
        return id;
    }

    //Methods for updating entries

    public void updateTask(final ContentValues vals) {
        final Task t = mRealm.where(Task.class).equalTo(TasksTable.COLUMN_TASK_ID, vals.getAsString(TasksTable.COLUMN_TASK_ID)).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DBModelHelper.getTaskFromContentValues(vals, t);
            }
        });
    }

    public void updateBoard(final ContentValues vals) {
        final Board b = mRealm.where(Board.class).equalTo(BoardsTable.COLUMN_BOARD_ID, vals.getAsString(BoardsTable.COLUMN_BOARD_ID)).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DBModelHelper.getBoardFromContentValues(vals, b);
            }
        });
    }

    public void addTaskToBoard(String boardId, final ContentValues taskVals){
        final Board b = mRealm.where(Board.class).equalTo(BoardsTable.COLUMN_BOARD_ID, boardId).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task t = new Task();
                DBModelHelper.getTaskFromContentValues(taskVals, t);
                b.addTask(t);
            }
        });
    }

    //get methods


    public Cursor getAllTasks(){
        MatrixCursor result = new MatrixCursor(TasksTable.COLUMNS);
        for(Task t: mRealm.where(Task.class).findAll()){
            result.addRow(DBModelHelper.getTaskProperties(t));
        }
        return result;
    }


    public Cursor getTasksForBoard(String boardId){
        Board b = mRealm.where(Board.class).equalTo(BoardsTable.COLUMN_BOARD_ID, boardId).findFirst();
        MatrixCursor result = new MatrixCursor(TasksTable.COLUMNS);
        for(Task t: b.getTasks()){
            result.addRow(DBModelHelper.getTaskProperties(t));
        }
        return result;
    }

    public Cursor getAllBoards(){
        MatrixCursor result = new MatrixCursor(BoardsTable.COLUMNS);
        for(Board b: mRealm.where(Board.class).findAll()){
            result.addRow(DBModelHelper.getBoardProperties(b));
        }
        return result;
    }

    public Cursor getBoardById(String boardId){
        Board b = mRealm.where(Board.class).equalTo(BoardsTable.COLUMN_BOARD_ID, boardId).findFirst();
        MatrixCursor result = new MatrixCursor(BoardsTable.COLUMNS);
        result.addRow(DBModelHelper.getBoardProperties(b));
        return result;
    }

    public Cursor getTaskById(String taskId){
        Task t = mRealm.where(Task.class).equalTo(TasksTable.COLUMN_TASK_ID, taskId).findFirst();
        MatrixCursor result = new MatrixCursor(TasksTable.COLUMNS);
        result.addRow(DBModelHelper.getTaskProperties(t));
        return result;
    }

    //delete methods

    public int deleteBoardById(String boardId){
        try {
            //TODO: before deleting a board, delete all tasks associated with only that board
            mRealm.where(Board.class).equalTo(BoardsTable.COLUMN_BOARD_ID, boardId).findFirst().deleteFromRealm();
            return 1;
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public int deleteTaskById(String taskId){
        try {
            mRealm.where(Task.class).equalTo(TasksTable.COLUMN_TASK_ID, taskId).findFirst().deleteFromRealm();
            return 1;
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public void clearDb(){
        mRealm.deleteAll();
    }



}
