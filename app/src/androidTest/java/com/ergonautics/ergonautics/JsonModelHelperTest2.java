package com.ergonautics.ergonautics;

import android.support.test.runner.AndroidJUnit4;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.JsonModelHelper;
import com.ergonautics.ergonautics.models.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 8/29/16.
 */
@RunWith(AndroidJUnit4.class)
public class JsonModelHelperTest2 {

    @Test
    public void testTaskFromJson(){
        String jstring = "{\"display_name\":\"Some Task\", \"created_at\":12345567, " +
                "\"started_at\":-1, \"completed_at\":-1, \"value\":0, " +
                "\"scheduled_for\":-1, \"time_estimate\":123456.123456, \"task_id\":\"23e3fd827\"," +
                "\"status\":1}";
        try {
            Task t = JsonModelHelper.getTaskFromJson(jstring);
            assertEquals(123456, t.getTimeEstimate());
            assertEquals(1, t.getStatus());
        } catch (JSONException e) {
            assertTrue(false);
        }


    }

    @Test
    public void testJsonFromTask(){
        Task t = new Task("Some Task");
        t.setTaskId("23e3fd827");
        t.setCompletedAt(-1);
        t.setCreatedAt(12345567);
        t.setScheduledFor(-1);
        t.setStartedAt(-1);
        t.setTimeEstimate(123456);
        t.setValue(0);
        t.setStatus(2);

        try {
            String jstring = JsonModelHelper.getTaskAsJson(t);
            JSONObject jobj = new JSONObject(jstring);
            assertEquals(jobj.getString("task_id"), "23e3fd827");
            assertEquals(2, jobj.getInt("status"));
        } catch (JSONException e) {
            assertTrue(false);
        }


    }

    @Test
    public void testBoardFromJson(){
        String jstring = "{\"display_name\":\"Some Board\", \"created_at\":12345567, " +
                "\"board_id\":\"23e3fd827\"," +
                "\"tasks\":[], \"last_modified\":12345569}";

        try {
            Board b = JsonModelHelper.getBoardFromJson(jstring);
            assertEquals("23e3fd827", b.getBoardId());
            assertEquals(12345569, b.getLastModified());
        } catch (JSONException e) {
            assertTrue(false);
        }


    }

    @Test
    public void testJsonFromBoard(){
        Board b = new Board("Some Board");
        b.setBoardId("135fda77a65a6");
        b.setCreatedAt(124532L);
        b.setLastModified(124539L);
        b.addTask(new Task("Some Task"));

        try {
            String jstring = JsonModelHelper.getBoardAsJson(b);
            JSONObject jobj = new JSONObject(jstring);
            assertEquals(124539, jobj.getLong("last_modified"));
            assertEquals("135fda77a65a6", jobj.getString("board_id"));
        } catch (JSONException e) {
            assertTrue(false);
        }
    }
}
