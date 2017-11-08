package com.bhavaneulergmail.newsflow;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.bhavaneulergmail.newsflow.adapters.NewsAdapter;
import com.bhavaneulergmail.newsflow.adapters.SourceAdapter;
import com.bhavaneulergmail.newsflow.data.NewsContract;
import com.bhavaneulergmail.newsflow.templates.NewsInfo;
import com.bhavaneulergmail.newsflow.templates.SourceInfo;
import com.bhavaneulergmail.newsflow.utils.JsonFetch;
import com.bhavaneulergmail.newsflow.utils.NetworkConnect;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SourceAdapter.ClickInterfaceSource, NewsAdapter.ClickInterfaceNews {


    //To Run the App please get your apiKey from https://newsapi.org and insert it below in API_KEY
    public static final String API_KEY = "Insert your ApiKey here";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mNewsLayoutManager;
    private SourceAdapter mSourceAdapter;
    private static final String SOURCE = "source";
    public String mSourceId = "techcrunch";
    private ArrayList<SourceInfo> mSourceInfoArrayList;
    private ArrayList<NewsInfo> mNewsInfoArrayList;
    private RecyclerView mRecyclerViewNews;
    private NewsAdapter mNewsAdapter;
    private static final String STATE = "state";
    private Parcelable mListState;
    private Parcelable mNewsListState;
    private static final String STATE_NEWS = "state_news";
    private SharedPreferences sharedPreferences;
    private AdView mAdView;
    private static final String SELECT = "select";
    private int num = 3;
    private float width;
    private float height;
    public static final String SOURCE_ID = "sourceIds";
    public static final String NUM_ID = "numIds";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_source);
        mRecyclerViewNews = (RecyclerView) findViewById(R.id.recycler_view_news);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / displayMetrics.density;
        height = displayMetrics.heightPixels / displayMetrics.density;

        if (width > 600 && height > 480) {

            mNewsLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        } else {
            mNewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }


        sharedPreferences = getSharedPreferences("shared", 0);

        if (getIntent().getStringExtra(NUM_ID) != null) {
            String id = getIntent().getStringExtra(NUM_ID);
            num = Integer.parseInt(id);
        } else {
            num = sharedPreferences.getInt(SELECT, 3);
        }
        mLayoutManager.scrollToPosition(num);

        if (getIntent().getStringExtra(SOURCE_ID) != null) {
            mSourceId = getIntent().getStringExtra(SOURCE_ID);
        } else {

            mSourceId = sharedPreferences.getString(SOURCE, "techcrunch");

            Toast.makeText(this, mSourceId, Toast.LENGTH_SHORT).show();
        }

        if (NetworkConnect.checkNetwork(this)) {

            new Task().execute();

            new NewsTask().execute();
        } else {


            Toast.makeText(this, R.string.NoNetwork, Toast.LENGTH_LONG).show();

        }


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        if (NetworkConnect.checkNetwork(this)) {
            mAdView.loadAd(adRequest);
        }


    }

    @Override
    public void clickListenerSource(int position) {

        SourceInfo sourceInfo = mSourceInfoArrayList.get(position);
        mSourceId = sourceInfo.sourceId;
        num = position;
        if (NetworkConnect.checkNetwork(this)) {

            new NewsTask().execute();
        } else {


            Toast.makeText(this, R.string.NoNetwork, Toast.LENGTH_LONG).show();

        }

        //Toast.makeText(this, "Hare Krishna World" + position  + mSourceId, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(int position) {

        NewsInfo newsInfo = mNewsInfoArrayList.get(position);


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsInfo.url));
        if (NetworkConnect.checkNetwork(this)) {

            startActivity(intent);

        } else {


            Toast.makeText(this, R.string.NoNetwork, Toast.LENGTH_LONG).show();

        }

    }


    class Task extends AsyncTask<Void, Void, ArrayList<SourceInfo>> {

        @Override
        protected ArrayList<SourceInfo> doInBackground(Void... params) {

            ArrayList<SourceInfo> sourceInfos = null;
            String jsonResponse = null;

            try {

                jsonResponse = NetworkConnect.makeHttpRequest("https://newsapi.org/v1/sources?apiKey=" + API_KEY);


                sourceInfos = JsonFetch.getSourceInfo(jsonResponse);
            } catch (IOException e) {

                e.printStackTrace();
            }
            return sourceInfos;
        }


        protected void onPostExecute(ArrayList<SourceInfo> data) {

            mSourceAdapter = new SourceAdapter(MainActivity.this, data, MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mSourceAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.HORIZONTAL));

            mSourceInfoArrayList = data;
        }
    }


    public String createUrl(String queryString) {

        Uri.Builder builder = Uri.parse(queryString).buildUpon();
        builder.appendQueryParameter(SOURCE, mSourceId);
        Uri uri = builder.build();

        return uri.toString();


    }


    class NewsTask extends AsyncTask<Void, Void, ArrayList<NewsInfo>> {

        @Override
        protected ArrayList<NewsInfo> doInBackground(Void... params) {

            ArrayList<NewsInfo> newsInfos = null;
            String jsonResponse;

            try {
                jsonResponse = NetworkConnect.makeHttpRequest(createUrl("https://newsapi.org/v1/articles?apiKey=" + API_KEY));


                newsInfos = JsonFetch.getNewsInfo(jsonResponse);


            } catch (IOException e) {

                e.printStackTrace();

            }


            return newsInfos;


        }


        protected void onPostExecute(ArrayList<NewsInfo> data) {

            mNewsAdapter = new NewsAdapter(data, MainActivity.this, MainActivity.this);
            //mRecyclerViewNews.setLayoutManager(mNewsLayoutManager);
            mRecyclerViewNews.setAdapter(mNewsAdapter);

            mRecyclerViewNews.setLayoutManager(mNewsLayoutManager);

            int resId = R.anim.layout_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(MainActivity.this, resId);
            mRecyclerViewNews.setLayoutAnimation(animation);
            mNewsInfoArrayList = data;


        }
    }


    @Override
    protected void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putString("sor", mSourceId);

        mListState = mLayoutManager.onSaveInstanceState();
        savedState.putParcelable(STATE, mListState);


        mNewsListState = mNewsLayoutManager.onSaveInstanceState();
        savedState.putParcelable(STATE_NEWS, mNewsListState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle outState) {

        super.onRestoreInstanceState(outState);

        if (outState != null) {
            mListState = outState.getParcelable(STATE);
            mNewsListState = outState.getParcelable(STATE_NEWS);

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }

        if (mNewsListState != null) {
            mNewsLayoutManager.onRestoreInstanceState(mNewsListState);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();


        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOURCE, mSourceId);
        editor.putInt(SELECT, num);
        editor.commit();


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {


        if (menuItem.getItemId() == R.id.next) {
            Intent intent = new Intent(MainActivity.this, SaveActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(menuItem);
    }

//  /Users/prajwalm/Downloads/NewsFlow
}
