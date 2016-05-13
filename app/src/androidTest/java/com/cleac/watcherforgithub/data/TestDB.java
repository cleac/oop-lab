package com.cleac.watcherforgithub.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.zip.DataFormatException;

/**
 * Created by cleac on 06.04.15.
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();
    public static final String test_login = "asdf";
    public static final String test_login_index = "1";
    public static final String test_repo_index = "1";
    public static final String test_repo_name = "pabracadabra";
    public static final String test_repo_path = "http://google.com/";

    public void setUp() {
        deleteTheDatabase();
    }

    void deleteTheDatabase() {
        mContext.deleteDatabase(DataDBHelper.DATABASE_NAME);
    }

    public void testCreateDB() throws Throwable{

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DataContract.UsersEntry.TABLE_NAME);
        tableNameHashSet.add(DataContract.ReposEntry.TABLE_NAME);
        tableNameHashSet.add(DataContract.BranchesEntry.TABLE_NAME);
        tableNameHashSet.add(DataContract.CommitEntry.TABLE_NAME);

        mContext.deleteDatabase(DataDBHelper.DATABASE_NAME);

        SQLiteDatabase db = new DataDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

       do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );
        assertTrue("Error: Your database was created without all tables",
                tableNameHashSet.isEmpty());
        c.close();


        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + DataContract.UsersEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());


        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> reposColumnHashSet = new HashSet<String>();
        reposColumnHashSet.add(DataContract.UsersEntry._ID);
        reposColumnHashSet.add(DataContract.UsersEntry.USER_LOGIN);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            reposColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required users entry columns",
                reposColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + DataContract.ReposEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> userColumnHashSet = new HashSet<String>();
        userColumnHashSet.add(DataContract.ReposEntry._ID);
        userColumnHashSet.add(DataContract.ReposEntry.AUTHOR_ID);
        userColumnHashSet.add(DataContract.ReposEntry.REPO_NAME);
        userColumnHashSet.add(DataContract.ReposEntry.REPO_PATH);


        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            userColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required repos entry columns",
                userColumnHashSet.isEmpty());
    }

    public void testWrite() {
        mContext.deleteDatabase(DataDBHelper.DATABASE_NAME);

        SQLiteDatabase db = new DataDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());

        final String sql_insert_user_data = "insert into " + DataContract.UsersEntry.TABLE_NAME + " values(" +
                test_login_index + " , " +'\"'+test_login + '\"'+ " );";
        db.rawQuery(sql_insert_user_data,null);

        final String sql_insert_repo_data = "insert into " + DataContract.ReposEntry.TABLE_NAME + " values(" +
                test_repo_index + " , " + test_login_index + " , " +
                '\"'+test_repo_name +'\"'+" , " +'\"'+ test_repo_path +'\"' + ");";
        db.rawQuery(sql_insert_repo_data,null);
        db.close();

        mContext.deleteDatabase(DataDBHelper.DATABASE_NAME);

    }

}
