package com.mohammedhemaid.travelapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammedhemaid.travelapp.view.RowNote;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecycleViewAdapter";
    private Context mContext;
    private List<?> mData = new ArrayList();
    private int recycleViewRes;
    private Listener listener;

    public RecycleViewAdapter(Context mContext, Listener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        Log.d(TAG, "onCreateViewHolder: ");
        view = LayoutInflater.from(mContext).inflate(recycleViewRes, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: outside " + position);

        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(listener, mData.get(position), position);
            Log.d(TAG, "onBindViewHolder: " + position);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mData.size());
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //

        View customView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            customView = itemView;

        }

        private void bind(Listener listener, Object o, int position) {

            if (customView instanceof IBind) {

                ((IBind) customView).bind(listener, o, position);
            }
        }

    }

    public void setRecycleViewRes(int recycleViewRes) {
        this.recycleViewRes = recycleViewRes;
    }

    public void setData(List<?> mData) {
        this.mData = mData;
        Log.d(TAG, "setData: " + this.mData.size());
        notifyDataSetChanged();
    }

    public interface IBind {
        void bind(Listener listener, Object o, int position);
    }


    public interface Listener {

        void onEditClick(Object o);

        void onDeleteClick(Object o);

        void onError();
    }
}