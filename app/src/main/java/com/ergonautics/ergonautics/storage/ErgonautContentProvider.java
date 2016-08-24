package com.ergonautics.ergonautics.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class ErgonautContentProvider extends ContentProvider {
    private static final String TAG = "ERGONAUT-CP";
    private static final String AUTHORITY = "com.ergonautics.ergonautics.ErgonautContentProvider";
    private static final String TASKS_TABLE = "tasks";
    private static final String BOARDS_TABLE = "boards";
    public static final Uri TASKS_INSERT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + BOARDS_TABLE + "/*/"  + TASKS_TABLE);
    public static final Uri BOARDS_INSERT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + BOARDS_TABLE);

    public static final int TASKS = 1;
    public static final int BOARDS = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BOARDS_TABLE + "/*/" + TASKS_TABLE, TASKS);
        sURIMatcher.addURI(AUTHORITY, BOARDS_TABLE, BOARDS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Check which model we are using and perform the insertion accordingly
        String id = "";
        DBHelper db = new DBHelper(getContext());
        switch (sURIMatcher.match(uri)){
            case TASKS:
                List<String> segments = uri.getPathSegments();
                Log.d(TAG, "insert: " + segments.toString());
                String boardId = segments.get(1);
                db.createTask(values, boardId);
                break;
            case BOARDS:
                db.createBoard(values);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri.toString());
        }
        return Uri.withAppendedPath(uri, "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        DBHelper db = new DBHelper(getContext());
        //Check which model we are using and perform the insertion accordingly
        Log.d(TAG, "query: Got request for data at URI " + uri.toString());
        switch (sURIMatcher.match(uri)){
            case TASKS:
                result = db.getAllTasks();
                break;
            case BOARDS:
                result = db.getAllBoards();
                break;
            default:
                throw new IllegalArgumentException("Invalid URI " + uri.toString());
        }
        return result;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
