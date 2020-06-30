package com.stickers.laks.whatssappforsticker.GoogleKeyboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.stickers.laks.whatssappforsticker.GoogleKeyboard.Util.AppIndexingUtil;


public class AppIndexingUpdateService extends JobIntentService
{
    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;
    public static String idx = null;

    public static void enqueueWork(Context context,String id) {
        idx = id;
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        AppIndexingUtil.setStickers(getApplicationContext(),idx, FirebaseAppIndex.getInstance());
    }
}