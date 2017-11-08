package com.bhavaneulergmail.newsflow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhavaneulergmail.newsflow.R;
import com.bhavaneulergmail.newsflow.templates.SourceInfo;

import java.util.ArrayList;

/**
 * Created by prajwalm on 20/10/17.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SourceInfo> mSourceInfoArrayList;
    private ClickInterfaceSource mClickInterfaceSource;


    public interface ClickInterfaceSource {
        void clickListenerSource(int position);
    }


    public SourceAdapter(Context context, ArrayList<SourceInfo> sourceInfoArrayList, ClickInterfaceSource clickInterfaceSource) {

        this.mContext = context;
        this.mSourceInfoArrayList = sourceInfoArrayList;
        this.mClickInterfaceSource = clickInterfaceSource;

    }

    @Override
    public SourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SourceAdapter.ViewHolder holder, int position) {

        SourceInfo sourceInfo = mSourceInfoArrayList.get(position);

        TextView sourceName = holder.sourceName;
        TextView sourceCategory = holder.sourceCategory;

        sourceName.setText(sourceInfo.sourceName);
        sourceCategory.setText(sourceInfo.sourceCategory);

    }

    @Override
    public int getItemCount() {
        return mSourceInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView sourceName;
        public TextView sourceCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            sourceName = itemView.findViewById(R.id.source_name);
            sourceCategory = itemView.findViewById(R.id.source_category);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            mClickInterfaceSource.clickListenerSource(position);

        }
    }
}
