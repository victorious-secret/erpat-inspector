package com.techcamp.disaster;


import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.techcamp.disaster.data.DataManager;

import java.util.HashMap;

/**
 * Created by joseph on 4/29/14.
 */
public class TechCamp extends Application {



    HashMap<Long, ApplicationInfo> pendingDownloads = new HashMap<Long, ApplicationInfo>();

    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        DataManager.getInstance().init(this);
    }

}
