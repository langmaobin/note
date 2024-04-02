package com.android.notes.sample;

import android.view.MotionEvent;

import com.android.notes.sample.fragment.SettingFragment;
import com.lib.utils.ValidUtil;
import com.android.notes.sample.app.AppBaseActivity;
import com.android.notes.sample.fragment.NewNoteFragment;
import com.android.notes.sample.manager.BaseManager;

import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_TO_ACTIVITY;
import static com.ywvision.wyvisionhelper.app.AppConstants.OTHER_ACTIVITY.OTHER_EMPTY;
import static com.ywvision.wyvisionhelper.app.AppConstants.OTHER_ACTIVITY.OTHER_SETTING;


public class OtherActivity extends AppBaseActivity {

    private String type;

    @Override
    public void onSetupView() {
        super.onSetupView();
        setLayoutView(R.layout.activity_other);
        setFrameLayoutView(R.id.frame_layout_main);
        setEnabledSlideOverFragmentTransition(true);
//        fullScreen(this);
//        StatusBarUtils.StatusBarLightMode(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            hideKeyboard();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onSetupToolbar() {
        super.onSetupToolbar();
    }


    @Override
    public void onBindData() {
        super.onBindData();

        baseManager = new BaseManager(this);
        type = getIntent().getStringExtra(ARG_TO_ACTIVITY);
        if (!ValidUtil.isEmpty(type)) {
            switch (type) {
                case OTHER_SETTING:
                    switchFragment(SettingFragment.newInstance());
                    break;
                case OTHER_EMPTY:
                    switchFragment(NewNoteFragment.newInstance());
                    break;
            }
        }
    }
}