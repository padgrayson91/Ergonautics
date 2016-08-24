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
    private static final String DATABASE_NAME = "ergonautics";
    private static final int DATABASE_VERSION = 1;

    private Realm mRealm;
    private RealmConfiguration mRealmConfig;
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
        mRealmConfig = new RealmConfiguration.Builder(mContext).build();
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

    //TODO: methods for updating entries

    //get methods


    public Cursor getAllTasks(){
        MatrixCursor result = new MatrixCursor(TasksTable.COLUMNS);
        for(Task t: mRealm.where(Task.class).findAll()){
            result.addRow(DBModelHelper.getTaskProperties(t));
        }
        return result;
    }


    //TODO
//    public Cursor getTasksForBoard(int boardLocalId){
//        SQLiteDatabase db = getReadableDatabase();
//        String subColumns = String.format("%s.%s, %s.%s, %s.%s",
//                TasksTable.TABLE_NAME, //1
//                TasksTable._ID,  //2
//                TasksTable.TABLE_NAME, //3
//                TasksTable.COLUMN_DISPLAY_NAME, //4
//                TasksTable.TABLE_NAME, //5
//                TasksTable.COLUMN_TASK_ID //3
//        );
//        //Fetching columns from: tasks
//        //Joining with: board task relation tables
//        //On: board task relation has task id (all relationships for each task)
//        //Filter by: relationship has board id provided
//        String query = String.format("SELECT %s FROM %s JOIN %s ON %s.%s=%s.%s WHERE %s.%s=%s",
//                subColumns, //1
//                TasksTable.TABLE_NAME, //2
//                TaskBoardRelationTable.TABLE_NAME, //3
//                TasksTable.TABLE_NAME, //4
//                TasksTable._ID, //5
//                TaskBoardRelationTable.TABLE_NAME, //6
//                TaskBoardRelationTable.COLUMN_TASK_LOCAL_ID, //7
//                TaskBoardRelationTable.TABLE_NAME, //8
//                TaskBoardRelationTable.COLUMN_BOARD_LOCAL_ID, //9
//                boardLocalId //10
//
//
//        );
//        Cursor cursor = db.rawQuery(query, null);
//        return cursor;
//    }

    public Cursor getAllBoards(){
        MatrixCursor result = new MatrixCursor(BoardsTable.COLUMNS);
        for(Board b: mRealm.where(Board.class).findAll()){
            result.addRow(DBModelHelper.getBoardProperties(b));
        }
        return result;
    }



}
