package com.android.notes.sample.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.android.notes.sample.R;
import com.android.notes.sample.app.AppBaseFragment;
import com.android.notes.sample.manager.BaseManager;
import com.android.notes.sample.utils.SkinSelectorUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends AppBaseFragment {


    @BindView(R.id.relative_layout_bg)
    RelativeLayout relativeLayoutBg;
    @BindView(R.id.relative_layout_top)
    RelativeLayout relativeLayoutTop;
    @BindView(R.id.relative_online)
    RelativeLayout relativeOnline;
    @BindView(R.id.relative_agreement)
    RelativeLayout relativeAgreement;
    @BindView(R.id.relative_privacy)
    RelativeLayout relativePrivacy;
    @BindView(R.id.relative_aboutus)
    RelativeLayout relativeAboutus;
    @BindView(R.id.relative_bottom)
    RelativeLayout relativeBottom;
    @BindView(R.id.relative_button)
    RelativeLayout relativeButton;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetupView() {
        super.onSetupView();
        setLayoutView(R.layout.fragment_setting);
    }

    @Override
    public void onBindData() {
        super.onBindData();
        baseManager = new BaseManager(getContext());
        initSkinColor();
    }

    @OnClick({
            R.id.relative_layout_close,
            R.id.image_view_back,
            R.id.relative_button
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_close:
            case R.id.image_view_back:
                onBackPressed();
                break;
            case R.id.relative_button:
                baseManager.getSharedPref().clear();
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClickButtonRetry(Object tag) {
        super.onClickButtonRetry(tag);

    }

    public void initSkinColor() {
        SkinSelectorUtils.setCorner(relativeLayoutTop, 0, 0, 35, 35, "290945");
        SkinSelectorUtils.setGradientTl_Br(relativeLayoutBg, "1d244f", "3f1a47");
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativeOnline, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativeAgreement, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativePrivacy, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativeAboutus, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setCorner(relativeBottom, 35, 35, 0, 0, "000000");
        SkinSelectorUtils.setCorner(relativeButton, 100, 100, 100, 100, "f54085");

    }
}