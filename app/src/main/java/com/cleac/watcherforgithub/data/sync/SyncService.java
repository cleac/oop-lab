package com.cleac.watcherforgithub.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Objects;

/**
 * Created by cleac on 4/15/15.
 */
public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter= null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if(sSyncAdapter==null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
