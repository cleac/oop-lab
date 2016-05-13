package com.cleac.watcherforgithub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cleac on 06.04.15.
 */
public class DataDBHelper extends SQLiteOpenHelper{

    private static int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "github_watcher.db";

    public DataDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String sq_create_table_users = "create table " + DataContract.UsersEntry.TABLE_NAME + " ( " +
                DataContract.UsersEntry._ID + " integer primary key , " +
                DataContract.UsersEntry.USER_LOGIN + " text not null, " +
                DataContract.UsersEntry.EMAIL_URL + " text, "+
                " unique ( "+ DataContract.UsersEntry.USER_LOGIN+") on conflict ignore);";

        final String sq_create_table_repos = "create table " + DataContract.ReposEntry.TABLE_NAME + " ( " +
                DataContract.ReposEntry._ID + " integer primary key autoincrement ," +
                DataContract.ReposEntry.AUTHOR_ID + " integer not null ," +
                DataContract.ReposEntry.REPO_NAME + " text not null ," +
                DataContract.ReposEntry.REPO_PATH + " text not null ," +
                " foreign key ( " + DataContract.ReposEntry.AUTHOR_ID + " ) references " +
                DataContract.UsersEntry.TABLE_NAME + " ( " + DataContract.UsersEntry._ID + " )"+
                " unique ( "+ DataContract.ReposEntry.REPO_PATH+") on conflict ignore );";

        final String sq_create_table_branches = "create table " + DataContract.BranchesEntry.TABLE_NAME + " ( " +
                DataContract.BranchesEntry._ID + " integer primary key autoincrement ," +
                DataContract.BranchesEntry.REPO_ID + " integer not null ," +
                DataContract.BranchesEntry.LAST_COMMIT_SHA + " text not null ," +
                DataContract.BranchesEntry.NAME + " text not null ," +
                " foreign key ( " + DataContract.BranchesEntry.REPO_ID + " ) references " +
                DataContract.ReposEntry.TABLE_NAME + " ( " + DataContract.ReposEntry._ID + " ) );" ;


        final String sq_create_table_commits = "create table " + DataContract.CommitEntry.TABLE_NAME + " ( " +
                DataContract.CommitEntry._ID + " integer primary key autoincrement ," +
                DataContract.CommitEntry.REPO_ID + " integer not null ," +
                DataContract.CommitEntry.BRANCH_ID + " integer ," +
                DataContract.CommitEntry.COMMITTER_ID + " integer not null ," +
                DataContract.CommitEntry.SHA + " text not null unique on conflict ignore ," +
                DataContract.CommitEntry.MESSAGE + " text not null ," +
                DataContract.CommitEntry.DATE + " integer ," +
                " foreign key ( " + DataContract.CommitEntry.REPO_ID + " ) references " +
                DataContract.ReposEntry.TABLE_NAME + " ( " + DataContract.ReposEntry._ID + " ), " +
                " foreign key ( " + DataContract.CommitEntry.BRANCH_ID + " ) references " +
                DataContract.BranchesEntry.TABLE_NAME + " ( " + DataContract.BranchesEntry._ID + " ), " +
                " foreign key ( " + DataContract.CommitEntry.COMMITTER_ID + " ) references " +
                DataContract.UsersEntry.TABLE_NAME + " ( " + DataContract.UsersEntry._ID + " ) ); ";

        sqLiteDatabase.execSQL(sq_create_table_users);
        sqLiteDatabase.execSQL(sq_create_table_repos);
        sqLiteDatabase.execSQL(sq_create_table_branches);
        sqLiteDatabase.execSQL(sq_create_table_commits);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.CommitEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.BranchesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.ReposEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.UsersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
