package com.ergonautics.ergonautics.models;

import android.util.Log;

import com.ergonautics.ergonautics.ErgonautAPI;
import com.ergonautics.ergonautics.utils.ReflectionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/18/16.
 * Class to convert between Java model classes and JSON strings
 */
public class JsonModelHelper {
    private static final String TAG = "ERGONAUT-JSON";

    //JSON keys
    private static final String KEY_TASK = "task";
    private static final String KEY_TASK_NAME = "display_name";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_TASK_CREATED_AT = "created_at";
    private static final String KEY_TASK_STARTED_AT = "started_at";
    private static final String KEY_TASK_SCHEDULED_FOR = "scheduled_for";
    private static final String KEY_TASK_VALUE = "value";
    private static final String KEY_TASK_TIME_ESTIMATE = "time_estimate";
    private static final String KEY_TASK_STATUS = "status";
    private static final String KEY_BOARD_NAME = "display_name";
    private static final String KEY_USERNAME_LOGIN = "username";
    private static final String KEY_USERNAME_REGISTER = "username";
    private static final String KEY_PASSWORD_LOGIN = "password";
    private static final String KEY_PASSWORD_REGISTER = "password";
    private static final String KEY_BOARD_ID = "board_id";
    private static final String KEY_BOARD_TASKS = "tasks";
    private static final String KEY_BOARD_CREATED_AT = "created_at";
    private static final String KEY_BOARD_LAST_MODIFIED = "last_modified";
    private static final String KEY_SESSION_TOKEN = "token";
    private static final String KEY_ERROR_CODE = "error_code";
    private static final String KEY_GLOBAL_TASKS = "tasks";
    private static final String KEY_GLOBAL_BOARDS = "boards";
    private static final String [] TASK_KEYS = {KEY_TASK_NAME, KEY_TASK_ID, KEY_TASK_CREATED_AT, KEY_TASK_STARTED_AT,
        KEY_TASK_SCHEDULED_FOR, KEY_TASK_VALUE, KEY_TASK_TIME_ESTIMATE, KEY_TASK_STATUS};
    private static final String [] BOARD_KEYS = {KEY_BOARD_NAME, KEY_BOARD_ID, KEY_BOARD_TASKS, KEY_BOARD_CREATED_AT, KEY_BOARD_LAST_MODIFIED};

    /**
     *
     * @param t the task to parse
     * @return the requested Task as a JSON String
     */
    public static String getTaskAsJson(Task t) throws JSONException {
        JSONObject jobj = new JSONObject();
        for(String key: TASK_KEYS){
            try {
                Method getter = ReflectionUtils.getGetter(key, Task.class);
                jobj.put(key, getter.invoke(t));
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "getTaskAsJson: Unable to get getter method for " + key);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jobj.toString();
    }

    /**
     *
     * @param t the task we want to generate a request for
     * @param boardId the ID of the board where the task is being added
     * @return a JSON string representing the task
     */
    public static String getTaskRequestAsJson(Task t, String boardId) throws JSONException {
        String taskAsJson = getTaskAsJson(t);
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_TASK, new JSONObject(taskAsJson));
        jobj.put(KEY_BOARD_ID, boardId);
        return jobj.toString();
    }

    /**
     * Should only be called on a board whose tasks are synced with remote API
     * @param b the board to parse
     * @return the requested Board as a JSON String
     */
    public static String getBoardAsJson(Board b) throws JSONException {
        JSONObject jobj = new JSONObject();
        for(String key: BOARD_KEYS){
            //Special exception for tasks, because we only need the ids in the remote db
            if(key.equals(KEY_BOARD_TASKS)){
                JSONArray tasks = new JSONArray();
                for(Task t: b.getTasks()){
                    tasks.put(t.getTaskId());
                }
                jobj.put(KEY_BOARD_TASKS, tasks);
            } else {
                try {
                    Method getter = ReflectionUtils.getGetter(key, Board.class);
                    jobj.put(key, getter.invoke(b));
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "getBoardAsJson: Unable to get getter method for " + key);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return jobj.toString();
    }

    /**
     *
     * @param json the JSON String to parse
     * @return the task object parsed from the JSON String
     */
    public static Task getTaskFromJson(String json) throws JSONException {
        JSONObject jobj = new JSONObject(json);
        Task result = new Task(jobj.getString(KEY_TASK_NAME));
        for(String key: TASK_KEYS) {
            if (jobj.has(key)) {
                try {
                    Method setter = ReflectionUtils.getSetter(key, Task.class);
                    Class fieldType = setter.getParameterTypes()[0];

                    if(fieldType.equals(String.class)){
                        String val = jobj.getString(key);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                        int val = jobj.getInt(key);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                        //Long may be converted from double, so get as String and then remove any decimals
                        Object temp = jobj.get(key);
                        String tempStr = String.valueOf(temp);
                        String withoutDecimals = tempStr.split("\\.")[0];
                        long val = Long.valueOf(withoutDecimals);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                        double val = jobj.getDouble(key);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                        boolean val = jobj.getBoolean(key);
                        setter.invoke(result, val);
                    }
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "getTaskFromJson: invalid json key to setter conversion " + key);
                } catch (InvocationTargetException e) {
                    Log.e(TAG, "getTaskFromJson: unable to access setter for json conversion Task." + key);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "getTaskFromJson: setter is private or inaccessible Task." + key);
                }
            }
        }

        // NOTE: if Task has any complex data types added which are stored as JSONArrays/JSONObjects
        // These must be extracted outside of the reflective method

        return result;
    }

