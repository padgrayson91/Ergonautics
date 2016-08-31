package com.ergonautics.ergonautics.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class ErgonautContentProvider extends ContentProvider {
    private static final String TAG = "ERGONAUT-CP";
    private static final String AUTHORITY = "com.ergonautics.ergonautics.ErgonautContentProvider";
    private static final String TASKS_TABLE = "tasks";
    private static final String BOARDS_TABLE = "boards";
    private static final String PREFIX = "content://";
    private static final String TASKS_INSERT_PATH = BOARDS_TABLE + "/*/"  + TASKS_TABLE;
    private static final String BOARDS_INSERT_PATH = BOARDS_TABLE;
    private static final String TASKS_QUERY_PATH = TASKS_TABLE;
    private static final String BOARDS_QUERY_PATH = BOARDS_TABLE;
    //This should only be accessed through the UriHelper since it needs to be manipulated before being used
    protected static final Uri TASKS_INSERT_URI = Uri.parse(PREFIX
            + AUTHORITY + "/" + TASKS_INSERT_PATH);
    public static final Uri BOARDS_INSERT_URI = Uri.parse(PREFIX
            + AUTHORITY + "/" + BOARDS_INSERT_PATH);
    public static final Uri TASKS_QUERY_URI = Uri.parse(PREFIX
            + AUTHORITY + "/" + TASKS_QUERY_PATH);
    public static final Uri BOARDS_QUERY_URI = Uri.parse(PREFIX
            + AUTHORITY + "/" + BOARDS_QUERY_PATH);

    private static final int TASKS = 1;
    private static final int BOARDS = 2;
    private static final int TASKS_FOR_BOARD = 3;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, TASKS_TABLE, TASKS);
        sURIMatcher.addURI(AUTHORITY, BOARDS_TABLE, BOARDS);
        sURIMatcher.addURI(AUTHORITY, BOARDS_TABLE + "/*/" + TASKS_TABLE, TASKS_FOR_BOARD);
        sURIMatcher.addURI(AUTHORITY, TASKS_TABLE + "/*", TASKS);
        sURIMatcher.addURI(AUTHORITY, BOARDS_TABLE + "/*", BOARDS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        DBHelper db = new DBHelper(getContext());
        int result = 0;
        switch (sURIMatcher.match(uri)){
            case TASKS:
                String taskID = uri.getLastPathSegment();
                result = db.deleteTaskById(taskID);
                break;
            case BOARDS:
                String boardId = uri.getLastPathSegment();
                result = db.deleteBoardById(boardId);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri.toString());
        }
        return result;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        //TODO: Implement this to handle requests to get the MIME type for a row.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        //Check which model we are using and perform the insertion accordingly
        String id = "";
        DBHelper db = new DBHelper(getContext());
        switch (sURIMatcher.match(uri)){
            case TASKS_FOR_BOARD:
                List<String> segments = uri.getPathSegments();
                Log.d(TAG, "insert: segments " + segments.toString());
                String boardId = segments.get(1);
                id = db.createTask(values, boardId);
                break;
            case BOARDS:
                id = db.createBoard(values);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri.toString());
        }
        Log.d(TAG, "insert: value inserted with id " + id);
        return Uri.withAppendedPath(uri, "/" + id);
    }

    @Override
    //TODO: support querying by other parameters
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        DBHelper db = new DBHelper(getContext());
        //Check which model we are using and perform the insertion accordingly
        Log.d(TAG, "query: Got request for data at URI " + uri.toString());
        switch (sURIMatcher.match(uri)){
            case TASKS:
                Log.d(TAG, "query: segments " + uri.getPathSegments().toString());
                if(uri.getLastPathSegment().equals(TASKS_TABLE)) {
                    //Request for all tasks
                    result = db.getAllTasks();
                } else {
                    String id = uri.getLastPathSegment();
                    result = db.getTaskById(id);
                }
                break;
            case BOARDS:
                if(uri.getLastPathSegment().equals(BOARDS_TABLE)){
                    //Request for all boards
                    result = db.getAllBoards();
                } else {
                    String id = uri.getLastPathSegment();
                    result = db.getBoardById(id);
                }
                break;
            case TASKS_FOR_BOARD:
                List<String> segments = uri.getPathSegments();
                String boardId = segments.get(1);
                result = db.getTasksForBoard(boardId);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri.toString());
        }
        return result;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
