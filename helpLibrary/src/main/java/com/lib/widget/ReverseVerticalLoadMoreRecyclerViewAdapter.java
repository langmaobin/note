package com.lib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.R;
import com.lib.base.BaseRecyclerViewAdapter;
import com.lib.utils.ValidUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Extends this adapter class to implements load more feature
 * <p/>
 * rv.addOnScrollListener()
 * -> adapter.scrollItem()
 * <p/>
 * adapter.setLoadingItemView() <- call before load data
 * adapter.loadMoreData() <- call after data finish loaded
 * adapter.setRetryItemView() <- call if there is error occurs
 *
 * @param <T> generic type data to be set into the list
 */
public abstract class ReverseVerticalLoadMoreRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder, T> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_LAST = 2;
    private final int VIEW_TYPE_RETRY = 3;

    protected boolean isLoading;
    protected boolean isLast;
    protected boolean isRetry;

    private boolean showPlaceholderAlt;

    public ReverseVerticalLoadMoreRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(context).inflate(R.layout.item_view_loading, viewGroup, false);
                return new ViewHolderLoading(view);
            case VIEW_TYPE_LAST:
                view = LayoutInflater.from(context).inflate(R.layout.item_view_last, viewGroup, false);
                return new ViewHolderLast(view);
            case VIEW_TYPE_RETRY:
                view = LayoutInflater.from(context).inflate(R.layout.item_view_tap_to_retry, viewGroup, false);
                return new ViewHolderRetry(view);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null && !showPlaceholder && !showPlaceholderAlt) {
            if (isLast) {
                return VIEW_TYPE_LAST;
            } else if (isRetry) {
                return VIEW_TYPE_RETRY;
            } else {
                return VIEW_TYPE_LOADING;
            }
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() || getItem(0) == null;
    }

    @Override
    public void addItem(int position, T item) {
        if (list != null && position >= 0) {
            list.add(position, item);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public void removeItem(int position) {
        if (isPositionValid(position)) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            if (isLastItemNull()) {
                list.remove(0);
                notifyItemRemoved(0);
                notifyItemRangeChanged(0, getItemCount());
            }
        }
    }

    @Override
    public void setList(List<T> list) {
        if (list != null) {
            this.isLast = false;
            this.isLoading = false;
            this.isRetry = false;
            Collections.reverse(list);
            LinkedHashSet<T> linkedHashSet = new LinkedHashSet<>();
            linkedHashSet.addAll(list);
            list.clear();
            list.addAll(linkedHashSet);
            this.list = new ArrayList<>(list);
            notifyDataSetChanged();
        }
    }

    /**
     * Put under RecyclerView addOnScrollListener -> onScrolled method
     *
     * @param layoutManager rv.getLayoutManager()
     */

    public void scrollItem(RecyclerView.LayoutManager layoutManager) {
        scrollItem(layoutManager, -1);
    }

    public void scrollItem(RecyclerView.LayoutManager layoutManager, int offset) {
        int firstVisibleItem = -1;
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        }
        if (!isLast && !isLoading && !isRetry && firstVisibleItem == 0) {
            int itemCount = getItemCount();
            if (offset > 0) {
                // Check is item multiple of 10 (based on x offset per page)
                if (offset % 10 == 0) {
                    loadMoreItems();
                } else {
                    setLastItemView();
                }
            } else {
                if (itemCount > 0) {
                    loadMoreItems();
                }
            }
            isLoading = true;
        }
    }

    /**
     * Call when the data finish loaded
     * (If list is null or empty, last item view will be show as a result)
     */
    public void loadMoreData(List<T> list) {
        isLast = false;
        isRetry = false;
        removeNonListItemView();
        if (ValidUtil.isEmpty(list)) {
            setLastItemView();
        } else {
            int originalItemCount = this.list.size();
            Collections.reverse(list);
            this.list.addAll(0, list);
            LinkedHashSet<T> linkedHashSet = new LinkedHashSet<>();
            linkedHashSet.addAll(this.list);
            int newItemCount = linkedHashSet.size() - originalItemCount;
            this.list.clear();
            this.list.addAll(linkedHashSet);
//            notifyItemRangeChanged(startPosition, getItemCount());
            notifyItemRangeInserted(0, newItemCount);
        }
        isLoading = false;
    }

    /**
     * Call when the data is loading
     */
    public void setLoadingItemView() {
        addNonListItemView();
    }

    /**
     * Call when there is error occurs while loading
     */
    public void setRetryItemView() {
        isRetry = true;
        removeNonListItemView();
        addNonListItemView();
    }

    public void setLastItemView() {
        isLast = true;
        addNonListItemView();
    }

    private void addNonListItemView() {
        list.add(0, null);
        notifyItemInserted(0);
    }

    private void removeNonListItemView() {
        if (isLoading) {
            list.remove(0);
            notifyItemRemoved(0);
        }
    }

    private void tapToRetry() {
        isLoading = true;
        isRetry = false;
        removeNonListItemView();
        retryItems();
    }

    private boolean isLastItemNull() {
        return getItemCount() > 0 && getItem(0) == null;
    }

    private class ViewHolderLoading extends RecyclerView.ViewHolder {
        ViewHolderLoading(View view) {
            super(view);
        }
    }

    private class ViewHolderLast extends RecyclerView.ViewHolder {
        ViewHolderLast(View view) {
            super(view);
        }
    }

    private class ViewHolderRetry extends RecyclerView.ViewHolder {

        RelativeLayout rlTapToRetry;

        ViewHolderRetry(View view) {
            super(view);
            rlTapToRetry = view.findViewById(R.id.rl_tap_to_retry);
            rlTapToRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tapToRetry();
                }
            });
        }

    }

    /**
     * Called when list has scroll to bottom
     */
    public abstract void loadMoreItems();

    /**
     * Called when click on retry item
     */
    public abstract void retryItems();

}