package com.cleac.watcherforgithub.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by cleac on 4/13/15.
 */
public class TestProvider extends AndroidTestCase {

    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                DataContract.UsersEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                DataContract.BranchesEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                DataContract.ReposEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                DataContract.CommitEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                DataContract.UsersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Users table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                DataContract.BranchesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Branches table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                DataContract.ReposEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Repos table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                DataContract.CommitEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Commits table during delete", 0, cursor.getCount());
        cursor.close();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                DataProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: DataProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + DataContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DataContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: DataProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testUsersWriteRead() {
        ContentValues userValues = new ContentValues();
        userValues.put(DataContract.UsersEntry.USER_LOGIN,"cleac");
        userValues.put(DataContract.UsersEntry.EMAIL_URL,"nestorf250@gmail.com");

        long id = DataContract.UsersEntry.getIdFromUri(mContext.getContentResolver().insert(DataContract.UsersEntry.CONTENT_URI,
                userValues));

        Cursor cursor = mContext.getContentResolver().query(DataContract.UsersEntry.CONTENT_URI,
                new String[]{DataContract.UsersEntry.USER_LOGIN,
                        DataContract.UsersEntry.EMAIL_URL},
                DataContract.UsersEntry._ID + " = ?",
                new String[]{String.valueOf(id)},null,null);
        assertTrue("Bad query",cursor.moveToFirst());

        assertEquals("User login written incorrect","cleac",cursor.getString(0));
        assertEquals("User login written incorrect","nestorf250@gmail.com",cursor.getString(1));
        cursor.close();
    }
}
