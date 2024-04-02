package com.lib.widget;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lib.R;
import com.lib.widget.multiimageselector.MultiImageSelectorActivity;

public class GalleryImageSelectorActivity extends MultiImageSelectorActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.__t_global_text_choose_images));
        }
    }

}