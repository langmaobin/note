package com.android.notes.sample.adapter;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.notes.sample.utils.SkinSelectorUtils;
import com.lib.utils.ValidUtil;
import com.lib.widget.VerticalLoadMoreRecyclerViewAdapter;
import com.android.notes.sample.R;
import com.android.notes.sample.model.AutoModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAutoAdapter extends VerticalLoadMoreRecyclerViewAdapter<AutoModel> {
    private OnClickListener onClickListener;

    public MyAutoAdapter(Context context, OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public void loadMoreItems() {
        if (onClickListener != null) {
            onClickListener.onItemFMasterLoadMore();
        }
    }

    @Override
    public void retryItems() {
        if (onClickListener != null) {
            onClickListener.onItemFMasterLoadRetry();
        }
    }

    @Override
    public RecyclerView.ViewHolder createView(Context context, ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_auto, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, AutoModel item, int position) {
        if (!ValidUtil.isEmpty(item)) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (item.getContent().length()<=20){
                viewHolder.textViewMasterName.setText(item.getContent());
            }else {
                String first_20_characters = item.getContent().substring(0, 20);
                viewHolder.textViewMasterName.setText(first_20_characters);
            }
            SkinSelectorUtils.setGradientAndCornerAndStroker(viewHolder.relativeLayout,50, "2f2b5c","3f1e61","404040", GradientDrawable.Orientation.LEFT_RIGHT);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onItemMasterClick(item, position);
                    }
                }
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.relative_layout)
        RelativeLayout relativeLayout;
        @BindView(R.id.text_view_master_name)
        AppCompatTextView textViewMasterName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface OnClickListener {
        void onItemMasterClick(AutoModel masterModel, int position);

        void onItemFMasterLoadMore();

        void onItemFMasterLoadRetry();

    }

}