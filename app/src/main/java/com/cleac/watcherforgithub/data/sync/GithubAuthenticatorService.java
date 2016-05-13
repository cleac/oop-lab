package com.cleac.watcherforgithub.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by cleac on 4/15/15.
 */
public class GithubAuthenticatorService extends Service {
    private GithubAuthenticator mGithubAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mGithubAuthenticator = new GithubAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mGithubAuthenticator.getIBinder();
    }
}
