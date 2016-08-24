package com.ergonautics.ergonautics;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.DBModelHelper;
import com.ergonautics.ergonautics.storage.ErgonautContentProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Created by patrickgrayson on 8/19/16.
 */
@RunWith(AndroidJUnit4.class)
public class ErgonautContentProviderTest {
    private ContentResolver mResolver;

    @Before
    public void setUp(){
        mResolver = getContext().getContentResolver();
    }

    @Test
    public void testTaskInsert(){
        Task t = new Task("Example Task");
        ContentValues tVals = DBModelHelper.getContentValuesForTask(t);
        String uriString = ErgonautContentProvider.TASKS_INSERT_URI.toString();
        uriString.replace("*", "12345"); //TODO: need an actual board id
        Uri uri = mResolver.insert(ErgonautContentProvider.TASKS_INSERT_URI, tVals);
        assertNotEquals("0", uri.getLastPathSegment());
    }

    @After
    public void tearDown(){
        mResolver = null;
    }
}
