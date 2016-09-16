package com.example.photoeditor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: 30.06.16
 * Time: 19:07
 *
 * @author Olga
 */
@SuppressWarnings("unused")
public abstract class CollectionRecycleAdapter<T> extends RecyclerView.Adapter<CollectionRecycleAdapter.RecycleViewHolder> {
    protected LayoutInflater mInflater;
    private Context mContext;
    private final List<T> mList;

    public CollectionRecycleAdapter(Context context) {
        mContext = context.getApplicationContext();
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        //noinspection unchecked
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    protected Context getContext() {
        return mContext;
    }

    public void setCollection(Collection<? extends T> collection) {
        mList.clear();

        if (collection != null) {
            mList.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addToCollection(Collection<T> items) {
        if (items != null) {
            final int from = mList.size();
            mList.addAll(items);
            final int to = mList.size();

            notifyItemRangeInserted(from, to);
        }
    }

    public void clearCollection() {
        if (mList != null) {
            int count = mList.size();
            mList.clear();
            notifyItemRangeRemoved(0, count);
        }
    }

    public void removeItem(int index) {
        if (mList != null) {
            notifyItemRemoved(index);
            mList.remove(index);
        }
    }

    public List<T> getCollection() {
        return mList;
    }

    /**
     * Abstract view holder for extending.
     */
    public abstract static class RecycleViewHolder<T> extends RecyclerView.ViewHolder {
        public RecycleViewHolder(View itemView) {
            super(itemView);
            create(itemView);
        }

        protected abstract void create(View rootView);

        public abstract void bind(T model);

        final public View getRoot() {
            return itemView;
        }
    }

}
