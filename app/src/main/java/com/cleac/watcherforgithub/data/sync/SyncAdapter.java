package com.cleac.watcherforgithub.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.cleac.watcherforgithub.data.rest.RestError;

import com.cleac.watcherforgithub.R;
import com.cleac.watcherforgithub.data.DataContract;
import com.cleac.watcherforgithub.data.rest.RestClient;
import com.cleac.watcherforgithub.data.rest.model.UserData;
import com.cleac.watcherforgithub.data.rest.model.branch.Branch;
import com.cleac.watcherforgithub.data.rest.model.committs.CommitsDatum;
import com.cleac.watcherforgithub.data.rest.model.repo.RepoInfo;

import java.util.ArrayList;

import retrofit.RetrofitError;

/**
 * Created by cleac on 4/15/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {
        //Update Repositories
        Cursor reposCursor = mContentResolver.query(
                DataContract.ReposEntry.CONTENT_URI,
                new String[]{DataContract.ReposEntry.REPO_NAME,
                        DataContract.UsersEntry.USER_LOGIN,
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry._ID},
                null,
                null,
                null
        );
        if(reposCursor.moveToFirst()) {
            do {
                try {
                    RepoInfo repoInfo = RestClient.instance().ApiService().getRepoData(
                            reposCursor.getString(1), reposCursor.getString(0));
                    ArrayList<CommitsDatum> commitsData = RestClient.instance().ApiService().getCommitsData(
                            reposCursor.getString(1), reposCursor.getString(0));
                    for (CommitsDatum commit : commitsData) {
                        ContentValues commitValues = new ContentValues();
                        long committer_id = getUserId(commit.getAuthor().getLogin(), mContentResolver);
                        commitValues.put(DataContract.CommitEntry.MESSAGE, commit.getCommit().getMessage());
                        commitValues.put(DataContract.CommitEntry.SHA, commit.getSha());
                        commitValues.put(DataContract.CommitEntry.REPO_ID, reposCursor.getInt(2));
                        commitValues.put(DataContract.CommitEntry.COMMITTER_ID, committer_id);
                        mContentResolver.insert(DataContract.CommitEntry.CONTENT_URI, commitValues);
                    }

                    ContentValues repoValues = new ContentValues();
                    repoValues.put(DataContract.ReposEntry.REPO_NAME, repoInfo.getName());
                    repoValues.put(DataContract.ReposEntry.AUTHOR_ID, getUserId(reposCursor.getString(1), mContentResolver));
                    repoValues.put(DataContract.ReposEntry.REPO_PATH,
                            reposCursor.getString(1) + '/' + repoInfo.getName());
                    try {
                        mContentResolver.insert(DataContract.ReposEntry.CONTENT_URI, repoValues);
                    } catch (SQLException excpt) {
                        if (excpt.getMessage().equals("Failed to insert row")) {
                            mContentResolver.update(DataContract.ReposEntry.CONTENT_URI,
                                    repoValues,
                                    DataContract.ReposEntry.REPO_PATH + " = ?",
                                    new String[]{reposCursor.getString(1) + '/' + repoInfo.getName()}
                            );
                        } else {
                            throw excpt;
                        }
                    }
                } catch (RetrofitError error) {
                    if (error.getResponse() != null) {
                        RestError body = (RestError) error.getBodyAs(RestError.class);
                        Log.e(RestClient.class.getSimpleName(), "Error with fetching data");
                    }
                }
            } while (reposCursor.moveToNext());
            reposCursor.close();
            updateUsers();
        }
    }

    private void updateUsers() {
        Cursor usernameCursor = mContentResolver.query(
                DataContract.UsersEntry.CONTENT_URI,
                new String[]{DataContract.UsersEntry.USER_LOGIN},
                null,   //selection
                null,   //selection args
                null    //Sort
        );
        usernameCursor.moveToFirst();
        do {
            try {
                UserData userData =
                        RestClient.instance().ApiService().getUserData(usernameCursor.getString(0));
                ContentValues userValues = new ContentValues();
                userValues.put(DataContract.UsersEntry.EMAIL_URL, userData.getEmail());
                mContentResolver.update(
                        DataContract.UsersEntry.CONTENT_URI, userValues,
                        DataContract.UsersEntry.TABLE_NAME + '.' + DataContract.UsersEntry.USER_LOGIN +
                                " = ? ", new String[]{usernameCursor.getString(0)});
            } catch(RetrofitError error) {
                if(error.getResponse()!=null) {
                    RestError body = (RestError) error.getBodyAs(RestError.class);
                    Log.e(RestClient.class.getSimpleName(),"Error with fetching data");
                }
            }
        } while (usernameCursor.moveToNext());
        usernameCursor.close();
    }

    public static long getUserId(String userlogin, ContentResolver mContentResolver) {
        long user_id;
        Cursor user_idCursor = mContentResolver.query(
                DataContract.UsersEntry.CONTENT_URI.buildUpon().
                        appendPath(userlogin).build(),
                new String[]{DataContract.UsersEntry._ID},
                null, null, null, null
        );
        if (user_idCursor.moveToFirst()) {
            user_id = user_idCursor.getLong(0);
            user_idCursor.close();
        } else {
            UserData userData =
                    RestClient.instance().ApiService().getUserData(userlogin);
            ContentValues userValues = new ContentValues();
            userValues.put(DataContract.UsersEntry.USER_LOGIN, userData.getLogin());
            userValues.put(DataContract.UsersEntry.EMAIL_URL, userData.getEmail());
            DataContract.UsersEntry.getIdFromUri(
                    mContentResolver.insert(DataContract.UsersEntry.CONTENT_URI, userValues));
            user_id = DataContract.UsersEntry.getIdFromUri(
                    mContentResolver.insert(DataContract.UsersEntry.CONTENT_URI, userValues));
        }
        return user_id;
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }
}
