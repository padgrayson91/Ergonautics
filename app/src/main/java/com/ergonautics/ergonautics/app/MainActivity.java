package com.ergonautics.ergonautics.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ergonautics.ergonautics.R;
import com.ergonautics.ergonautics.storage.LocalStorage;

/**
 * App main page where user can view active tasks, current boards, notifications, etc.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ERGONAUT-MAIN";
    private static final int REQUEST_CODE_LOGIN = 1001;
    private LocalStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if user is logged in
        storage = LocalStorage.getInstance(this);
        if(storage.getSessionToken().isEmpty()){
            switchToLoginActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_LOGIN:
                if(resultCode == RESULT_OK){
                    //Login was successful
                    //TODO: show user landing page
                } else {
                    //User hit back
                    finish();
                }
                break;
            default:
                Log.d(TAG, "onActivityResult: unknown activity result");
        }
    }

    private void switchToLoginActivity(){
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
    }
}
