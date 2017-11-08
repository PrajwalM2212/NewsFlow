package com.bhavaneulergmail.newsflow.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhavaneulergmail.newsflow.R;
import com.bhavaneulergmail.newsflow.data.NewsContract;
import com.bhavaneulergmail.newsflow.templates.NewsInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<NewsInfo> mNewsInfoArrayList;
    private Context mContext;
    private ClickInterfaceNews mClickInterfaceNews;
    private String mTitle;
    private String mAuthor;
    private String mUrl;

    public interface ClickInterfaceNews {
        void onClick(int position);
    }


    public NewsAdapter(ArrayList<NewsInfo> newsInfoArrayList, Context context, ClickInterfaceNews clickInterfaceNews) {
        this.mNewsInfoArrayList = newsInfoArrayList;
        this.mContext = context;
        this.mClickInterfaceNews = clickInterfaceNews;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final NewsInfo newsInfo = mNewsInfoArrayList.get(position);

        final TextView titleView;
        titleView = holder.titleView;
        titleView.setText(newsInfo.title);

        // TextView dateView;
        // dateView = holder.dateView;
        // dateView.setText(newsInfo.publishedAt);

        final TextView authorView = holder.authorView;
        if (newsInfo.author.equals("null")) {
            newsInfo.author = "Unknown";
        }
        authorView.setText(newsInfo.author);


        TextView descriptionView = holder.descriptionView;
        if (newsInfo.description.equals("null")) {
            newsInfo.description = "Not Available";
        }
        descriptionView.setText(newsInfo.description);

        ImageView imageView = holder.imageView;
        Picasso.with(mContext).load(newsInfo.urlToImage).into(imageView);


        final ImageButton button = holder.button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(button);
                mTitle = titleView.getText().toString();
                mAuthor = authorView.getText().toString();
                mUrl = newsInfo.url;

            }
        });


    }

    @Override
    public int getItemCount() {
        return mNewsInfoArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleView;
        //TextView dateView;
        TextView descriptionView;
        TextView authorView;
        ImageView imageView;
        ImageButton button;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            //dateView = itemView.findViewById(R.id.date);
            descriptionView = itemView.findViewById(R.id.description);
            authorView = itemView.findViewById(R.id.author);
            imageView = itemView.findViewById(R.id.image);
            button = itemView.findViewById(R.id.but);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickInterfaceNews.onClick(position);

        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.pop_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MenuItemClickListener());
        popupMenu.show();

    }

    class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.save:

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NewsContract.NewsEntry.HEADLINE, mTitle);
                    contentValues.put(NewsContract.NewsEntry.AUTHOR, mAuthor);
                    contentValues.put(NewsContract.NewsEntry.NEWS_URL, mUrl);

                    Uri insertedUri = mContext.getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, contentValues);
                    Toast.makeText(mContext, insertedUri.toString(), Toast.LENGTH_SHORT).show();

                    return true;

                case R.id.share:

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, mTitle + "\n" + mUrl);
                    mContext.startActivity(Intent.createChooser(sharingIntent, "Share Via"));
                    return true;

            }
            return true;
        }
    }

}


