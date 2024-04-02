package com.android.notes.sample.utils;

import static com.android.notes.sample.utils.ResourceUtils.getResources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.lang.reflect.Field;


public class SkinSelectorUtils {

    /**
     * 设置底部tab图标
     * -android.R.attr.state_checked 和 android.R.attr.state_checked 的区别在于 “-” 号代表值里的true 和 false ,有“-”为false 没有则为true
     *
     * @paramradioButton控件
     * @paramdrawableNormal常态时的图片
     * @paramdrawableSelect选中时的图片
     */
    public static void setViewSelectorDrawable(View view, String normalPath, String selectPath) {

        Bitmap bitmapNormal = BitmapFactory.decodeFile(normalPath);
        BitmapDrawable drawableNormal = new BitmapDrawable(bitmapNormal);

        Bitmap bitmapSelect = BitmapFactory.decodeFile(selectPath);
        BitmapDrawable drawableSelect = new BitmapDrawable(bitmapSelect);

        StateListDrawable drawable = new StateListDrawable();
        //选中
        drawable.addState(new int[]{android.R.attr.state_checked}, drawableSelect);
        //未选中
        drawable.addState(new int[]{-android.R.attr.state_checked}, drawableNormal);

        view.setBackgroundDrawable(drawable);

    }

    //SelectorUtils.setButtonSelectorDrawablePadding(radioHome, UNZIP_PATH+"/ic_home_off.png",UNZIP_PATH+"/ic_home_on.png");
    public static void setButtonSelectorDrawablePadding(RadioButton radioButton, String normalPath, String selectPath) {

        Bitmap bitmapNormal = BitmapFactory.decodeFile(normalPath);
        BitmapDrawable drawableNormal = new BitmapDrawable(bitmapNormal);

        Bitmap bitmapSelect = BitmapFactory.decodeFile(selectPath);
        BitmapDrawable drawableSelect = new BitmapDrawable(bitmapSelect);

        StateListDrawable drawable = new StateListDrawable();
        //未选中
        drawable.addState(new int[]{-android.R.attr.state_checked}, drawableNormal);
        //选中
        drawable.addState(new int[]{android.R.attr.state_checked}, drawableSelect);
        drawable.setBounds(0, 10, 130, 130);
        radioButton.setCompoundDrawables(null, drawable, null, null);

    }

    /**
     * 设置底部tab文字颜色
     *
     * @paramradioButton控件
     * @paramnormal正常时的颜色值
     * @paramchecked选中时的颜色值
     */

