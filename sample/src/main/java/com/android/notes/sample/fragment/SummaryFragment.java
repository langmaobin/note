package com.android.notes.sample.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.AppCompatTextView;
import com.android.notes.sample.R;
import com.android.notes.sample.app.AppBaseFragment;
import com.android.notes.sample.manager.BaseManager;
import com.android.notes.sample.utils.SkinSelectorUtils;
import com.lib.utils.ValidUtil;
import java.util.List;

import butterknife.BindView;

public class SummaryFragment extends AppBaseFragment {

    @BindView(R.id.relative_layout_bg)
    RelativeLayout relativeLayoutBg;

    @BindView(R.id.linear_layout_content)
    LinearLayout linearLayoutContent;

    @BindView(R.id.relative_button)
    RelativeLayout relativeButton;
    @BindView(R.id.relative_work)
    RelativeLayout relativeWork;
    @BindView(R.id.text_work)
    AppCompatTextView textWork;

    @BindView(R.id.relative_button_life)
    RelativeLayout relativeButtonLife;
    @BindView(R.id.relative_life)
    RelativeLayout relativeLife;
    @BindView(R.id.text_life)
    AppCompatTextView textLife;

    @BindView(R.id.relative_button_well)
    RelativeLayout relativeButtonWell;
    @BindView(R.id.relative_well)
    RelativeLayout relativeWell;
    @BindView(R.id.text_well)
    AppCompatTextView textWell;

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetupView() {
        super.onSetupView();
        setLayoutView(R.layout.fragment_summary);
    }

    @Override
    public void onBindData() {
        super.onBindData();
        baseManager = new BaseManager(getContext());
        initSkinColor();
    }

    @Override
    public void onResume() {
        super.onResume();
        List workList = baseManager.getWorkList();
        if (!ValidUtil.isEmpty(workList)) {
            int size = workList.size();
            textWork.setText(getContext().getString(R.string.__t_record, String.valueOf(size)));
        } else {
            textWork.setText(getContext().getString(R.string.__t_record, "0"));
        }

        List lifeList = baseManager.getLifeList();
        if (!ValidUtil.isEmpty(lifeList)) {
            int size = lifeList.size();
            textLife.setText(getContext().getString(R.string.__t_record, String.valueOf(size)));
        } else {
            textLife.setText(getContext().getString(R.string.__t_record, "0"));
        }

        List wellList = baseManager.getWellList();
        if (!ValidUtil.isEmpty(wellList)) {
            int size = wellList.size();
            textWell.setText(getContext().getString(R.string.__t_record, String.valueOf(size)));
        } else {
            textWell.setText(getContext().getString(R.string.__t_record, "0"));
        }
    }

    @Override
    public void onClickButtonRetry(Object tag) {
        super.onClickButtonRetry(tag);

    }

    public void initSkinColor() {
        SkinSelectorUtils.setCorner(linearLayoutContent, 35, 35, 0, 0, "290945");
        SkinSelectorUtils.setGradientTl_Br(relativeLayoutBg, "1d244f", "3f1a47");
        SkinSelectorUtils.setCorner(relativeButton, 100, 100, 100, 100, "f54085");
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativeWork, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setCorner(relativeButtonLife, 100, 100, 100, 100, "f54085");
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativeLife, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setCorner(relativeButtonWell, 100, 100, 100, 100, "f54085");
        SkinSelectorUtils.setGradientAndCornerAndStroker(relativeWell, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
    }
}