package com.ergonautics.ergonautics.models;

/**
 * Created by patrickgrayson on 8/19/16.
 * Constants shared among all model classes
 */
interface ModelConstants {
    String REMOTE_ID_DEFAULT = null; //To identify boards which have not been stored in the remote database
    String DISPLAY_NAME_DEFAULT = "";
    long STARTED_AT_DEFAULT = -1L;
    long COMPLETED_AT_DEFAULT = -1L;
    long SCHEDULED_FOR_DEFAULT = -1L;
    int VALUE_DEFAULT = 0;
    long TIME_ESTIMATE_DEFAULT = 1000L * 60L * 30L;  //30 minutes in milliseconds

    //Task statuses (left here because they might eventually apply to other types of objects
    int STATUS_UNSTARTED = 0;
    int STATUS_IN_PROGRESS = 1;
    int STATUS_COMPLETED = 2;
    int STATUS_DEFUALT = STATUS_UNSTARTED;
}