    /**
     *
     * @param json the JSON string containing a list of task objects
     * @return the parsed list of tasks
     */
    public static ArrayList<Task> getTasksFromJson(String json) throws JSONException {
        JSONObject jobj = new JSONObject(json);
        ArrayList<Task> result = new ArrayList<>();
        JSONArray tasksArray = jobj.getJSONArray(KEY_GLOBAL_TASKS);
        for(int i = 0; i < tasksArray.length(); i++){
            result.add(getTaskFromJson(tasksArray.getJSONObject(i).toString()));
        }
        return result;
    }

    /**
     *
     * @param json the JSON String to parse
     * @return the board object parsed from the JSON String wi
     */
    public static Board getBoardFromJson(String json) throws JSONException {
        JSONObject jobj = new JSONObject(json);
        Board result = new Board(jobj.getString(KEY_BOARD_NAME));
        for(String key: BOARD_KEYS) {
            if (jobj.has(key)) {
                try {
                    Method setter = ReflectionUtils.getSetter(key, Board.class);
                    Class fieldType = setter.getParameterTypes()[0];

                    if(fieldType.equals(String.class)){
                        String val = jobj.getString(key);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
                        int val = jobj.getInt(key);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
                        //Long may be converted from double, so get as String and then remove any decimals
                        Object temp = jobj.get(key);
                        String tempStr = String.valueOf(temp);
                        String withoutDecimals = tempStr.split("\\.")[0];
                        long val = Long.valueOf(withoutDecimals);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
                        double val = jobj.getDouble(key);
                        setter.invoke(result, val);
                    } else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)){
                        boolean val = jobj.getBoolean(key);
                        setter.invoke(result, val);
                    }
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "getBoardFromJson: invalid json key to setter conversion " + key);
                } catch (InvocationTargetException e) {
                    Log.e(TAG, "getBoardFromJson: unable to access setter for json conversion " + key);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "getBoardFromJson: setter is private or inaccessible " + key);
                }
            }
        }
        return result;
    }

    /**
     *
     * @param json a json string containing a list of board objects
     * @return the parsed list of board objects with no associated tasks
     */
    public static ArrayList<Board> getBoardsFromJson(String json) throws JSONException {
        JSONObject jobj = new JSONObject(json);
        ArrayList<Board> result = new ArrayList<>();
        JSONArray boardsArray = jobj.getJSONArray(KEY_GLOBAL_BOARDS);
        for(int i = 0; i < boardsArray.length(); i++){
            Board b = getBoardFromJson(boardsArray.getJSONObject(i).toString());
            result.add(b);
        }
        return result;
    }


    /**
     *
     * @param json the JSON string for the board to parse
     * @return the remote task IDs for each task as an ArrayList
     * @throws JSONException
     */
    public static ArrayList<String> getTaskIdsForBoardFromJson(String json) throws JSONException {
        JSONObject jobj = new JSONObject(json);
        JSONArray taskIds = jobj.getJSONArray(KEY_BOARD_TASKS);
        ArrayList<String> result = new ArrayList<>(taskIds.length());
        for(int i = 0; i < taskIds.length(); i++){
            result.add(taskIds.getString(i));
        }
        return result;
    }

    /**
     *
     * @param username the username to include in the returned JSON String
     * @param password the password to include in the returned JSON String
     * @return a JSON string representing the registration request
     */
    public static String getRegistrationAsJson(String username, String password) throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_USERNAME_REGISTER, username);
        jobj.put(KEY_PASSWORD_REGISTER, password);
        return jobj.toString();
    }


    /**
     * Convert a username and password to a JSON string which can be sent to the Ergonaut API
     * @param username the username provided for login
     * @param password the password provided for login
     * @return a JSON string with the appropriate data
     */
    public static String getLoginAsJson(String username, String password) throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_USERNAME_LOGIN, username);
        jobj.put(KEY_PASSWORD_LOGIN, password);
        return jobj.toString();
    }

    /**
     * Obtain a session token from a login attempt response
     * @param loginResponseAsJson the json string sent by the Ergonaut API
     * @return the session token as a string
     */
    public static String getTokenFromLogin(String loginResponseAsJson) throws JSONException {
        JSONObject jobj = new JSONObject(loginResponseAsJson);
        return jobj.getString(KEY_SESSION_TOKEN);
    }

    /**
     * Check for error messages in an API response and return the appropriate error code if one is present
     * @param response the API response
     * @return one of the error codes located in ErgonautAPI (or ErgonautAPI.SUCCESS if no error is present)
     */
    public static int getErrorCode(String response) throws JSONException {
        JSONObject jobj = new JSONObject(response);
        if(jobj.has(KEY_ERROR_CODE)){
            return jobj.getInt(KEY_ERROR_CODE);
        } else {
            return ErgonautAPI.SUCCESS;
        }

    }
}
