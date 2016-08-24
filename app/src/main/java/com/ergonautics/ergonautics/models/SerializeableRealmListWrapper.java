package com.ergonautics.ergonautics.models;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmModel;

/**
 * Created by patrickgrayson on 8/24/16.
 */
public class SerializeableRealmListWrapper extends SerializableWrapper implements Serializable {
    private ArrayList<RealmModel> list;

    private SerializeableRealmListWrapper(){
        list = new ArrayList<>();
    }

    public static SerializeableRealmListWrapper fromRealmList(RealmList<RealmModel> inList){
        SerializeableRealmListWrapper wrapper = new SerializeableRealmListWrapper();
        for(RealmModel rm: inList){
            wrapper.list.add(rm);
        }
        return wrapper;
    }

    @Override
    public Object unwrap() {
        RealmList<RealmModel> result = new RealmList<>();
        for(RealmModel rm: list){
            result.add(rm);
        }
        return result;
    }
}
