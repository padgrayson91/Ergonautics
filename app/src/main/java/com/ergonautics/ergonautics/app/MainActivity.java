package com.ergonautics.ergonautics.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ergonautics.ergonautics.R;

/**
 * App main page where user can view active tasks, current boards, notifications, etc.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
