package com.ergonautics.ergonautics.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ergonautics.ergonautics.R;


/**
 * Activity to handle user login
 */
public class LoginActivity extends AppCompatActivity implements LoginTask.ILoginListener {
    private static final String TAG = "ERGONAUT-LOGIN";
    private EditText mUsernameEdit;
    private EditText mPasswordEdit;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameEdit = (EditText) findViewById(R.id.edit_username);
        mPasswordEdit = (EditText) findViewById(R.id.edit_password);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(mLoginListener);
    }

    View.OnClickListener mLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick - attempting login" );
            String [] credentials = new String[2];
            credentials[0] = mUsernameEdit.getText().toString();
            credentials[1] = mPasswordEdit.getText().toString();
            LoginTask lt = new LoginTask(LoginActivity.this, LoginActivity.this);
            lt.execute(credentials);
        }
    };

    @Override
    public void onLoginComplete(int result) {
        Log.d(TAG, "onLoginComplete: notified of login completion" );
    }
}
