package com.bhavaneulergmail.newsflow.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhavaneulergmail.newsflow.R;
import com.bhavaneulergmail.newsflow.data.NewsContract;

/**
 * Created by prajwalm on 31/10/17.
 */

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private ClickInterface mClickInterface;
    private String mUrl;

    public interface ClickInterface {
        void onClick(int position);
    }

    public SaveAdapter(Context context, Cursor cursor, ClickInterface clickInterface) {
        this.mContext = context;
        this.mCursor = cursor;
        this.mClickInterface = clickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView titleView = holder.titleView;
        TextView authorView = holder.authorView;

        String title = null;
        String author = null;

        mCursor.moveToPosition(position);
        {
            title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.HEADLINE));
            author = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.AUTHOR));
            holder.itemView.setTag(mCursor.getInt(mCursor.getColumnIndex(NewsContract.NewsEntry._ID)));
        }

        titleView.setText(title);
        authorView.setText(author);


    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleView;
        public TextView authorView;


        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.save_title);
            authorView = itemView.findViewById(R.id.save_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickInterface.onClick(getAdapterPosition());

        }
    }

}
