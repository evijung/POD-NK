package com.mist.it.pod_nk;

import android.provider.BaseColumns;

/**
 * Created by Tunyaporn on 6/29/2017.
 */

public interface MyConstant extends BaseColumns {
    public static final String projectString = "DMS_NK";
    public static final String serverString = "http://service.eternity.co.th/";
    public static final String urlGetUser = serverString + projectString + "/app/centerservice/getuser.php";
    public static final String urlGetJobList = serverString + projectString + "/app/centerservice/getJobList.php";
    public static final String urlGetJobListDate = serverString + projectString + "/app/centerservice/getListJobDate.php";

}
