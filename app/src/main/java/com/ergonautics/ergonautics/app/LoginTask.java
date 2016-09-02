package com.ergonautics.ergonautics.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ergonautics.ergonautics.ErgonautAPI;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class LoginTask extends AsyncTask<String, Void, Integer> {
    private static final String TAG = "ERGONAUT-LOGIN";
    public interface ILoginListener {
        void onLoginComplete(int result);
    }
    private ILoginListener mListener;
    private ErgonautAPI mApi;

    public LoginTask(Context c, ILoginListener listener){
        mListener = listener;
        mApi = new ErgonautAPI(c);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        if(strings.length < 2){
            return ErgonautAPI.ERROR_INVALID_DATA;
        }
        String username = strings[0];
        String password = strings[1];

        return mApi.login(username, password);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if(mListener != null){
            mListener.onLoginComplete(result);
        }
        Log.d(TAG, "onPostExecute: Login success: " + (result == ErgonautAPI.SUCCESS));
    }
}
