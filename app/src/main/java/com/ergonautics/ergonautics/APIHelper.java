package com.ergonautics.ergonautics;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by patrickgrayson on 8/18/16.
 * Class to abstract all communications with the Ergonaut API
 */
public class APIHelper {
    //Values to use for all API connections
    private static final String PROTOCOL = "http://";
    private static final String HOST = "ergonautics.com";
    private static final int PORT = 80;

    //API endpoints
    private static final String TASKS_URL = "tasks";
    private static final String BOARDS_URL = "boards";
    private static final String LOGIN_URL = "login";
    private static final String REGISTER_URL = "register";

    //Header Constants
    private static final String API_TOKEN = "api-token";

    //For okhttp
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    /**
     *
     * @param registrationAsJson JSON String used for registration
     * @return the full response from the Ergonaut API
     */
    public static String register(String registrationAsJson) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, registrationAsJson);
        Request request = new Request.Builder()
                .url(getUrlString(REGISTER_URL, null))
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     *
     * @param loginAsJson JSON string used for login
     * @return the full response from the Ergonaut API
     */
    public static String login(String loginAsJson) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, loginAsJson);
        Request request = new Request.Builder()
                .url(getUrlString(LOGIN_URL, null))
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     *
     * @param token the session token to use
     * @param taskId the id of the task to be fetched
     * @return the full response from the Ergonaut API
     */
    public static String getTask(String token, String taskId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader(API_TOKEN, token)
                .url(getUrlString(TASKS_URL, taskId))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getAllTasks(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader(API_TOKEN, token)
                .url(getUrlString(TASKS_URL, null))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     *
     * @param token the session token to use
     * @param boardId the id of the board to be fetched
     * @return the full response from the Ergonaut API
     */
    public static String getBoard(String token, String boardId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader(API_TOKEN, token)
                .url(getUrlString(BOARDS_URL, boardId))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getAllBoards(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader(API_TOKEN, token)
                .url(getUrlString(BOARDS_URL, null))
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     *
     * @param token the session token to use
     * @param boardAsJson the board to post as a JSON String
     * @return the full response from the Ergonaut API
     */
    public static String postBoard(String token, String boardAsJson) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, boardAsJson);
        Request request = new Request.Builder()
                .addHeader(API_TOKEN, token)
                .url(getUrlString(BOARDS_URL, null))
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     *
     * @param token the session token to use
     * @param taskRequestAsJson the task request to post as a JSON String
     * @return the full response from the Ergonaut API
     */
    public static String postTask(String token, String taskRequestAsJson) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, taskRequestAsJson);
        Request request = new Request.Builder()
                .addHeader(API_TOKEN, token)
                .url(getUrlString(TASKS_URL, null))
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Get a String representation of a url based on the desired endpoint and resource
     * @param endpoint The API endpoint to connect to e.g. LOGIN_URL = "login"
     * @param resource The specific resource id to fetch
     * @return a String that can be parsed as a URL
     */
    private static String getUrlString(String endpoint, @Nullable String resource){
        if(resource == null) {
            return String.format("%s%s:%d/%s", PROTOCOL, HOST, PORT, endpoint);
        } else {
            return String.format("%s%s:%d/%s/%s", PROTOCOL, HOST, PORT, endpoint, resource);
        }
    }


}
