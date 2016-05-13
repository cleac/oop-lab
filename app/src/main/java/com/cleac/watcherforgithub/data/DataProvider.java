package com.cleac.watcherforgithub.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

public class DataProvider extends ContentProvider{

    public static final int BRANCH = 100;
    public static final int ALL_BRANCHES = 101;
    public static final int COMMIT = 200;
    public static final int ALL_COMMITS = 201;
    public static final int REPO = 300;
    public static final int ALL_REPOS = 301;
    public static final int USER = 400;
    public static final int ALL_USER = 401;
    private DataDBHelper mDataDBHelper;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,DataContract.PATH_BRANCH, ALL_BRANCHES);
        matcher.addURI(authority,DataContract.PATH_BRANCH+"/*", BRANCH);

        matcher.addURI(authority,DataContract.PATH_COMMIT, ALL_COMMITS);
        matcher.addURI(authority,DataContract.PATH_COMMIT+"/*", COMMIT);

        matcher.addURI(authority,DataContract.PATH_REPO, ALL_REPOS);
        matcher.addURI(authority,DataContract.PATH_REPO+"/*", REPO);

        matcher.addURI(authority,DataContract.PATH_USER, ALL_USER);
        matcher.addURI(authority,DataContract.PATH_USER+"/*", USER);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDataDBHelper = new DataDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case BRANCH:
                tableName = DataContract.BranchesEntry.TABLE_NAME + " , " +
                        DataContract.CommitEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                else
                    selection = "";
                selection += DataContract.CommitEntry.TABLE_NAME +'.'+DataContract.CommitEntry.BRANCH_ID
                        + '=' +
                        DataContract.BranchesEntry.TABLE_NAME + '.' + DataContract.BranchesEntry._ID
                        +" and "+
                        DataContract.BranchesEntry.TABLE_NAME+'.'+ DataContract.BranchesEntry.NAME
                        +"= ?";
                selectionArgs = new String[]{DataContract.BranchesEntry.getNameFromUri(uri)};
                break;
            case ALL_BRANCHES:
                tableName = DataContract.BranchesEntry.TABLE_NAME;
                break;
            case COMMIT:
                tableName = DataContract.CommitEntry.TABLE_NAME + " , " +
                        DataContract.UsersEntry.TABLE_NAME + " , " +
                        DataContract.BranchesEntry.TABLE_NAME + " , " +
                        DataContract.ReposEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                else
                    selection = "";
                selection += DataContract.CommitEntry.TABLE_NAME + '.' + DataContract.CommitEntry.COMMITTER_ID
                        + '=' +
                        DataContract.UsersEntry.TABLE_NAME + '.' + DataContract.UsersEntry._ID +
                        " and " +
                        DataContract.CommitEntry.TABLE_NAME + '.' + DataContract.CommitEntry.BRANCH_ID
                        + '=' +
                        DataContract.BranchesEntry.TABLE_NAME + '.' + DataContract.BranchesEntry._ID +
                        " and " +
                        DataContract.CommitEntry.TABLE_NAME + '.' + DataContract.CommitEntry.REPO_ID
                        + '=' +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry._ID
                        +" and "+
                        DataContract.CommitEntry.TABLE_NAME+'.'+ DataContract.CommitEntry.SHA
                        +"= ?";
                selectionArgs = new String[]{DataContract.CommitEntry.getShaFromUri(uri)};
                break;
            case ALL_COMMITS:
                tableName = DataContract.CommitEntry.TABLE_NAME + " , " +
                        DataContract.UsersEntry.TABLE_NAME + " , " +
                        DataContract.BranchesEntry.TABLE_NAME + " , " +
                        DataContract.ReposEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                else
                    selection = "";
                selection += DataContract.CommitEntry.TABLE_NAME + '.' + DataContract.CommitEntry.COMMITTER_ID
                        + '=' +
                        DataContract.UsersEntry.TABLE_NAME + '.' + DataContract.UsersEntry._ID +
                        " and " +
                        DataContract.CommitEntry.TABLE_NAME + '.' + DataContract.CommitEntry.BRANCH_ID
                        + '=' +
                        DataContract.BranchesEntry.TABLE_NAME + '.' + DataContract.BranchesEntry._ID +
                        " and " +
                        DataContract.CommitEntry.TABLE_NAME + '.' + DataContract.CommitEntry.REPO_ID
                        + '=' +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry._ID;
                break;
            case REPO:
