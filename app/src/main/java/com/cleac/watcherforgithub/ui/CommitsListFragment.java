package com.cleac.watcherforgithub.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cleac.watcherforgithub.R;
import com.cleac.watcherforgithub.data.DataContract;
import com.cleac.watcherforgithub.data.rest.RestClient;
import com.cleac.watcherforgithub.data.rest.model.committs.CommitsDatum;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by cleac on 4/15/15.
 */
public class CommitsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {
    static final String DETAIL_URI = "URI";
    CommitsListAdapter mCommitsAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private Uri mUri;

    private static final int COMMITS_LOADER = 0;

    private static String[] COMMITS_COLUMNS = {
            DataContract.CommitEntry.TABLE_NAME+'.'+DataContract.CommitEntry._ID,
            DataContract.CommitEntry.SHA
    };

    static final int COMMITS_ID = 0;
    static final int COMMIT_SHA = 1;

    public CommitsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCommitsAdapter = new CommitsListAdapter(getActivity(),null,0);

        Bundle arguments = getArguments();
        mUri = arguments.getParcelable(CommitsListFragment.DETAIL_URI);
        Log.v(this.getClass().getSimpleName(),mUri.toString());

        final View rootView = inflater.inflate(R.layout.fragment_repository, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_commits);
        mListView.setAdapter(mCommitsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RepositoryActivity.class);
                startActivity(intent);
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
        getLoaderManager().initLoader(COMMITS_LOADER,null,this);
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
        if(mUri!=null) {
            String sortOrder = DataContract.CommitEntry.TABLE_NAME + '.' +
                    DataContract.CommitEntry._ID + " ASC";
            return new CursorLoader(getActivity(),
                    mUri,
                    COMMITS_COLUMNS,
                    null,null,null
                    );
/*            return new CursorLoader(getActivity(),
                    mUri,
                    COMMITS_COLUMNS,
                    null,
                    null,
                    sortOrder);*/
        } else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCommitsAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {mCommitsAdapter.swapCursor(null);
    }
}