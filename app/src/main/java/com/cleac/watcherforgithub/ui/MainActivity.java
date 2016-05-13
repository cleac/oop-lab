package com.cleac.watcherforgithub.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cleac.watcherforgithub.R;
import com.cleac.watcherforgithub.data.DataContract;
import com.cleac.watcherforgithub.data.sync.SyncAdapter;


public class MainActivity extends ActionBarActivity implements RepositoryListFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RepositoryListFragment())
                    .commit();
        }
        SyncAdapter.initializeSyncAdapter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_repo:
                DialogFragment dialog = new AddRepoDialog();
                dialog.show(getFragmentManager(),"addRepo");
                break;
            case R.id.action_refresh:
                SyncAdapter.syncImmediately(getApplicationContext());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(this,RepositoryActivity.class)
                .setData(uri);
        startActivity(intent);
    }

    public static class AddRepoDialog extends DialogFragment {

        Context mContext;

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            mContext = getActivity().getBaseContext();

            builder.setView(inflater.inflate(R.layout.dialog_add_repo,null))
                    .setPositiveButton("Try", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            EditText usernameInput = (EditText)
                                    getDialog().findViewById(R.id.username_input);
                            String userlogin = usernameInput.getText().toString();
                            EditText reponameInput = (EditText)
                                    getDialog().findViewById(R.id.reponame_input);

                            ContentValues userValues = new ContentValues();
                            userValues.put(DataContract.UsersEntry.USER_LOGIN,
                                    usernameInput.getText().toString());
                            long user_id;
                            Cursor user_idCursor = mContext.getContentResolver().query(
                                    DataContract.UsersEntry.CONTENT_URI.buildUpon().
                                            appendPath(userlogin).build(),
                                    new String[]{DataContract.UsersEntry._ID},
                                    null, null, null, null
                            );
                            if (user_idCursor.moveToFirst()) {
                                user_id = user_idCursor.getLong(0);
                            } else {
                                user_id = DataContract.UsersEntry.getIdFromUri(
                                        mContext.getContentResolver().insert(DataContract.UsersEntry.CONTENT_URI, userValues));
                            }
                            ContentValues repoValues = new ContentValues();
                            repoValues.put(DataContract.ReposEntry.AUTHOR_ID, user_id);
                            repoValues.put(
                                    DataContract.ReposEntry.REPO_PATH,
                                    usernameInput.getText().toString() +
                                            '/' +
                                            reponameInput.getText().toString()
                            );
                            repoValues.put(DataContract.ReposEntry.REPO_NAME,
                                    reponameInput.getText().toString());
                            mContext.getContentResolver().insert(
                                    DataContract.ReposEntry.CONTENT_URI,
                                    repoValues
                            );
                            SyncAdapter.syncImmediately(getActivity());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            return builder.create();
        }
    }
}
