package com.ergonautics.ergonautics;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by patrickgrayson on 8/18/16.
 * Class to convert between Java model classes and JSON strings
 */
public class JsonModelHelper {
    //JSON keys
    private static final String KEY_TASK = "task";
    private static final String KEY_TASK_NAME = "display_name";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_BOARD_NAME = "display_name";
    private static final String KEY_USERNAME_LOGIN = "username";
    private static final String KEY_USERNAME_REGISTER = "username";
    private static final String KEY_PASSWORD_LOGIN = "password";
    private static final String KEY_PASSWORD_REGISTER = "password";
    private static final String KEY_BOARD_ID = "board_id";
    private static final String KEY_BOARD_TASKS = "tasks";
    private static final String KEY_SESSION_TOKEN = "token";
    private static final String KEY_ERROR_CODE = "error_code";
    private static final String KEY_GLOBAL_TASKS = "tasks";
    private static final String KEY_GLOBAL_BOARDS = "boards";

    /**
     *
     * @param t the task to parse
     * @return the requested Task as a JSON String
     */
    public static String getTaskAsJson(Task t) throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_TASK_NAME, t.getDisplayName());
        String result = jobj.toString();
        //TODO: add more fields as needed
        return result;
    }

    /**
     *
     * @param t the task we want to generate a request for
     * @param boardId the ID of the board where the task is being added
     * @return
     */
    public static String getTaskRequestAsJson(Task t, String boardId) throws JSONException {
        String taskAsJson = getTaskAsJson(t);
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_TASK, new JSONObject(taskAsJson));
        jobj.put(KEY_BOARD_ID, boardId);
        String result = jobj.toString();
        return result;
    }

    /**
     * Should only be called on a board whose tasks are synced with remote API
     * @param b the board to parse
     * @return the requested Board as a JSON String
     */
    public static String getBoardAsJson(Board b) throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_BOARD_NAME, b.getDisplayName());
        JSONArray tasks = new JSONArray();
        for(Task t: b.getTasks()){
            tasks.put(t.getTaskId());
        }
        jobj.put(KEY_BOARD_TASKS, tasks);
        //TODO: add more fields as needed
        String result = jobj.toString();
        return result;
    }

    /**
     *
     * @param json the JSON String to parse
     * @return the task object parsed from the JSON String
     */
    public static Task getTaskFromJson(String json) throws JSONException {
        JSONObject jobj = new JSONObject(json);
        Task result = new Task(jobj.getString(KEY_TASK_NAME));
        if(jobj.has(KEY_TASK_ID)){
            result.setTaskId(jobj.getString(KEY_TASK_ID));
        }
        //TODO: get more fields as needed
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
        if(jobj.has(KEY_BOARD_ID)){
            result.setBoardId(jobj.getString(KEY_BOARD_ID));
        }
        //TODO: get more fields as needed
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
     * @return
     */
    public static String getRegistrationAsJson(String username, String password) throws JSONException {
        JSONObject jobj = new JSONObject();
        jobj.put(KEY_USERNAME_REGISTER, username);
        jobj.put(KEY_PASSWORD_REGISTER, password);
        String result = jobj.toString();
        return result;
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
        String result = jobj.toString();
        return result;
    }

    /**
     * Obtain a session token from a login attempt response
     * @param loginResponseAsJson the json string sent by the Ergonaut API
     * @return the session token as a string
     */
    public static String getTokenFromLogin(String loginResponseAsJson) throws JSONException {
        JSONObject jobj = new JSONObject(loginResponseAsJson);
        String token = jobj.getString(KEY_SESSION_TOKEN);
        return token;
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
