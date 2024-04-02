package com.lib.widget.swipegallery;


import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.lib.R;
import com.lib.utils.MediaUtil;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ValidUtil;
import com.lib.widget.imageview.TouchImageView;
import com.squareup.picasso.Callback;

import java.util.List;

class SwipeGalleryPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> imgSrcList;
    private boolean isUrlPath;

    SwipeGalleryPagerAdapter(Context context, List<String> imgSrcList, boolean isUrlPath) {
        this.context = context;
        this.imgSrcList = imgSrcList;
        this.isUrlPath = isUrlPath;
    }

    @Override
    public int getCount() {
        return imgSrcList != null ? imgSrcList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_swipe_gallery, container, false);

        final RelativeLayout rlProgressBar = view.findViewById(R.id.rl_progressbar);
        TouchImageView imageViewSwipeGalleryItem = view.findViewById(R.id.image_view_swipe_gallery_item);
//        ImageView imageViewSwipeGalleryPlaceholder = (ImageView) view.findViewById(R.id.image_view_swipe_gallery_placeholder);

        imageViewSwipeGalleryItem.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if (onSwipeGalleryInteractionListener != null) {
                    onSwipeGalleryInteractionListener.onSingleTapOnImage();
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }

        });
//        imageViewSwipeGalleryPlaceholder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onSwipeGalleryInteractionListener != null) {
//                    onSwipeGalleryInteractionListener.onSingleTapOnImage();
//                }
//            }
//        });

        String imgSrc = imgSrcList.get(position);

//        MediaUtil.loadPicassoImgOriginalWithGifPlaceholder(context, imgSrc, imageViewSwipeGalleryItem, imageViewSwipeGalleryPlaceholder);

        int defaultImg = R.drawable.img_placeholder;

        if (isUrlPath && !ValidUtil.isEmpty(imgSrc)) {
            rlProgressBar.setVisibility(View.VISIBLE);
            MediaUtil.loadPicassoImgWithProgressbar(context, imgSrc, imageViewSwipeGalleryItem, new Callback() {

                @Override
                public void onSuccess() {
                    rlProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    rlProgressBar.setVisibility(View.GONE);
                }

            });
        } else if (SettingsUtil.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Bitmap bitmap = MediaUtil.getImageFromFilePath(imgSrc);
            if (bitmap != null) {
                imageViewSwipeGalleryItem.setImageBitmap(bitmap);
            } else {
                imageViewSwipeGalleryItem.setImageResource(defaultImg);
            }
        } else {
            imageViewSwipeGalleryItem.setImageResource(defaultImg);
        }

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private OnSwipeGalleryInteractionListener onSwipeGalleryInteractionListener;

    void setOnSwipeGalleryInteractionListener(OnSwipeGalleryInteractionListener onSwipeGalleryInteractionListener) {
        this.onSwipeGalleryInteractionListener = onSwipeGalleryInteractionListener;
    }

    interface OnSwipeGalleryInteractionListener {
        void onSingleTapOnImage();
    }

}
