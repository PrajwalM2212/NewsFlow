package com.bhavaneulergmail.newsflow.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class NewsProvider extends ContentProvider {


    private static final int NEWS_ID = 100;
    private static final int NEWS_ITEM_ID = 101;
    private static UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDbHelper mDbHelper;


    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS, NEWS_ID);

        uriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS + "/#", NEWS_ITEM_ID);

        return uriMatcher;

    }


    @Override
    public boolean onCreate() {
        mDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor retCursor = null;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWS_ID:
                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NEWS_ITEM_ID:
                String id = uri.getLastPathSegment();
                String mSelection = NewsContract.NewsEntry._ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(NewsContract.NewsEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported");
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long insertedId;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS_ID:
                insertedId = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported");
        }

        Uri insertedUri = ContentUris.withAppendedId(uri, insertedId);
        getContext().getContentResolver().notifyChange(uri, null);
        return insertedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {


        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case NEWS_ID:
                rowsDeleted = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NEWS_ITEM_ID:
                String id = uri.getLastPathSegment();
                String mSelection = NewsContract.NewsEntry._ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                rowsDeleted = db.delete(NewsContract.NewsEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported");


        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
