package com.android.notes.sample;


import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_TO_ACTIVITY;
import static com.ywvision.wyvisionhelper.app.AppConstants.OTHER_ACTIVITY.OTHER_EMPTY;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.android.notes.sample.app.AppBaseActivity;
import com.android.notes.sample.fragment.HomeFragment;
import com.android.notes.sample.fragment.SummaryFragment;
import com.android.notes.sample.manager.BaseManager;
import com.android.notes.sample.utils.SkinSelectorUtils;
import com.android.notes.sample.widget.CheckableTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class SampleActivity extends AppBaseActivity {
    @BindView(R.id.linear_layout_tab)
    LinearLayout linearLayoutTab;
    @BindView(R.id.linear_layout_home)
    LinearLayout linearLayoutHome;
    @BindView(R.id.image_view_home)
    ImageView imageViewHome;
    @BindView(R.id.text_view_home)
    CheckableTextView textViewHome;
    @BindView(R.id.linear_layout_summary)
    LinearLayout linearLayoutCourse;
    @BindView(R.id.image_view_summary)
    ImageView imageViewCourse;
    @BindView(R.id.text_view_summary)
    CheckableTextView textViewCourse;
    @BindView(R.id.linear_layout_add)
    LinearLayout linearLayoutLive;
    @BindView(R.id.image_view_add)
    ImageView imageViewLive;

    private HomeFragment homeFragment;
    private SummaryFragment summaryFragment;
    private Fragment currentFragment;

    @Override
    public void onSetupView() {
        super.onSetupView();
        setLayoutView(R.layout.activity_main);
        setFrameLayoutView(R.id.main_fragment_content);
    }

    @Override
    public void onBindData() {
        super.onBindData();
        baseManager = new BaseManager(this);
        init();
        initSkinColor();
    }

    @OnClick({
            R.id.linear_layout_home,
            R.id.linear_layout_summary,
            R.id.linear_layout_add,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_home:
                transformPanel(homeFragment, view);
                break;
            case R.id.linear_layout_summary:
                transformPanel(summaryFragment, view);
                break;
            case R.id.linear_layout_add:
                switchActivity(OTHER_EMPTY);
                break;
        }
    }


    private void init() {
        homeFragment = HomeFragment.newInstance();
        summaryFragment = SummaryFragment.newInstance();
        transformPanel(homeFragment, linearLayoutHome);
    }

    public void viewTabClick(ImageView imageView, CheckableTextView textView, boolean click) {
        imageView.setSelected(click);
        textView.setChecked(click);
    }

    private void transformPanel(Fragment toPanel, View view) {

        viewTabClick(imageViewHome, textViewHome, false);
        viewTabClick(imageViewCourse, textViewCourse, false);
        switch (view.getId()) {
            case R.id.linear_layout_home:
                viewTabClick(imageViewHome, textViewHome, true);
                break;
            case R.id.linear_layout_summary:
                viewTabClick(imageViewCourse, textViewCourse, true);
                break;
        }

        if (!toPanel.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_content, toPanel)
                    .hide(toPanel)
                    .commit();
        }

        if (currentFragment != toPanel) {
            if (currentFragment == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(toPanel)
                        .disallowAddToBackStack()
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .show(toPanel)
                        .disallowAddToBackStack()
                        .commit();
            }
            currentFragment = toPanel;
        }
    }

    public void switchActivity(String type) {
        Intent intent = new Intent(this, OtherActivity.class);
        intent.putExtra(ARG_TO_ACTIVITY, type);
        startActivity(intent);
    }

    public void initSkinColor() {
        //设置底部按钮文字select
        SkinSelectorUtils.setSelectorTextColor(textViewHome, "918DAC", "F94695");
        SkinSelectorUtils.setSelectorTextColor(textViewCourse, "918DAC", "F94695");
//        SkinSelectorUtils.setCorner(linearLayoutTab, 35, 35, 0, 0,"000000");
    }
}
