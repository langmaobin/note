package com.lib.widget.swipegallery;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.lib.R;
import com.lib.utils.SettingsUtil;
import com.lib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import static com.lib.base.BaseConstants.LibReqCode.REQ_CODE_CHECK_PERMISSION;

/**
 * Limitation: If RecyclerView has too many items which some of the items are not bind yet,
 * setting up the shared elements will have problem for activity transition
 */
public class SwipeGalleryActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, View.OnClickListener,
        SwipeGalleryPagerAdapter.OnSwipeGalleryInteractionListener {

    public static final int REQ_CODE_SWIPE_GALLERY = 1111;

    public static final String ARG_IMG_SRC_LIST = "ARG_IMG_SRC_LIST";
    public static final String ARG_IS_URL_PATH = "ARG_IS_URL_PATH";
    public static final String ARG_CURRENT_POSITION = "ARG_CURRENT_POSITION";
    public static final String ARG_GALLERY_TRANSITION = "ARG_GALLERY_TRANSITION";

    private ViewPager vpSwipeGallery;
    private LinearLayout llIndicatorDots;
    private TextView tvPageCount;
    private ImageView[] ivDots;

    private SwipeGalleryPagerAdapter swipeGalleryPagerAdapter;
    private List<String> imgSrcList;
    private boolean isUrlPath;
    private int currentPosition = 0;
    private int dotsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initWindowsFeature();
        checkPermissionNeeded();
    }

    @Override
    public void onBackPressed() {
        backToPreviousScreen();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_CHECK_PERMISSION: {
                boolean isAllPermissionGranted = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isAllPermissionGranted = false;
                        break;
                    }
                }
                if (isAllPermissionGranted) {
                    init();
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        updateDotsIndicatorUi(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Do nothing here...
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do nothing here...
    }

    @Override
    public void onClick(View v) {
        // Do nothing here...
    }

    @Override
    public void onSingleTapOnImage() {
        onBackPressed();
    }

    private void initWindowsFeature() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set screen transition feature
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            getWindow().setSharedElementEnterTransition(new com.lib.transition.DetailTransition(1000, 400));
//            getWindow().setSharedElementExitTransition(new com.lib.transition.DetailTransition(1000, 400));
//            getWindow().setSharedElementReturnTransition(new com.lib.transition.DetailTransition(1000, 400));
        }
        // Show activity in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void checkPermissionNeeded() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (SettingsUtil.hasPermission(this, permissions)) {
            init();
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQ_CODE_CHECK_PERMISSION);
        }
    }

    private void init() {

        setContentView(R.layout.activity_swipe_gallery);

        getIntentData();

        vpSwipeGallery = findViewById(R.id.vp_swipe_gallery);
        llIndicatorDots = findViewById(R.id.ll_indicator_dots);
        tvPageCount = findViewById(R.id.tv_page_count);

        ViewCompat.setTransitionName(vpSwipeGallery, ARG_GALLERY_TRANSITION + currentPosition);

        swipeGalleryPagerAdapter = new SwipeGalleryPagerAdapter(this, imgSrcList, isUrlPath);
        swipeGalleryPagerAdapter.setOnSwipeGalleryInteractionListener(this);

        vpSwipeGallery.setAdapter(swipeGalleryPagerAdapter);
        vpSwipeGallery.setCurrentItem(currentPosition);
        vpSwipeGallery.addOnPageChangeListener(this);

        setDotsIndicatorUi();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            imgSrcList = intent.getStringArrayListExtra(ARG_IMG_SRC_LIST);
            isUrlPath = intent.getBooleanExtra(ARG_IS_URL_PATH, false);
            currentPosition = intent.getIntExtra(ARG_CURRENT_POSITION, 0);
        } else {
            currentPosition = 0;
        }
        if (imgSrcList == null) {
            imgSrcList = new ArrayList<>();
        }
    }

    private void setDotsIndicatorUi() {
        dotsCount = swipeGalleryPagerAdapter.getCount();
        if (dotsCount <= 1 || dotsCount > 10) {
            llIndicatorDots.setVisibility(View.GONE);
        } else {
            ivDots = new ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                ivDots[i] = new ImageView(this);
                ivDots[i].setImageDrawable(ViewUtil.getDrawable(this, R.drawable.swipegallery_dot_normal));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(4, 0, 4, 0);
                llIndicatorDots.addView(ivDots[i], params);
            }
            ivDots[0].setImageDrawable(ViewUtil.getDrawable(this, R.drawable.swipegallery_dot_selected));
        }
        updateDotsIndicatorUi(currentPosition);
    }

    private void updateDotsIndicatorUi(int position) {
        if (llIndicatorDots.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < dotsCount; i++) {
                ivDots[i].setImageDrawable(ViewUtil.getDrawable(this, R.drawable.swipegallery_dot_normal));
            }
            ivDots[position].setImageDrawable(ViewUtil.getDrawable(this, R.drawable.swipegallery_dot_selected));
        }
        if (position < 0 || dotsCount <= 0) {
            tvPageCount.setText(getString(R.string.app_text_page_count_formatted, 1, 1));
        } else {
            tvPageCount.setText(getString(R.string.app_text_page_count_formatted, position + 1, dotsCount));
        }
        ViewCompat.setTransitionName(vpSwipeGallery, ARG_GALLERY_TRANSITION + position);
    }

    private void backToPreviousScreen() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARG_CURRENT_POSITION, vpSwipeGallery.getCurrentItem());
        setResult(Activity.RESULT_OK, resultIntent);
        supportFinishAfterTransition();
    }

//    private void goToNextItem() {
//        vpSwipeGallery.setCurrentItem((vpSwipeGallery.getCurrentItem() < dotsCount)
//                ? vpSwipeGallery.getCurrentItem() + 1 : 0);
//    }
//
//    private void goToPreviousItem() {
//        vpSwipeGallery.setCurrentItem((vpSwipeGallery.getCurrentItem() > 0)
//                ? vpSwipeGallery.getCurrentItem() - 1 : dotsCount - 1);
//    }
//
//    private void backToPreviousScreen() {
//        finish();
//    }
//
//    private void setItemNavVisibility() {
//        if (position + 1 == dotsCount) {
//            btnNext.setVisibility(View.GONE);
//            btnFinish.setVisibility(View.VISIBLE);
//        } else {
//            btnNext.setVisibility(View.VISIBLE);
//            btnFinish.setVisibility(View.GONE);
//        }
//    }

}