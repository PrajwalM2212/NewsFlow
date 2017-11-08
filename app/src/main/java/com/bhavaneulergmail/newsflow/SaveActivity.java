package com.bhavaneulergmail.newsflow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bhavaneulergmail.newsflow.adapters.SaveAdapter;
import com.bhavaneulergmail.newsflow.data.NewsContract;
import com.bhavaneulergmail.newsflow.utils.NetworkConnect;

public class SaveActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>, SaveAdapter.ClickInterface {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SaveAdapter mSaveAdapter;
    private Cursor cursorData;
    private Parcelable mListState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_save);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_save);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels / displayMetrics.density;
        float height = displayMetrics.heightPixels / displayMetrics.density;

        if (width > 600 && height > 480) {
            mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        } else {
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = (int) viewHolder.itemView.getTag();
                String swipePosition = Integer.toString(position);
                Uri uri = NewsContract.NewsEntry.CONTENT_URI.buildUpon().appendPath(swipePosition).build();
                getContentResolver().delete(uri, null, null);
                getLoaderManager().restartLoader(0, null, SaveActivity.this);


            }
        }).attachToRecyclerView(mRecyclerView);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.content.AsyncTaskLoader<Cursor>(this) {

            public void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                return getContentResolver().query(NewsContract.NewsEntry.CONTENT_URI, null, null, null, null);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mSaveAdapter = new SaveAdapter(this, data, this);
        mRecyclerView.setAdapter(mSaveAdapter);
        cursorData = data;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(int position) {

        cursorData.moveToPosition(position);
        String url = cursorData.getString(cursorData.getColumnIndex(NewsContract.NewsEntry.NEWS_URL));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (NetworkConnect.checkNetwork(this)) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.NoNetwork, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("SAVE_STATE", mListState);

    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        if (outState != null) {
            mListState = outState.getParcelable("SAVE_STATE");
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