    public static void setSelectorTextColor(TextView textView, String normalColor, String checkedColor) {

        int normal = Color.parseColor("#" + normalColor);
        int checked = Color.parseColor("#" + checkedColor);
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}  // checked
        };
        int[] colors = new int[]{
                normal,
                checked
        };//把两种颜色一次性添加
        ColorStateList colorStateList = new ColorStateList(states, colors);
        textView.setTextColor(colorStateList);
    }

    /**
     * 设置TextView文字颜色
     *
     * @paramtextView控件
     * @paramcolorVale颜色值
     */
    public static void setTextViewColor(TextView textView, String colorVale) {

        textView.setTextColor(Color.parseColor("#" + colorVale));

    }

    /**
     * 设置Button文字颜色
     *
     * @paramtextView控件
     * @paramcolorVale颜色值
     */
    public static void setButtonTextColor(Button button, String colorVale) {

        button.setTextColor(Color.parseColor("#" + colorVale));

    }

    /**
     * 设置TextView文字颜色
     *
     * @paramtextView控件
     * @paramcolorVale颜色值
     */
    public static void setTextViewGradientColor(TextView textView, String colorStart, String colorEnd) {

        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, textView.getPaint().getTextSize(), Color.parseColor("#" + colorStart), Color.parseColor("#" + colorEnd), Shader.TileMode.CLAMP);
        textView.getPaint().setShader(mLinearGradient);
        textView.invalidate();
    }

    public static void setTextViewHintColor(TextView textView, String colorVale) {

        textView.setHintTextColor(Color.parseColor("#" + colorVale));

    }

    public static void setTextViewCursorColor(TextView textView, String colorVale, int drawable) {

        GradientDrawable myGrad1 = (GradientDrawable) getResources().getDrawable(drawable);
        myGrad1.setColor(Color.parseColor("#" + colorVale));
        myGrad1.setSize(4, 20);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(textView, myGrad1);
        } catch (Exception ignored) {
            String error = ignored.toString();
        }

    }

    /**
     * 设置view背景颜色
     *
     * @paramtextView控件
     * @paramcolorVale颜色值
     */
    public static void setBackgroundColor(View view, String colorVale) {

        view.setBackgroundColor(Color.parseColor("#" + colorVale));

    }

    /**
     * 设置view透明度
     *
     * @paramcolorVale颜色值
     */
    public static void setAlpha(View view, String alphaVale) {
        Double dvale = Double.valueOf(alphaVale) * 255;
        int vale = dvale.intValue();
        view.getBackground().mutate().setAlpha(vale);
    }

    /**
     * 设置描边
     *
     * @paramcolorVale颜色值
     */
    public static void setStroke(View view, String colorVale) {
        int strokeWidth = 3; // 3dp 边框宽度
        int roundRadius = 50; // 8dp 圆角半径
        GradientDrawable gd = new GradientDrawable();//创建drawable
//            gd.setColor(context.getResources().getColor(R.color.c5));//内部填充颜色
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, Color.parseColor("#" + colorVale));//边框颜色
        view.setBackgroundDrawable(gd);
    }

    /**
     * 设置描边
     *
     * @paramcolorVale颜色值
     */
    public static void setStrokeAndCorner(View view, String colorVale, int roundRadius) {
        int strokeWidth = 3; // 3dp 边框宽度
        GradientDrawable gd = new GradientDrawable();//创建drawable
//            gd.setColor(context.getResources().getColor(R.color.c5));//内部填充颜色
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, Color.parseColor("#" + colorVale));//边框颜色
        view.setBackgroundDrawable(gd);
    }

    public static void setStrokeAndCornerAndBg(View view, String colorVale, int roundRadius, String color) {
        int strokeWidth = 3; // 3dp 边框宽度
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.parseColor("#" + color));//内部填充颜色
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, Color.parseColor("#" + colorVale));//边框颜色
        view.setBackgroundDrawable(gd);
    }

    /**
     * 设置四角统一圆角
     *
     * @paramcolorVale颜色值
     */
    public static void setRadiusAndColor(View view, String colorVale, int radius) {

        int roundRadius = radius;
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.parseColor("#" + colorVale));//内部填充颜色
        gd.setCornerRadius(roundRadius);

        view.setBackgroundDrawable(gd);
    }

    /**
     * 设置圆角
     *
     * @paramcolorVale颜色值
     */
    public static void setCorner(View view, float lt, float rt, float rb, float lb, String color) {
        float corners[] = {lt, lt, rt, rt, rb, rb, lb, lb};
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.parseColor("#" + color));//内部填充颜色
        gd.setCornerRadii(corners);
        view.setBackgroundDrawable(gd);
    }

    /**
     * 设置四角不同角度
     */
    public static void setCorner(View view, float lt, float rt, float rb, float lb) {

        float corners[] = {lt, lt, rt, rt, rb, rb, lb, lb};
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setCornerRadii(corners);
        view.setBackgroundDrawable(gd);
    }

    /**
     * 设置单一渐变
     *
     * @paramcolorVale颜色值
     */
    public static void setGradient(View view, String colorStart, String colorEnd) {

        int colors[] = {Color.parseColor("#" + colorStart), Color.parseColor("#" + colorEnd)};//分别为开始颜色，结束颜色
        GradientDrawable gduser = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
        view.setBackgroundDrawable(gduser);
    }
    public static void setGradientTl_Br(View view, String colorStart, String colorEnd) {

        int colors[] = {Color.parseColor("#" + colorStart), Color.parseColor("#" + colorEnd)};//分别为开始颜色，结束颜色
        GradientDrawable gduser = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        view.setBackgroundDrawable(gduser);
    }
    /**
     * 设置渐变从上到下
     *
     * @paramcolorVale颜色值
     */
    public static void setGradientTopBottom(View view, String colorStart, String colorEnd) {

        int colors[] = {Color.parseColor("#" + colorStart), Color.parseColor("#" + colorEnd)};//分别为开始颜色，结束颜色
        GradientDrawable gduser = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        view.setBackgroundDrawable(gduser);
    }

    /**
     * 设置渐变带有圆角
     *
     * @paramcolorVale颜色值
     */
    public static void setGradientAndCorner(View view, float lt, float rt, float rb, float lb, String colorStart, String colorEnd) {
//        float corners[] = {10f, 10f, 40f, 40f, 10f, 10f, 40f, 40f};
        float corners[] = {lt, lt, rt, rt, rb, rb, lb, lb};
        int colors[] = {Color.parseColor("#" + colorStart), Color.parseColor("#" + colorEnd)};//分别为开始颜色，结束颜色
        GradientDrawable gduser = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
        gduser.setCornerRadii(corners);
        view.setBackgroundDrawable(gduser);
    }

    public static void setGradientAndCornerAndStroker(View view, int roundRadius, String colorStart, String colorEnd, String colorVale, GradientDrawable.Orientation ori) {

        int strokeWidth = 3; // 3dp 边框宽度
        int colors[] = {Color.parseColor("#" + colorStart), Color.parseColor("#" + colorEnd)};//分别为开始颜色，结束颜色
        GradientDrawable gduser = new GradientDrawable(ori, colors);
        gduser.setCornerRadius(roundRadius);
        gduser.setStroke(strokeWidth, Color.parseColor("#" + colorVale));//边框颜色
        view.setBackgroundDrawable(gduser);
    }

    public static void setImageViewBitmap(ImageView imageView, String normalPath) {

        Bitmap bitmapNormal = BitmapFactory.decodeFile(normalPath);
        imageView.setImageBitmap(bitmapNormal);
    }

    /**
     * 设置背景图片
     *
     * @paramcolorVale颜色值
     */

    public static void setViewBackgroundDrawable(View view, String normalPath) {
        try {
            Bitmap bitmapNormal = BitmapFactory.decodeFile(normalPath);
            BitmapDrawable drawableNormal = new BitmapDrawable(bitmapNormal);
            view.setBackground(drawableNormal);
        } catch (Exception e) {

        }
    }

    public static Drawable getViewBackgroundDrawable(String normalPath) {

        Bitmap bitmapNormal = BitmapFactory.decodeFile(normalPath);
        BitmapDrawable drawableNormal = new BitmapDrawable(bitmapNormal);
        return drawableNormal;
    }

    public static void setProgressDrawable(Context context, ProgressBar bar, String colorVale, String secondVale, String aplha) {
        Double dvale = Double.valueOf(aplha) * 225;
        int vale = dvale.intValue();

        int radius0 = DensityUtil.dp2px(context, 10);
        float[] outerR = new float[]{radius0, radius0, radius0, radius0, radius0, radius0, radius0, radius0};
        RoundRectShape roundRectShape1 = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable1 = new ShapeDrawable();
        shapeDrawable1.setShape(roundRectShape1);
        shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable1.getPaint().setColor(Color.parseColor("#" + colorVale));
        ClipDrawable clipDrawable = new ClipDrawable(shapeDrawable1, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        RoundRectShape roundRectShape0 = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(roundRectShape0);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(Color.parseColor("#" + secondVale));
        shapeDrawable.setAlpha(vale);

        Drawable[] layers = new Drawable[]{shapeDrawable, clipDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);
        bar.setProgressDrawable(layerDrawable);
    }

    /**
     * 设置svg图片颜色
     *
     * @param view
     */
    public static void setSvgDrawableColor(ImageView view, int id, String colorvale) {
        try {
            Drawable drawable = getResources().getDrawable(id);
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.parseColor("#" + colorvale),
                    PorterDuff.Mode.SRC_ATOP);
            drawable.setColorFilter(porterDuffColorFilter);
            view.setImageDrawable(drawable);
        } catch (Exception e) {

        }

    }
}
