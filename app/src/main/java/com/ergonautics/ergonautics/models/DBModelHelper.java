package com.ergonautics.ergonautics.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.ergonautics.ergonautics.storage.DBHelper;
import com.ergonautics.ergonautics.storage.Serializer;
import com.ergonautics.ergonautics.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.realm.RealmList;

/**
 * Created by patrickgrayson on 8/19/16.
 * class to help convert between model classes and local db entries
 */
public class DBModelHelper {
    private static final String TAG = "ERGONAUT-DB";

    public static ContentValues getContentValuesForTask(Task t){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.TasksTable.COLUMN_DISPLAY_NAME, t.getDisplayName());
        cv.put(DBHelper.TasksTable.COLUMN_TASK_ID, t.getTaskId());
        Log.d(TAG, "getContentValuesForTask: storing values for " + t.getDisplayName());

        for(String key: DBHelper.TasksTable.COLUMNS){
            try {
                Method getter = ReflectionUtils.getGetter(key, Task.class);
                Class fieldType = getter.getReturnType();
                if(fieldType.equals(String.class)){
                    String val = (String) getter.invoke(t);
                    cv.put(key, val);
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                    int val = (int) getter.invoke(t);
                    cv.put(key, val);
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                    long val = (long) getter.invoke(t);
                    cv.put(key, val);
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                    double val = (double) getter.invoke(t);
                    cv.put(key, val);
                } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                    boolean val = (boolean) getter.invoke(t);
                    cv.put(key, val);
                }
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "getContentValuesForTask: no getter! " + key);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return cv;
    }

    public static ContentValues getContentValuesForBoard(Board b){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.BoardsTable.COLUMN_DISPLAY_NAME, b.getDisplayName());
        cv.put(DBHelper.BoardsTable.COLUMN_BOARD_ID, b.getBoardId());
        cv.put(DBHelper.BoardsTable.COLUMN_TASKS, Serializer.serialize(b.getTasks()));


        for(String key: DBHelper.BoardsTable.COLUMNS){
            try {
                Method getter = ReflectionUtils.getGetter(key, Board.class);
                Class fieldType = getter.getReturnType();
                if(fieldType.equals(String.class)){
                    String val = (String) getter.invoke(b);
                    cv.put(key, val);
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                    int val = (int) getter.invoke(b);
                    cv.put(key, val);
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                    long val = (long) getter.invoke(b);
                    cv.put(key, val);
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                    double val = (double) getter.invoke(b);
                    cv.put(key, val);
                } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                    boolean val = (boolean) getter.invoke(b);
                    cv.put(key, val);
                }
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "getContentValuesForTask: no getter! " + key);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return cv;
    }

    //NOTE: This should always match the order of DBHelper.TasksTable.COLUMNS!
    public static Object[] getTaskProperties(Task t){
        Object [] props = new Object[DBHelper.TasksTable.COLUMNS.length];
        for(int i = 0; i < props.length; i++){
            try {
                props[i] = ReflectionUtils.getGetter(DBHelper.TasksTable.COLUMNS[i], Task.class).invoke(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return props;
    }

    //NOTE: This should always match the order of DBHelper.BoardsTable.COLUMNS!
    public static Object[] getBoardProperties(Board b) {
        Object [] props = new Object[DBHelper.BoardsTable.COLUMNS.length];
        props[0] = b.getBoardId();
        props[1] = b.getDisplayName();
        props[2] = Serializer.serialize(b.getTasks()); //Tasks need to be accessed explicitly so we can serialize them
        //further fields can be accessed via reflection to avoid the need to update this method
        for(int i = 3; i < DBHelper.BoardsTable.COLUMNS.length; i++){
            try {
                props[i] = ReflectionUtils.getGetter(DBHelper.BoardsTable.COLUMNS[i], Board.class).invoke(b);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    public static Task getTaskFromContentValues(ContentValues cv, Task toUpdate){
        Log.d(TAG, "getTaskFromContentValues: setting values for " + toUpdate.getDisplayName());
        for(String key: DBHelper.TasksTable.COLUMNS){
            if(!cv.containsKey(key)){
                throw new IllegalArgumentException("Contentvalues is missing value for " + key);
            }
            try {
                Method setter = ReflectionUtils.getSetter(key, Task.class);
                Class fieldType = setter.getParameterTypes()[0];

                if(fieldType.equals(String.class)){
                    String val = cv.getAsString(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                    int val = cv.getAsInteger(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                    long val = cv.getAsLong(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                    double val = cv.getAsDouble(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                    boolean val = cv.getAsBoolean(key);
                    setter.invoke(toUpdate, val);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException ex) {
                throw new NullPointerException("Null: " + key);
            }
        }
        return toUpdate;
    }

    public static Board getBoardFromContentValues(ContentValues cv, Board toUpdate){
        for(String key: DBHelper.BoardsTable.COLUMNS){
            if(!cv.containsKey(key)){
                throw new IllegalArgumentException("Contentvalues is missing value for " + key);
            }
            try {
                Method setter = ReflectionUtils.getSetter(key, Board.class);
                Class fieldType = setter.getParameterTypes()[0];

                if(fieldType.equals(String.class)){
                    String val = cv.getAsString(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                    int val = cv.getAsInteger(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                    long val = cv.getAsLong(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                    double val = cv.getAsDouble(key);
                    setter.invoke(toUpdate, val);
                } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                    boolean val = cv.getAsBoolean(key);
                    setter.invoke(toUpdate, val);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException ex) {
                throw new NullPointerException("Null: " + key);
            }
        }
        //Tasks list can't be extracted reflectively so we need to do it explicitly
        try {
            RealmList<Task> tasks = (RealmList<Task>) Serializer.deserialize(cv.getAsByteArray(DBHelper.BoardsTable.COLUMN_TASKS));
            for (Task t : tasks) {
                toUpdate.addTask(t);
            }
        } catch (NullPointerException ignored){}

        return toUpdate;
    }

    //methods to convert DB entries to model objects

    /**
     * Creates a task object from the current row of the cursor.  This will not move the cursor
     * @param c the cursor, which should have been pulled from the "tasks" table
     * @return a Task object with appropriate values pulled from the cursor
     */
    public static Task getTaskFromCursor(Cursor c){
        String displayName = c.getString(c.getColumnIndex(DBHelper.TasksTable.COLUMN_DISPLAY_NAME));
        Task t = new Task(displayName);
        Log.d(TAG, "getTaskFromCursor: Setting values for " + displayName);
        for(String key: DBHelper.TasksTable.COLUMNS){
            try {
                Method setter = ReflectionUtils.getSetter(key, Task.class);
                Class fieldType = setter.getParameterTypes()[0];

                if(fieldType.equals(String.class)){
                    String val = c.getString(c.getColumnIndex(key));
                    setter.invoke(t, val);
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                    int val = c.getInt(c.getColumnIndex(key));
                    setter.invoke(t, val);
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                    long val = c.getLong(c.getColumnIndex(key));
                    setter.invoke(t, val);
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                    double val = c.getDouble(c.getColumnIndex(key));
                    setter.invoke(t, val);
                } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                    boolean val = Boolean.valueOf(c.getString(c.getColumnIndex(key)));
                    setter.invoke(t, val);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String taskId = c.getString(c.getColumnIndex(DBHelper.TasksTable.COLUMN_TASK_ID));
        t.setTaskId(taskId);
        return t;
    }

    /**
     * Creates a board object from the current row of the cursor. This will not move the cursor
     * @param c the cursor, which should have been pulled from the "boards" table
     * @return a Board object with appropriate values pulled from the cursor, but with an empty task list
     */
    public static Board getBoardFromCursor(Cursor c){
        String displayName = c.getString(c.getColumnIndex(DBHelper.BoardsTable.COLUMN_DISPLAY_NAME));
        Board b = new Board(displayName);
        for(String key: DBHelper.BoardsTable.COLUMNS){
            try {
                Method setter = ReflectionUtils.getSetter(key, Board.class);
                Class fieldType = setter.getParameterTypes()[0];

                if(fieldType.equals(String.class)){
                    String val = c.getString(c.getColumnIndex(key));
                    setter.invoke(b, val);
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                    int val = c.getInt(c.getColumnIndex(key));
                    setter.invoke(b, val);
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                    long val = c.getLong(c.getColumnIndex(key));
                    setter.invoke(b, val);
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                    double val = c.getDouble(c.getColumnIndex(key));
                    setter.invoke(b, val);
                } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                    boolean val = Boolean.valueOf(c.getString(c.getColumnIndex(key)));
                    setter.invoke(b, val);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        RealmList<Task> tasks;
        try {
            tasks = (RealmList<Task>) Serializer.deserialize(c.getBlob(c.getColumnIndex(DBHelper.BoardsTable.COLUMN_TASKS)));
        } catch (NullPointerException ex){
            tasks = new RealmList<>();
        }
        for(Task t: tasks){
            b.addTask(t);
        }

        return b;
    }
}
