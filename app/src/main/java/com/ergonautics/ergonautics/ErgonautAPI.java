package com.ergonautics.ergonautics;

import android.content.Context;
import android.util.Log;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.LocalStorage;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by patrickgrayson on 8/18/16.
 * Abstraction of all API interaction and JSON such that only the model classes are
 * ever exposed to other app components
 */
public class ErgonautAPI {
    private static final String TAG = "ERGONAUT-API";
    //Error Codes
    public static final int SUCCESS = 0;
    public static final int ERROR_RESOURCE_NOT_FOUND = 1; //Requested resource did not exist
    public static final int ERROR_AUTH_FAILED = 2; //Expired token
    public static final int ERROR_INVALID_DATA = 3;
    public static final int ERROR_UNKNOWN = 99; //Unknown error

    private LocalStorage mStorage;
    private Context mContext;

    public ErgonautAPI(Context c){
        mContext = c;
        mStorage = LocalStorage.getInstance(c);
    }

    public int addTask(Task t, String boardId){
        try {
            String token = mStorage.getSessionToken();
            String taskRequestAsJson = ModelHelper.getTaskRequestAsJson(t, boardId);
            String result = APIHelper.postTask(token, taskRequestAsJson);
            Log.d(TAG, "addTask: " + result);
            int errorCode = ModelHelper.getErrorCode(result);
            return errorCode;
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: check error
            return ERROR_UNKNOWN;
        } catch (JSONException e) {
            e.printStackTrace();
            return ERROR_INVALID_DATA;
        }
    }

    public int addBoard(Board b){
        try {
            String token = mStorage.getSessionToken();
            String boardAsJson = ModelHelper.getBoardAsJson(b);
            String result = APIHelper.postBoard(token, boardAsJson);
            Log.d(TAG, "addBoard: " + result);
            int errorCode = ModelHelper.getErrorCode(result);
            return errorCode;
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: check error
            return ERROR_UNKNOWN;
        } catch (JSONException e) {
            e.printStackTrace();
            return ERROR_INVALID_DATA;
        }
    }

    public Task getTask(String taskId){
        try {
            String token = mStorage.getSessionToken();
            String response = null;
            response = APIHelper.getTask(token, taskId);
            Log.d(TAG, "getTask: " + response);
            //TODO: check response
            return ModelHelper.getTaskFromJson(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Board getBoard(String boardId){
        try {
            String token = mStorage.getSessionToken();
            String response = APIHelper.getBoard(token, boardId);
            Log.d(TAG, "getBoard: " + response);
            //TODO: check response
            return ModelHelper.getBoardFromJson(response);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int login(String username, String password){
        try {
            String loginAsJson = ModelHelper.getLoginAsJson(username, password);
            String response = APIHelper.login(loginAsJson);
            Log.d(TAG, "login: " + response);
            int errorCode = ModelHelper.getErrorCode(response);
            if(errorCode == SUCCESS) {
                String token = ModelHelper.getTokenFromLogin(response);
                mStorage.setSessionToken(token);

            }
            return errorCode;
        } catch (IOException e){
            e.printStackTrace();
            //TODO: check error
            return ERROR_UNKNOWN;
        } catch (JSONException e) {
            return ERROR_INVALID_DATA;
        }
    }

    public int register(String username, String password){
        try {
            String registrationAsJson = ModelHelper.getRegistrationAsJson(username, password);
            String response = APIHelper.register(registrationAsJson);
            Log.d(TAG, "register: " + response);
            int errorCode = ModelHelper.getErrorCode(response);
            return errorCode;
        } catch (IOException e){
            e.printStackTrace();
            //TODO: check error
            return ERROR_UNKNOWN;
        } catch (JSONException e) {
            return ERROR_INVALID_DATA;
        }
    }


}
