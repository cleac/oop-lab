package com.cleac.watcherforgithub.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.cleac.watcherforgithub.R;

/**
 * Created by cleac on 4/15/15.
 */
public class CommitsListAdapter extends CursorAdapter {
    public static class ViewHolder {
        public final TextView commitSha;
        public final TextView author;

        public ViewHolder(View view) {
            commitSha = (TextView) view.findViewById(R.id.commit_sha);
            author = (TextView) view.findViewById(R.id.commit_author);
        }
    }

    public CommitsListAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.commit_list_node;
        View view = LayoutInflater.from(context).inflate(layoutId,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String commitSHA = cursor.getString(CommitsListFragment.COMMIT_SHA);
        viewHolder.commitSha.setText(commitSHA.substring(0,5)+"...");
    }
}