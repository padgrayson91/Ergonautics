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
}
