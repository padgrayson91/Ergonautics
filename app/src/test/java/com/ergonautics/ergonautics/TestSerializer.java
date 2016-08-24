package com.ergonautics.ergonautics;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.Task;
import com.ergonautics.ergonautics.storage.Serializer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class TestSerializer {
    @Test
    public void testSerializeTask(){
        Task t = new Task("Task to Serialize");
        byte[] result = Serializer.serialize(t);
        assertTrue(result.length > 0);
    }

    @Test
    public void testDeserializeTask(){
        Task t = new Task("Task to deserialize");
        byte[] result = Serializer.serialize(t);
        assertTrue(result.length > 0);
        Task des = (Task) Serializer.deserialize(result);
        assertEquals(t.getDisplayName(), des.getDisplayName());
    }

    @Test
    public void testSerializeBoard(){
        Board b = new Board("Board to Serialize");
        byte [] result = Serializer.serialize(b);
        assertTrue(result.length > 0);
    }

    @Test
    public void testDeserializeBoard(){
        Board b = new Board("Board to Serialize");
        byte [] result = Serializer.serialize(b);
        assertTrue(result.length > 0);
        Board des = (Board) Serializer.deserialize(result);
        assertEquals(b.getDisplayName(), des.getDisplayName());
    }


}
