package com.cleac.watcherforgithub.ui;

import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cleac.watcherforgithub.R;
import com.cleac.watcherforgithub.data.DataContract;

import java.util.ArrayList;

/**
 * Created by cleac on 4/15/15.
 */
public class RepositoryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    RepositoryListAdapter mReposAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private static final int REPOS_LOADER = 0;

    private static String[] REPOS_COLUMNS = {
            DataContract.ReposEntry.TABLE_NAME+'.'+DataContract.ReposEntry._ID,
            DataContract.ReposEntry.REPO_PATH,
            DataContract.ReposEntry.REPO_NAME
    };

    static final int REPO_ID = 0;
    static final int REPO_PATH = 1;
    static final int REPO_NAME = 2;

    public interface Callback {
        public void onItemSelected(Uri uri);
    }

    public RepositoryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mReposAdapter = new RepositoryListAdapter(getActivity(),null,0);

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_repos);
        mListView.setAdapter(mReposAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(
                                    DataContract.ReposEntry.buildRepoUri(cursor.getString(REPO_NAME))
                            );
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(REPOS_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder =  DataContract.ReposEntry.TABLE_NAME+'.'+DataContract.ReposEntry._ID + " ASC";
        return new CursorLoader(getActivity(),
                DataContract.ReposEntry.CONTENT_URI,
                REPOS_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mReposAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mReposAdapter.swapCursor(null);
    }
}