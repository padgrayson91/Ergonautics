package com.ergonautics.ergonautics.storage;

import com.ergonautics.ergonautics.models.Board;
import com.ergonautics.ergonautics.models.SerializableBoardWrapper;
import com.ergonautics.ergonautics.models.SerializableWrapper;
import com.ergonautics.ergonautics.models.SerializeableRealmListWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import io.realm.RealmList;

/**
 * Created by patrickgrayson on 8/24/16.
 * http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
 */
public class Serializer {

    public static byte[] serialize(Object toSerialize){
        SerializableWrapper wrapper = getWrapperForObject(toSerialize);
        if(wrapper != null){
            //Object needed to be wrapped for serialization
            toSerialize = wrapper;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] myBytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(toSerialize);
            myBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return myBytes;
    }

    public static Object deserialize(byte [] toDeserialize){
        ByteArrayInputStream bis = new ByteArrayInputStream(toDeserialize);
        ObjectInput in = null;
        Object result = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        if(result instanceof SerializableWrapper){
            result = getObjectFromWrapper((SerializableWrapper) result);
        }
        return result;
    }

    private static SerializableWrapper getWrapperForObject(Object toWrap){
        if(toWrap instanceof Board){
            return SerializableBoardWrapper.fromBoard((Board) toWrap);
        } else if(toWrap instanceof RealmList){
            return SerializeableRealmListWrapper.fromRealmList((RealmList) toWrap);
        } else {
            return null;
        }
    }

    private static Object getObjectFromWrapper(SerializableWrapper wrapper){
        return wrapper.unwrap();
    }


}
