//package com.lib.widget;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.support.annotation.NonNull;
//import android.support.design.widget.TabLayout;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//public class CustomTabLayout extends TabLayout {
//
//    private Context context;
//    private Typeface typefaceLight;
//    private Typeface typefaceRegular;
//    private Typeface typefaceSemiBold;
//
//    public CustomTabLayout(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public CustomTabLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        this.context = context;
//        this.typefaceLight = Typeface.createFromAsset(context.getAssets(), "fonts/MicrosoftYaHei-Light.ttf");
//        this.typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/MicrosoftYaHei-Regular.ttf");
//        this.typefaceSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/MicrosoftYaHei-SemiBold.ttf");
//    }
//
//    @Override
//    public void addTab(@NonNull Tab tab, boolean setSelected) {
//        super.addTab(tab, setSelected);
//        ViewGroup mainView = (ViewGroup) getChildAt(0);
//        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
//        int tabChildCount = tabView.getChildCount();
//        for (int i = 0; i < tabChildCount; i++) {
//            View tabViewChild = tabView.getChildAt(i);
//            if (tabViewChild instanceof TextView) {
//                TextView tv = (TextView) tabViewChild;
//                tv.setTypeface(typefaceSemiBold);
//            }
//        }
//    }
//
//    public void setTabViewTypeface(boolean isSelected, Tab tab) {
//        ViewGroup mainView = (ViewGroup) getChildAt(0);
//        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
//        int tabChildCount = tabView.getChildCount();
//        for (int i = 0; i < tabChildCount; i++) {
//            View tabViewChild = tabView.getChildAt(i);
//            if (tabViewChild instanceof TextView) {
//                TextView tv = (TextView) tabViewChild;
//                tv.setTypeface(isSelected ? typefaceRegular : typefaceLight);
//            }
//        }
//    }
//
//}