/*              If i will use branches - keep it
                tableName = DataContract.ReposEntry.TABLE_NAME + " , "+
                        DataContract.BranchesEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                selection += DataContract.BranchesEntry.TABLE_NAME +'.'+DataContract.BranchesEntry.REPO_ID
                        + '=' +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry._ID
                        + " and " +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry.REPO_NAME
                        + "= ?";
                selectionArgs = new String[]{DataContract.ReposEntry.getNameFromUri(uri)};*/
                tableName = DataContract.ReposEntry.TABLE_NAME + " , "+
                        DataContract.CommitEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                else
                    selection = "";
                selection += DataContract.CommitEntry.TABLE_NAME +'.'+DataContract.CommitEntry.REPO_ID
                        + '=' +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry._ID
                        + " and " +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry.REPO_NAME
                        + "= ?";
                selectionArgs = new String[]{DataContract.ReposEntry.getNameFromUri(uri)};
                Log.e(this.getClass().getSimpleName(),selectionArgs[0]);
                break;
            case ALL_REPOS:
                tableName = DataContract.ReposEntry.TABLE_NAME + " , "+
                        DataContract.UsersEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                else
                    selection = "";
                selection += DataContract.UsersEntry.TABLE_NAME +'.'+DataContract.UsersEntry._ID
                        + '=' +
                        DataContract.ReposEntry.TABLE_NAME + '.' + DataContract.ReposEntry.AUTHOR_ID;
                break;
            case USER:
                tableName = DataContract.UsersEntry.TABLE_NAME;
                if(!TextUtils.isEmpty(selection))
                    selection += " and ";
                else
                    selection = "";
                selection += DataContract.UsersEntry.TABLE_NAME + '.' + DataContract.UsersEntry.USER_LOGIN
                        + "= ?";
                selectionArgs = new String[]{DataContract.UsersEntry.getNameFromUri(uri)};
                break;
            case ALL_USER:
                tableName = DataContract.UsersEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return mDataDBHelper.getReadableDatabase().query (
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDataDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri retUri = DataContract.BASE_CONTENT_URI;
        switch (match) {
            case ALL_BRANCHES: {
                long _id = db.insert(DataContract.BranchesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    retUri = DataContract.BranchesEntry.buildBranchUri(_id);
//                else
//                    throw new android.database.SQLException("Failed to insert row");
                break;
            }
            case ALL_COMMITS: {
                long _id = db.insert(DataContract.CommitEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    retUri = DataContract.CommitEntry.buildCommitUri(_id);
                else {
                    Cursor cursor = db.query(DataContract.CommitEntry.TABLE_NAME,
                            new String[]{DataContract.CommitEntry._ID},
                            DataContract.CommitEntry.SHA+" = ? ",
                            new String[]{values.getAsString(DataContract.CommitEntry.SHA)},
                            null,null,null
                    );
                    cursor.moveToFirst();
                    retUri = DataContract.CommitEntry.buildCommitUri(cursor.getLong(0));
                }
                break;
            }
            case ALL_REPOS: {
                long _id = db.insert(DataContract.ReposEntry.TABLE_NAME, null, values);
                Log.e(this.getClass().getSimpleName(),values.toString());
                if (_id > 0)
                    retUri = DataContract.ReposEntry.buildRepoUri(_id);
                else {
                    Cursor cursor = db.query(DataContract.ReposEntry.TABLE_NAME,
                            new String[]{DataContract.ReposEntry._ID},
                            DataContract.ReposEntry.REPO_PATH+" = ? ",
                            new String[]{values.getAsString(DataContract.ReposEntry.REPO_PATH)},
                            null,null,null
                            );
                    cursor.moveToFirst();
                    retUri = DataContract.ReposEntry.buildRepoUri(cursor.getLong(0));
                }
                break;
            }
            case ALL_USER: {
                long _id = db.insert(DataContract.UsersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    retUri = DataContract.UsersEntry.buildUserUri(_id);
                else {
                    Cursor cursor = db.query(DataContract.UsersEntry.TABLE_NAME,
                            new String[]{DataContract.UsersEntry._ID},
                            DataContract.UsersEntry.USER_LOGIN+" = ? ",
                            new String[]{values.getAsString(DataContract.UsersEntry.USER_LOGIN)},
                            null,null,null
                    );
                    cursor.moveToFirst();
                    if(cursor.getLong(0)>0)
                        retUri = DataContract.UsersEntry.buildUserUri(cursor.getLong(0));
                    else
                        throw new SQLException("Unknown error in i/o");
                    cursor.close();
                }
                break;
            }
            default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDataDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updated = 0;
        String tableName;

        switch (sUriMatcher.match(uri)) {
            case ALL_BRANCHES:
                tableName = DataContract.BranchesEntry.TABLE_NAME;
                break;
            case ALL_COMMITS:
                tableName = DataContract.CommitEntry.TABLE_NAME;
                break;
            case ALL_REPOS:
                tableName = DataContract.ReposEntry.TABLE_NAME;
                break;
            case ALL_USER:
                tableName = DataContract.UsersEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        updated = db.update(tableName,values,selection,selectionArgs);
        if(updated > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDataDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deleted = 0;
        String tableName;

        switch (sUriMatcher.match(uri)) {
            case ALL_BRANCHES:
                tableName = DataContract.BranchesEntry.TABLE_NAME;
                break;
            case ALL_COMMITS:
                tableName = DataContract.CommitEntry.TABLE_NAME;
                break;
            case ALL_REPOS:
                tableName = DataContract.ReposEntry.TABLE_NAME;
                break;
            case ALL_USER:
                tableName = DataContract.UsersEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        deleted = db.delete(tableName,selection,selectionArgs);
        if(deleted > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRANCH:
            case ALL_BRANCHES:
                return DataContract.BranchesEntry.CONTENT_TYPE;
            case COMMIT:
            case ALL_COMMITS:
                return DataContract.CommitEntry.CONTENT_TYPE;
            case REPO:
            case ALL_REPOS:
                return DataContract.ReposEntry.CONTENT_TYPE;
            case USER:
            case ALL_USER:
                return DataContract.UsersEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
