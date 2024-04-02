package com.lib.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lib.R;
import com.lib.widget.CustomSnackbar;
import com.lib.widget.TagGroup;
import com.lib.widget.swipegallery.SwipeGalleryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewUtil {

    public static void goToSwipeGalleryActivity(Activity activity, Fragment fragment, View view, String imgUrl) {
        List<String> imgSrcList = new ArrayList<>();
        imgSrcList.add(imgUrl);
        goToSwipeGalleryActivity(activity, fragment, view, imgSrcList, true, 0);
    }

    public static void goToSwipeGalleryActivity(Activity activity, Fragment fragment,
                                                String imgUrl, boolean isUrlPath) {
        List<String> imgSrcList = new ArrayList<>();
        imgSrcList.add(imgUrl);
        goToSwipeGalleryActivity(activity, fragment, null, imgSrcList, isUrlPath, 0);
    }

    public static void goToSwipeGalleryActivity(Activity activity, Fragment fragment,
                                                List<String> imgSrcList, boolean isUrlPath) {
        goToSwipeGalleryActivity(activity, fragment, null, imgSrcList, isUrlPath, 0);
    }

    /**
     * @param activity   activity to create scene transition
     * @param fragment   fragment to start new activity
     * @param imgSrcList list of image sources either in file path of web url format
     * @param isUrlPath  indicate whether the image should get from device or url
     * @param position   the position of the view clicked
     * @param pairs      shared elements list to trigger the transition
     */
    public static void goToSwipeGalleryActivity(Activity activity, Fragment fragment,
                                                List<String> imgSrcList, boolean isUrlPath,
                                                int position, Pair[] pairs) {
        Intent intent = new Intent(activity.getApplicationContext(), SwipeGalleryActivity.class);
        intent.putExtra(SwipeGalleryActivity.ARG_IMG_SRC_LIST, (ArrayList<String>) imgSrcList);
        intent.putExtra(SwipeGalleryActivity.ARG_IS_URL_PATH, isUrlPath);
        intent.putExtra(SwipeGalleryActivity.ARG_CURRENT_POSITION, position);
//        if (pairs != null) {
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
//            if (fragment != null) {
//                fragment.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY, options.toBundle());
//            } else {
//                activity.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY, options.toBundle());
//            }
//        } else
        if (fragment != null) {
            fragment.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY);
        } else {
            activity.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY);
        }
    }

    /**
     * @param activity   activity to create scene transition
     * @param fragment   fragment to start new activity
     * @param view       the view to trigger the transition
     * @param imgSrcList list of image sources either in file path of web url format
     * @param isUrlPath  indicate whether the image should get from device or url
     * @param position   the position of the view clicked
     */
    public static void goToSwipeGalleryActivity(Activity activity, Fragment fragment,
                                                View view, List<String> imgSrcList,
                                                boolean isUrlPath, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), SwipeGalleryActivity.class);
        intent.putExtra(SwipeGalleryActivity.ARG_IMG_SRC_LIST, (ArrayList<String>) imgSrcList);
        intent.putExtra(SwipeGalleryActivity.ARG_IS_URL_PATH, isUrlPath);
        intent.putExtra(SwipeGalleryActivity.ARG_CURRENT_POSITION, position);
        if (view != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    view, // The view which starts the transition
                    SwipeGalleryActivity.ARG_GALLERY_TRANSITION + position // The transitionName of the view we’re transitioning to
            );
            if (fragment != null) {
                fragment.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY, options.toBundle());
            } else {
                activity.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY, options.toBundle());
            }
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY);
        } else {
            activity.startActivityForResult(intent, SwipeGalleryActivity.REQ_CODE_SWIPE_GALLERY);
        }
    }

    public static void updateTaggedView(Context context, TagGroup tagGroup,
                                        Map<String, String> stringMap, boolean isInTaggingMode) {
        String[] taggedList;
        if (!ValidUtil.isEmpty(stringMap)) {
            int index = 0;
            int maxSize = 4;
            int size = stringMap.size();
            int oriTaggedSize = size >= maxSize ? maxSize : size;
            int taggedSize = isInTaggingMode ? oriTaggedSize : oriTaggedSize + 1;
            taggedList = new String[taggedSize];
            for (Map.Entry entry : stringMap.entrySet()) {
                int limit = isInTaggingMode ? taggedList.length : taggedList.length - 1;
                if (index < limit) {
                    taggedList[index] = String.valueOf(entry.getValue());
                    index++;
                    if (index >= 4) {
                        int remainder = size - 3;
                        taggedList[index - 1] = "+ " + remainder + " " + context.getString(R.string.__T_global_text_more);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (tagGroup.getVisibility() == View.GONE) {
                tagGroup.setVisibility(View.VISIBLE);
            }
        } else {
            taggedList = new String[]{};
            if (tagGroup.getVisibility() == View.VISIBLE) {
                tagGroup.setVisibility(View.GONE);
            }
        }
        if (!isInTaggingMode && taggedList.length > 0) {
            taggedList[taggedList.length - 1] = context.getString(R.string.__T_global_text_add_more);
        }
        tagGroup.setTags(taggedList);
    }

    public static String getFontPath(View view, String filePath) {
        String fontPostFix = "";
        Typeface typeface = null;
        if (view instanceof TextView) {
            typeface = ((TextView) view).getTypeface();
        } else if (view instanceof TextInputLayout) {
            typeface = ((TextInputLayout) view).getTypeface();
        }
        if (filePath == null) {
            filePath = ".";
        }
        if (typeface != null) {
            if (typeface.isBold()) {
                fontPostFix = "Bold";
            }
            if (typeface.isItalic()) {
                fontPostFix += "Italic";
            }
        }
        if (TextUtils.isEmpty(fontPostFix)) {
            fontPostFix = "Regular";
        }
        int ix = filePath.lastIndexOf(".");
        return filePath.substring(0, ix) + "-" + fontPostFix + filePath.substring(ix);
    }

    public static void displayToast(Context context, String message) {
        try {
            final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
//            View layout = LayoutInflater.from(context).inflate(R.layout.view_custom_toast, null);
//            TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
//            text.setText(message);
//            final Toast toast = new Toast(context);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            toast.setView(layout);
//            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CustomSnackbar buildSnackbar(View layoutContainerView, String messageText) {
        return snackbarBuilder(layoutContainerView, messageText).build();
    }

    public static CustomSnackbar buildSnackbarWithAction(View layoutContainerView,
                                                         String messageText, String actionText,
                                                         View.OnClickListener onActionClickListener) {
        return snackbarBuilder(layoutContainerView, messageText)
                .duration(Snackbar.LENGTH_INDEFINITE)
                .actionText(actionText)
                .onActionClickListener(onActionClickListener)
                .build();
    }

    public static CustomSnackbar buildSnackbarWithActionColor(View layoutContainerView,
                                                              String messageText, String actionText,
                                                              int actionTextColor,
                                                              View.OnClickListener onActionClickListener) {
        return snackbarBuilder(layoutContainerView, messageText)
                .duration(Snackbar.LENGTH_INDEFINITE)
                .actionText(actionText)
                .actionTextColor(actionTextColor)
                .onActionClickListener(onActionClickListener)
                .build();
    }

    public static CustomSnackbar buildSnackbarNetworkConnError(View layoutContainerView, String messageText) {
        return snackbarBuilder(layoutContainerView, messageText)
                .duration(Snackbar.LENGTH_INDEFINITE)
                .build();
    }

    private static CustomSnackbar.Builder snackbarBuilder(View layoutContainerView, String messageText) {
        return new CustomSnackbar.Builder().layoutContainerView(layoutContainerView).messageText(messageText);
    }

    public static String getVersionName(String versionName) {
        return String.format(Locale.ENGLISH, "Version %s", versionName);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int getActionBarSize(Context context) {
        if (context != null) {
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (32 * scale + 0.5f);
        }
        return 0;
    }

    public static int getScreenWidth(Activity activity) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getScreenHeight(Activity activity) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void copyToClipboard(Activity activity, String textToCopy) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text To Copy", textToCopy);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void setTextViewWithRedAsterik(TextView textView, String originalText) {
        String textRedForeground = "*";
        String textToDisplay = String.format(Locale.ENGLISH, "%s %s", originalText, textRedForeground);
        textView.setText(textToDisplay, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) textView.getText();
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#F04E4E")), originalText.length() + 1, textToDisplay.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setEditTextWithRedAsterik(EditText editText, String originalText) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(originalText);
        int start = builder.length();
        builder.append(" *");
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#F04E4E")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(builder);
    }

    public static void setEditTextHintWithRedAsterik(EditText editText, String originalText) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(originalText);
        int start = builder.length();
        builder.append(" *");
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#F04E4E")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(builder);
    }

    public static void setTextViewStrikeThrough(TextView textView,
                                                String textStrikeThrough) {
        textView.setText(textStrikeThrough, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) textView.getText();
        spannable.setSpan(new StrikethroughSpan(), 0, textStrikeThrough.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static int getDpFromPx(int sizeInDp, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    public static String getFormattedHtml(String content, String fontName) {
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0,target-densityDpi=device-dpi\" />" +
                "<style type=\"text/css\">" +
                "img {" +
                "display: block;" +
                "max-width: 100%;" +
                "height: auto;}" +
                "ul li{" +
                "list-style-type: disc;" +
                "}" +
                "@font-face {font-family: MyFont; src: url(\"file:///android_asset/fonts/" + fontName + ".ttf\")}" +
                "body {" +
                "font-family: MyFont;" +
                "text-align: justify;" +
                "text-justify: inter-word;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                content +
                "</body>" +
                "</html>";
    }

    public static Drawable getIconDrawable(final Context context, final int layoutId) {
        return new Drawable() {

            @Override
            public void draw(@NonNull Canvas canvas) {
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                View view = LayoutInflater.from(context).inflate(layoutId, null);
                view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                canvas.drawBitmap(bitmap, (width / 2) - (bitmap.getWidth() / 2), (height / 2) - (bitmap.getHeight() / 2), null);
            }

            @Override
            public void setAlpha(int i) {
            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {
            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSLUCENT;
            }

        };
    }

    public static Drawable getDrawable(Context context, int resId) {
        try {
            return ContextCompat.getDrawable(context, resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
