package com.cleac.watcherforgithub.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by cleac on 06.04.15.
 */
public class DataContract {

    public static final String CONTENT_AUTHORITY = "com.cleac.watcherforgithub";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_USER = "user";
    public static final String PATH_REPO = "repo";
    public static final String PATH_COMMIT = "commit";
    public static final String PATH_BRANCH = "branch";

    /**
     * Holds data about commits:
     *  - repository, where commit is from
     *  - current branch on repository, where commit is placed
     *  - commiter
     *  - sha checksum
     *  - message
     */
    public static final class CommitEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMIT;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMIT).build();

        public static final String TABLE_NAME = "commits";

        public static final String REPO_ID = "repo_id";
        public static final String BRANCH_ID = "branch_id";
        public static final String COMMITTER_ID = "committer_id";

        public static final String SHA = "sha";
        public static final String MESSAGE = "message";
        public static final String DATE = "date";

        public static Uri buildCommitUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI,_id);
        }

        public static String getShaFromUri(Uri uri) {
            return uri.getPathSegments().get(0);
        }
    }

    /**
     * Holds data about branches:
     *  - where were last commit
     *  - branch name
     *  - repository, where branch is
     * Will be after branch_id in CommitEntry
     */
    public static final class BranchesEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRANCH;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRANCH).build();
        public static final String TABLE_NAME = "branches";

        public static final String REPO_ID = "repo_id";

        public static final String NAME = "name";
        public static final String LAST_COMMIT_SHA = "last_commit_sha";

        public static Uri buildBranchUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI,_id);
        }
        public static String getNameFromUri(Uri uri) {
            return uri.getPathSegments().get(0);
        }
    }

    /**
     * Holds data about repository:
     *  - author of repository
     *  - name of repository
     *  - repository path on GitHub to fetch data
     */
    public static final class ReposEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REPO;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPO).build();
        public static final String TABLE_NAME = "repos";

        public static final String AUTHOR_ID = "author_id";

        public static final String REPO_NAME = "repo_name";
        public static final String REPO_PATH = "repo_path";

        public static Uri buildRepoUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI,_id);
        }
        public static Uri buildRepoUri(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }

        public static String getNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /**
     * Holds user data:
     *  - login of user
     * Will be used after:
     *  - auhor_id in ReposEntry
     *  - commiter_id in CommitEntry
     */
    public static final class UsersEntry implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
        public static final String TABLE_NAME = "users";

        public static final String USER_LOGIN = "user_login";

        public static final String EMAIL_URL = "email_url";

        public static Uri buildUserUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI,_id);
        }
        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static String getNameFromUri(Uri uri) {
            return uri.getPathSegments().get(0);
        }
    }
}
