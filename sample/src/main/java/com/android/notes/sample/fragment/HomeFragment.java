package com.android.notes.sample.fragment;

import static com.ywvision.wyvisionhelper.app.AppConstants.Args.ARG_TO_ACTIVITY;
import static com.ywvision.wyvisionhelper.app.AppConstants.OTHER_ACTIVITY.OTHER_SETTING;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.android.notes.sample.OtherActivity;
import com.android.notes.sample.utils.SkinSelectorUtils;
import com.lib.utils.ValidUtil;
import com.lib.widget.VerticalLoadMoreRecyclerViewAdapter;
import com.android.notes.sample.R;
import com.android.notes.sample.adapter.MyAutoAdapter;
import com.android.notes.sample.app.AppBaseFragment;
import com.android.notes.sample.manager.BaseManager;
import com.android.notes.sample.model.AutoModel;

import butterknife.BindView;
import butterknife.OnClick;

import java.util.Collections;
import java.util.List;

public class HomeFragment extends AppBaseFragment implements
        MyAutoAdapter.OnClickListener {

    private MyAutoAdapter workAdapter;
    private MyAutoAdapter lifeAdapter;
    private MyAutoAdapter healthAdapter;
    @BindView(R.id.relative_layout_bg)
    RelativeLayout relativeLayoutBg;
    @BindView(R.id.relative_layout_top)
    RelativeLayout relativeLayoutTop;

    @BindView(R.id.recycler_view_work)
    RecyclerView recyclerViewWork;
    @BindView(R.id.recycler_view_life)
    RecyclerView recyclerViewLife;
    @BindView(R.id.recycler_view_health)
    RecyclerView recyclerViewHealth;

    @BindView(R.id.linear_layout_work)
    LinearLayout linearLayoutWork;
    @BindView(R.id.linear_layout_well)
    LinearLayout linearLayoutWell;
    @BindView(R.id.linear_layout_life)
    LinearLayout linearLayoutLife;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetupView() {
        super.onSetupView();
        setLayoutView(R.layout.fragment_home);
    }

    @Override
    public void onBindData() {
        super.onBindData();
        baseManager = new BaseManager(getContext());
        init();
        initSkinColor();
    }

    @OnClick({
            R.id.relative_layout_setting,
            R.id.image_view_setting
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_setting:
            case R.id.image_view_setting:
                switchActivity(OTHER_SETTING);
                break;
        }
    }

    @Override
    public void onClickButtonRetry(Object tag) {
        super.onClickButtonRetry(tag);

    }


    @Override
    public void onItemMasterClick(AutoModel masterModel, int position) {

    }

    @Override
    public void onItemFMasterLoadMore() {

    }

    @Override
    public void onItemFMasterLoadRetry() {

    }

    public void switchActivity(String type) {
        Intent intent = new Intent(getContext(), OtherActivity.class);
        intent.putExtra(ARG_TO_ACTIVITY, type);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        List workList = baseManager.getWorkList();
        if (!ValidUtil.isEmpty(workList)) {
            linearLayoutWork.setVisibility(View.VISIBLE);
            int size = workList.size();
            if (size >= 3) {
                List lastThree = workList.subList(size - 3, size);
                Collections.reverse(lastThree);
                workAdapter.setList(lastThree);
            } else {
                Collections.reverse(workList);
                workAdapter.setList(workList);
            }
        } else {
            linearLayoutWork.setVisibility(View.GONE);
        }

        List lifeList = baseManager.getLifeList();
        if (!ValidUtil.isEmpty(lifeList)) {
            linearLayoutLife.setVisibility(View.VISIBLE);
            int size = lifeList.size();
            if (size >= 3) {
                List lastThree = lifeList.subList(size - 3, size);
                Collections.reverse(lastThree);
                lifeAdapter.setList(lastThree);
            } else {
                Collections.reverse(lifeList);
                lifeAdapter.setList(lifeList);
            }
        } else {
            linearLayoutLife.setVisibility(View.GONE);
        }

        List wellList = baseManager.getWellList();
        if (!ValidUtil.isEmpty(wellList)) {
            linearLayoutWell.setVisibility(View.VISIBLE);
            int size = wellList.size();
            if (size >= 3) {
                List lastThree = wellList.subList(size - 3, size);
                Collections.reverse(lastThree);
                healthAdapter.setList(lastThree);
            } else {
                Collections.reverse(wellList);
                healthAdapter.setList(wellList);
            }
        } else {
            linearLayoutWell.setVisibility(View.GONE);
        }
    }

    private void init() {
        workAdapter = new MyAutoAdapter(getContext(), this);
        initRecyclerView(recyclerViewWork, workAdapter);

        lifeAdapter = new MyAutoAdapter(getContext(), this);
        initRecyclerView(recyclerViewLife, lifeAdapter);

        healthAdapter = new MyAutoAdapter(getContext(), this);
        initRecyclerView(recyclerViewHealth, healthAdapter);
    }

    private void initRecyclerView(RecyclerView recyclerView, VerticalLoadMoreRecyclerViewAdapter verticalLoadMoreRecyclerViewAdapter) {
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(verticalLoadMoreRecyclerViewAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.post(new Runnable() {
                    public void run() {
                        verticalLoadMoreRecyclerViewAdapter.scrollItem(recyclerView.getLayoutManager());
                    }
                });
            }
        });
    }

    public void initSkinColor() {
        SkinSelectorUtils.setCorner(relativeLayoutTop, 0, 0, 35, 35, "290945");
        SkinSelectorUtils.setGradientTl_Br(relativeLayoutBg, "1d244f", "3f1a47");
    }
}