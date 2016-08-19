package com.ergonautics.ergonautics.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;

/**
 * Created by patrickgrayson on 8/18/16.
 * SQLite database helper for storing task and board data on device
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ergonautics";
    private static final int DATABASE_VERSION = 1;


    //For Tasks
    public static abstract class TasksTable implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_DISPLAY_NAME = "display_name"; //Name of task as displayed to user
        public static final String COLUMN_TASK_ID = "task_id"; //id of task obtained when it was added to the backend db

    }

    //For Boards
    public static abstract class BoardsTable implements BaseColumns {
        public static final String TABLE_NAME = "boards";
        public static final String COLUMN_DISPLAY_NAME = "display_name"; //Name of board as displayed to user
        public static final String COLUMN_BOARD_ID = "board_id"; //id of board obtained when it was added to the backend db
    }

    //For Relationships between boards and tasks: Many to many relationships are not easily mapped in SQlite
    public static abstract class TaskBoardRelationTable implements BaseColumns {
        public static final String TABLE_NAME = "tasks_to_boards";
        public static final String COLUMN_TASK_LOCAL_ID = "task_id";
        public static final String COLUMN_BOARD_LOCAL_ID = "board_id";
    }

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOARDS_TABLE = "CREATE TABLE " +
                BoardsTable.TABLE_NAME + "("
                + BoardsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BoardsTable.COLUMN_DISPLAY_NAME +  " TEXT,"
                + BoardsTable.COLUMN_BOARD_ID + " TEXT)";
        String CREATE_TASKS_TABLE = "CREATE TABLE " +
                TasksTable.TABLE_NAME + "("
                + TasksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TasksTable.COLUMN_DISPLAY_NAME + " TEXT,"
                + TasksTable.COLUMN_TASK_ID + " TEXT)";
        String CREATE_TASK_BOARD_RELATION_TABLE = "CREATE TABLE " +
                TaskBoardRelationTable.TABLE_NAME + "("
                + TaskBoardRelationTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TaskBoardRelationTable.COLUMN_BOARD_LOCAL_ID + " INTEGER,"
                + TaskBoardRelationTable.COLUMN_TASK_LOCAL_ID + " INTEGER)";
        db.execSQL(CREATE_BOARDS_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_TASK_BOARD_RELATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String DELETE_BOARDS = "DROP TABLE IF EXISTS " + BoardsTable.TABLE_NAME;
        String DELETE_TASKS = "DROP TABLE IF EXISTS " + TasksTable.TABLE_NAME;
        String DELETE_TASK_BOARD_RELATIONS = "DROP TABLE IF EXISTS " + TaskBoardRelationTable.TABLE_NAME;

        db.execSQL(DELETE_BOARDS);
        db.execSQL(DELETE_TASKS);
        db.execSQL(DELETE_TASK_BOARD_RELATIONS);
        onCreate(db);

    }

    //insert methods: return the LOCAL id of the added element

    /**
     *
     * @param taskVals ContentValues for the task to add
     * @return the local id of the added task
     */
    public long addTask(ContentValues taskVals) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TasksTable.TABLE_NAME, null, taskVals);
    }

    public long addBoardTaskRelationship(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TaskBoardRelationTable.TABLE_NAME, null, values);
    }

    public long addBoard(ContentValues vals) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(BoardsTable.TABLE_NAME, null, vals);
    }

    //TODO: methods for updating entries

    //get methods: return model objects for a single get, or cursors for range get

    @Nullable
    public Task getTaskByLocalId(int localId){
        SQLiteDatabase db = getReadableDatabase();
        String subColumns = String.format("%s, %s, %s",
                TasksTable._ID,  //1
                TasksTable.COLUMN_DISPLAY_NAME, //2
                TasksTable.COLUMN_TASK_ID //3
        );

        String query = String.format("SELECT %s FROM %s WHERE %s.%s=%d",
                subColumns, //1
                TasksTable.TABLE_NAME, //2
                TasksTable.TABLE_NAME, //3
                TasksTable._ID, //4
                localId //5
                );
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Task result = getTaskFromCursor(cursor);
        cursor.close();
        return result;
    }

    @Nullable
    public Board getBoardByLocalId(int localId){
        SQLiteDatabase db = getReadableDatabase();
        String subColumns = String.format("%s, %s, %s",
                BoardsTable._ID,  //1
                BoardsTable.COLUMN_DISPLAY_NAME, //2
                BoardsTable.COLUMN_BOARD_ID //3
        );

        String query = String.format("SELECT %s FROM %s WHERE %s.%s=%d",
                subColumns, //1
                BoardsTable.TABLE_NAME, //2
                BoardsTable.TABLE_NAME, //3
                BoardsTable._ID, //4
                localId //5
        );
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Board result = getBoardFromCursor(cursor);
        cursor.close();
        cursor = getTasksForBoard(localId);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            result.addTask(getTaskFromCursor(cursor));
        }

        return result;
    }

    public Cursor getAllTasks(){
        SQLiteDatabase db = getReadableDatabase();
        String subColumns = String.format("%s, %s, %s",
                TasksTable._ID,  //1
                TasksTable.COLUMN_DISPLAY_NAME, //2
                TasksTable.COLUMN_TASK_ID //3
        );

        String query = String.format("SELECT %s FROM %s",
                subColumns, //1
                TasksTable.TABLE_NAME //2
        );
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    public Cursor getTasksForBoard(int boardLocalId){
        SQLiteDatabase db = getReadableDatabase();
        String subColumns = String.format("%s.%s, %s.%s, %s.%s",
                TasksTable.TABLE_NAME, //1
                TasksTable._ID,  //2
                TasksTable.TABLE_NAME, //3
                TasksTable.COLUMN_DISPLAY_NAME, //4
                TasksTable.TABLE_NAME, //5
                TasksTable.COLUMN_TASK_ID //3
        );
        //Fetching columns from: tasks
        //Joining with: board task relation tables
        //On: board task relation has task id (all relationships for each task)
        //Filter by: relationship has board id provided
        String query = String.format("SELECT %s FROM %s JOIN %s ON %s.%s=%s.%s WHERE %s.%s=%s",
                subColumns, //1
                TasksTable.TABLE_NAME, //2
                TaskBoardRelationTable.TABLE_NAME, //3
                TasksTable.TABLE_NAME, //4
                TasksTable._ID, //5
                TaskBoardRelationTable.TABLE_NAME, //6
                TaskBoardRelationTable.COLUMN_TASK_LOCAL_ID, //7
                TaskBoardRelationTable.TABLE_NAME, //8
                TaskBoardRelationTable.COLUMN_BOARD_LOCAL_ID, //9
                boardLocalId //10


        );
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllBoards(){
        SQLiteDatabase db = getReadableDatabase();
        String subColumns = String.format("%s, %s, %s",
                BoardsTable._ID,  //1
                BoardsTable.COLUMN_DISPLAY_NAME, //2
                BoardsTable.COLUMN_BOARD_ID //3
        );

        String query = String.format("SELECT %s FROM %s",
                subColumns, //1
                BoardsTable.TABLE_NAME //2
        );
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    //methods to convert DB entries to model objects

    /**
     * Creates a task object from the current row of the cursor.  This will not move the cursor
     * @param c the cursor, which should have been pulled from the "tasks" table
     * @return a Task object with appropriate values pulled from the cursor
     */
    public Task getTaskFromCursor(Cursor c){
        String displayName = c.getString(c.getColumnIndex(TasksTable.COLUMN_DISPLAY_NAME));
        Task t = new Task(displayName);
        String taskId = c.getString(c.getColumnIndex(TasksTable.COLUMN_TASK_ID));
        int localId = c.getInt(c.getColumnIndex(TasksTable._ID));
        t.setTaskId(taskId);
        t.setLocalId(localId);
        return t;
    }

    /**
     * Creates a board object from the current row of the cursor. This will not move the cursor
     * @param c the cursor, which should have been pulled from the "boards" table
     * @return a Board object with appropriate values pulled from the cursor, but with an empty task list
     */
    public Board getBoardFromCursor(Cursor c){
        String displayName = c.getString(c.getColumnIndex(BoardsTable.COLUMN_DISPLAY_NAME));
        Board b = new Board(displayName);
        String boardId = c.getString(c.getColumnIndex(BoardsTable.COLUMN_BOARD_ID));
        int localId = c.getInt(c.getColumnIndex(BoardsTable._ID));
        b.setBoardId(boardId);
        b.setLocalId(localId);
        return b;
    }



}
