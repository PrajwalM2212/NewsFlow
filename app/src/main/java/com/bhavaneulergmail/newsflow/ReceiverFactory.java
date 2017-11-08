package com.bhavaneulergmail.newsflow;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.bhavaneulergmail.newsflow.data.NewsContract;
import com.bhavaneulergmail.newsflow.templates.SourceInfo;
import com.bhavaneulergmail.newsflow.utils.JsonFetch;
import com.bhavaneulergmail.newsflow.utils.NetworkConnect;

import java.io.IOException;
import java.util.ArrayList;


public class ReceiverFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
    private Intent intent;
    private ArrayList<SourceInfo> sourceInfos = null;


    private ArrayList<String> arrayList;

    public ReceiverFactory(Context context, Intent intent) {
        mContext = context;
        this.intent = intent;
    }


    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {
        String jsonResponse;

        if (NetworkConnect.checkNetwork(mContext)) {
            try {
                jsonResponse = NetworkConnect.makeHttpRequest("https://newsapi.org/v1/sources?apiKey=" + MainActivity.API_KEY);
                sourceInfos = JsonFetch.getSourceInfo(jsonResponse);
            } catch (IOException e) {

                e.printStackTrace();
            }

        } else {

            sourceInfos = new ArrayList<>();
            SourceInfo sourceInfo = new SourceInfo();
            sourceInfo.sourceCategory = "No Network";
            sourceInfo.sourceId = "No Network";
            sourceInfo.sourceName = "No Network";
            sourceInfos.add(sourceInfo);


        }
    }

    @Override
    public void onDestroy() {

        //mCursor.close();

    }

    @Override
    public int getCount() {

        return sourceInfos.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_list_widget);
        remoteViews.setTextViewText(R.id.text_wid, sourceInfos.get(position).sourceName);


        Bundle extras = new Bundle();
        extras.putString(MainActivity.SOURCE_ID, sourceInfos.get(position).sourceId);
        extras.putString(MainActivity.NUM_ID, Integer.toString(position));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.item_container, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
