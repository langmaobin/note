package com.lib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<VH, T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public List<T> list;

    protected boolean showPlaceholder;
    private int placeholderLayoutId = -1;
    private int placeholderSize;

    public BaseRecyclerViewAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (showPlaceholder && placeholderLayoutId != -1) {
            View view = LayoutInflater.from(context).inflate(placeholderLayoutId, viewGroup, false);
            return new ViewHolderPlaceholder(view);
        }
        return createView(context, viewGroup, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!showPlaceholder) {
            bindView((VH) holder, getItem(position), position);
        }
    }

    @Override
    public int getItemCount() {
        if (showPlaceholder) return placeholderSize;
        return list != null ? list.size() : 0;
    }

    public boolean isShowPlaceholder() {
        return showPlaceholder;
    }

    public void showPlaceholder() {
        this.showPlaceholder = true;
        notifyDataSetChanged();
    }

    public void hidePlaceholder() {
        this.showPlaceholder = false;
    }

    public void setupPlaceholder(int placeholderLayoutId, int placeholderSize) {
        this.placeholderLayoutId = placeholderLayoutId;
        this.placeholderSize = placeholderSize;
    }

    public boolean isEmpty() {
        return getItemCount() <= 0;
    }

    public void clear() {
        if (!isEmpty()) {
            list.clear();
            notifyDataSetChanged();
        }
    }

    public void addItem(int position, T item) {
        if (list != null && position >= 0) {
            list.add(position, item);
            notifyDataSetChanged();
//            notifyItemInserted(position);
//            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void removeItem(int position) {
        if (isPositionValid(position)) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void removeItemWithoutAnimation(int position) {
        if (isPositionValid(position)) {
            list.remove(position);
            notifyDataSetChanged();
        }
    }

    public void updateItem(int position, T item) {
        if (isPositionValid(position)) {
            list.set(position, item);
            notifyItemChanged(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public T getItem(int position) {
        return isPositionValid(position) ? list.get(position) : null;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        if (list != null) {
            LinkedHashSet<T> linkedHashSet = new LinkedHashSet<>();
            linkedHashSet.addAll(list);
            list.clear();
            list.addAll(linkedHashSet);
            this.list = new ArrayList<>(list);
            notifyDataSetChanged();
        }
    }

    protected boolean isPositionValid(int position) {
        return list != null && position >= 0 && position < list.size();
    }

    protected class ViewHolderPlaceholder extends RecyclerView.ViewHolder {
        ViewHolderPlaceholder(View itemView) {
            super(itemView);
        }
    }

    public abstract RecyclerView.ViewHolder createView(Context context, ViewGroup viewGroup, int viewType);

    public abstract void bindView(VH holder, T item, int position);

}