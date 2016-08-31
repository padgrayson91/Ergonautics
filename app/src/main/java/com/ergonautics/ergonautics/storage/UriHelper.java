package com.ergonautics.ergonautics.storage;

import android.net.Uri;

/**
 * Created by patrickgrayson on 8/31/16.
 */
public class UriHelper {

    public static Uri getTaskInsertUri(String boardId){
        String baseString = ErgonautContentProvider.TASKS_INSERT_URI.toString();
        String fullString = baseString.replace("*", boardId);
        return Uri.parse(fullString);
    }

}
