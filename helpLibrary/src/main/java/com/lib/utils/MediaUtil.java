package com.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.lib.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static com.lib.base.BaseConstants.TMP_IMG_NAME;

public class MediaUtil {

    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().toString() + File.separator;

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    /**
     * Save the image bitmap into local storage
     *
     * @param bitmap   the image bitmap to store in local storage
     * @param dirPath  the directory path to save the image
     * @param filename the image filename
     */
    public static void saveImage(Bitmap bitmap, String dirPath, String filename) {
        try {
            dirPath = dirPath != null ? dirPath : ROOT_DIR;
            File dir = new File(dirPath);
            // Create the directory if it does not exist yet
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Save the image
            File file = new File(dir, filename);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Download image from specific url
     *
     * @param dirPath   the directory path to store the downloaded image (end with '/')
     * @param urlString the url link to download the image
     * @return the image path that stored in local storage
     */
    public static String downloadUrlImage(String dirPath, String urlString) {
        try {
            dirPath = dirPath != null ? dirPath : ROOT_DIR;
            File dir = new File(dirPath);
            // Create the directory if it does not exist yet
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Save the image
            File file = new File(dir, TMP_IMG_NAME);
            URL url = new URL(urlString);
            InputStream input = url.openStream();
            byte[] buffer = new byte[2048];
            OutputStream output = new FileOutputStream(file);
            int bytesRead;
            while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            output.close();
            return dirPath + TMP_IMG_NAME;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get resized image bitmap based on original image file path
     *
     * @param filepath the image file path
     * @return the resized image bitmap
     */
    public static Bitmap getImageFromFilePath(String filepath) {
        try {
            File file = new File(filepath);
            if (file.exists()) {
                return resizeImage(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reduce image file size
     *
     * @param filepath the image file path
     * @return the image file after compressed
     */
    public static File resizeImageFileSize(String filepath) {

        File file = null;
        try {
            file = new File(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file == null) {
            return null;
        }

        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;

            // Factor of downsizing the image
            FileInputStream fis = new FileInputStream(file);

            // Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 400;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();

            // Override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            Bitmap.CompressFormat compressFormat;
            if (filepath.endsWith(".png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            } else {
                compressFormat = Bitmap.CompressFormat.JPEG;
            }

            selectedBitmap.compress(compressFormat, 100, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;

    }

    /**
     * To resize large image bitmap
     *
     * @param bitmap the original bitmap to be resize
     * @return the resized image bitmap
     */
    public static Bitmap resizeImage(Bitmap bitmap) {
        float multFactor;
        int scaleSize = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        if (originalHeight > originalWidth) {
            newHeight = scaleSize;
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else if (originalWidth > originalHeight) {
            newWidth = scaleSize;
            multFactor = (float) originalHeight / (float) originalWidth;
            newHeight = (int) (newWidth * multFactor);
        } else if (originalHeight == originalWidth) {
            newHeight = scaleSize;
            newWidth = scaleSize;
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

    /**
     * To search a specific file from external directory
     *
     * @param dirPath  the directory path to start search the file (set to null to search from root)
     * @param filename the filename to be search
     * @return file object that has been searched
     */
    public static File getFileFromExtDir(String dirPath, String filename) {
        try {
            dirPath = dirPath != null ? dirPath : ROOT_DIR;
            File dir = new File(dirPath);
            for (File file : dir.listFiles()) {
                if (file != null && file.getName().equals(filename)) {
                    return file;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPicPathAfterSaveCapturedImg(File f, String filePath, Bitmap bitmapOriginal) {
        String picturePath = "";
        try {
            // Remove previous temporarily file
            if (f.exists()) {
                f.delete();
            }
            // Create new file from it
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, System.currentTimeMillis() + ".jpg");
            picturePath = file.getAbsolutePath();
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmapOriginal.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picturePath;
    }

    public static Bitmap getBitmapCenterCrop(Bitmap src) {
        Bitmap dst;
        if (src.getWidth() >= src.getHeight()) {
            dst = Bitmap.createBitmap(
                    src,
                    src.getWidth() / 2 - src.getHeight() / 2,
                    0,
                    src.getHeight(),
                    src.getHeight()
            );
        } else {
            dst = Bitmap.createBitmap(
                    src,
                    0,
                    src.getHeight() / 2 - src.getWidth() / 2,
                    src.getWidth(),
                    src.getWidth()
            );
        }
        return dst;
    }

    public static String convertBitmapToBase64String(Bitmap bitmap) {
        return convertBitmapToBase64String(bitmap, 100);
    }

    public static String convertBitmapToBase64String(Bitmap bitmap, int quality) {
        String encoded = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
            byte[] bitmapByte = baos.toByteArray();
            encoded = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    public static Bitmap convertBase64StringToBitmap(String base64String) {
        Bitmap bitmap = null;
        try {
            byte[] decodedByte = Base64.decode(base64String, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Picasso picasso;

    private static Picasso getPicasso(Context context) {
        if (picasso == null) {
            picasso = new Picasso.Builder(context)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                            e.printStackTrace();
                        }
                    }).build();
        }
        return picasso;
    }

    public static void loadPicassoImg(Context context, String url, ImageView src) {
        loadPicassoImg(context, url, src, R.drawable.img_placeholder);
    }
    public static void loadPicassoBannerImg(Context context, String url, ImageView src) {
        loadPicassoBannerImg(context, url, src, R.drawable.banner_default);
    }

    public static void loadPicassoImgWithCustomPlaceholder(Context context, String url, ImageView src, int defaultImg) {
        loadPicassoImg2(context, url, src, defaultImg, defaultImg);
    }

    public static void loadPicassoHeadImg(Context context, String url, ImageView src) {
        loadPicassoImg(context, url, src, R.drawable.ic_center_profile, R.drawable.ic_center_profile);
    }

    public static void loadPicassoGenerHeadImg(Context context, String url, ImageView src, boolean isGener) {
        if (isGener){
            loadPicassoImg(context, url, src, R.drawable.image_profile, R.drawable.image_profile);
        }else {
            loadPicassoImg(context, url, src, R.drawable.image_femaleprofile, R.drawable.image_femaleprofile);
        }
    }

    public static void loadPicassoImg(Context context, String url, ImageView src, int defaultImg) {
        loadPicassoImg(context, url, src, defaultImg, R.drawable.img_placeholder);
    }
    public static void loadPicassoBannerImg(Context context, String url, ImageView src, int defaultImg) {
        loadPicassoImg(context, url, src, defaultImg, R.drawable.banner_default);
    }

    public static void loadPicassoImg(Context context, String url, ImageView src, int defaultImg, int placeholder) {
        Picasso picasso = getPicasso(context);
        RequestCreator requestCreator = !ValidUtil.isEmpty(url)
                ? picasso.load(url)
                : picasso.load(defaultImg);
        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(defaultImg)
                .placeholder(placeholder)
//                .centerCrop()
//                .resize(500, 500)
                .into(src);
    }

    public static void loadPicassoImg2(Context context, String url, ImageView src, int defaultImg, int placeholder) {
//        Picasso picasso = getPicasso(context);
//        RequestCreator requestCreator = !ValidUtil.isEmpty(url)
//                ? picasso.load(url)
//                : picasso.load(defaultImg);
//        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
//                .error(defaultImg)
//                .placeholder(placeholder)
////                .centerCrop()
////                .resize(500, 500)
//                .into(src);

        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .into(src);

    }

    public static void loadPicassoImg(Context context, ImageView src, int placeholder) {
        Picasso picasso = getPicasso(context);
        RequestCreator requestCreator = picasso.load(placeholder);
        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(placeholder)
                .placeholder(placeholder)
//                .centerCrop()
//                .resize(500, 500)
                .into(src);
    }

    public static void loadPicassoImg(Context context, String url, ImageView src, int defaultImg, int placeholder, Callback callback) {
        Picasso picasso = getPicasso(context);
        RequestCreator requestCreator = !ValidUtil.isEmpty(url)
                ? picasso.load(url)
                : picasso.load(defaultImg);
        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(defaultImg)
                .placeholder(placeholder)
                .into(src, callback);
    }

    public static void loadPicassoImgWithMemoryCacheSkip(Context context, String url, ImageView src) {
        Picasso picasso = getPicasso(context);
        RequestCreator requestCreator = !ValidUtil.isEmpty(url)
                ? picasso.load(url)
                : picasso.load(R.drawable.img_placeholder);
        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(R.drawable.img_placeholder)
                .placeholder(R.drawable.img_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop()
                .resize(500, 500)
                .into(src);
    }

    public static void loadPicassoImgLocalWithMemoryCacheSkip(Context context, String filePath, ImageView src) {
        getPicasso(context).load(new File(filePath))
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(R.drawable.img_placeholder)
                .placeholder(R.drawable.img_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop()
                .resize(500, 500)
                .into(src);
    }

    public static void loadPicassoImgLocalWithMemoryCacheSkipWithoutResize(Context context, String filePath, ImageView src) {
        getPicasso(context).load(new File(filePath))
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(R.drawable.img_placeholder)
                .placeholder(R.drawable.img_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(src);
    }

    public static void loadPicassoImgLocalWithMemoryNotCacheSkip(Context context, String filePath, ImageView src) {
        getPicasso(context).load(new File(filePath))
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(R.drawable.img_placeholder)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .resize(500, 500)
                .into(src);
    }


    public static void loadPicassoImgWithProgressbar(Context context, String url, ImageView src, Callback callback) {
        Picasso picasso = getPicasso(context);
        int defaultImg = R.drawable.img_placeholder;
        RequestCreator requestCreator = !ValidUtil.isEmpty(url)
                ? picasso.load(url)
                : picasso.load(defaultImg);
        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(defaultImg)
//                .centerCrop()
//                .resize(300, 300)
                .into(src, callback);
    }

    public static void loadPicassoImg(Context context, String url, ImageView src, int defaultImg, int placeholder, View view) {
        Picasso picasso = getPicasso(context);
        RequestCreator requestCreator = !ValidUtil.isEmpty(url)
                ? picasso.load(url)
                : picasso.load(defaultImg);
        requestCreator.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .error(defaultImg)
                .placeholder(placeholder)
//                .resize(view.getLayoutParams().width,view.getLayoutParams().height)
//                .centerCrop()
                .into(src);
    }

    public static void loadPicassoDrawableImg(Context context, ImageView src, int drawableRes, int defaultImg) {
        getPicasso(context).load(drawableRes)
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .placeholder(defaultImg)
//                .centerCrop()
//                .resize(300, 300)
                .into(src);
    }

    public static void loadPicassoImgLocal(Context context, String filePath, ImageView src) {
        loadPicassoImgLocal(context, filePath, src, R.drawable.img_placeholder);
    }

    public static void loadPicassoImgLocal(Context context, String filePath, ImageView src, int defaultImg) {
        getPicasso(context).load(new File(filePath))
                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(defaultImg)
                .placeholder(defaultImg)
//                .centerCrop()
//                .resize(300, 300)
                .into(src);
    }

    public static void loadPicassoImgWithGifPlaceholder(Context context, String url,
                                                        final ImageView src,
                                                        final ImageView gifPlaceholder) {
        gifPlaceholder.setVisibility(View.VISIBLE);
        src.setVisibility(View.GONE);
        loadPicassoImgWithGifPlaceholder(context, url, src, new Callback() {

            @Override
            public void onSuccess() {
                if (src.getVisibility() != View.VISIBLE) {
                    src.setVisibility(View.VISIBLE);
                    gifPlaceholder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                if (gifPlaceholder.getVisibility() != View.VISIBLE) {
                    gifPlaceholder.setVisibility(View.VISIBLE);
                    src.setVisibility(View.GONE);
                }
            }

        });
    }

    public static void loadPicassoImgOriginalWithGifPlaceholder(Context context, String url,
                                                                final ImageView src,
                                                                final ImageView gifPlaceholder) {
        gifPlaceholder.setVisibility(View.VISIBLE);
        src.setVisibility(View.GONE);
        loadPicassoImgOriginalWithGifPlaceholder(context, url, src, new Callback() {

            @Override
            public void onSuccess() {
                if (src.getVisibility() != View.VISIBLE) {
                    src.setVisibility(View.VISIBLE);
                    gifPlaceholder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                if (gifPlaceholder.getVisibility() != View.VISIBLE) {
                    gifPlaceholder.setVisibility(View.VISIBLE);
                    src.setVisibility(View.GONE);
                }
            }

        });
    }

    private static void loadPicassoImgWithGifPlaceholder(Context context, String url, ImageView src, Callback callback) {
        if (!ValidUtil.isEmpty(url)) {
            Picasso picasso = getPicasso(context);
            RequestCreator requestCreator;
            if (!url.startsWith("http")) {
                requestCreator = picasso.load(new File(url));
            } else {
                requestCreator = picasso.load(url);
            }
            requestCreator
                    .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
//                    .centerCrop()
//                    .resize(600, 600)
                    .into(src, callback);
        }
    }

    private static void loadPicassoImgOriginalWithGifPlaceholder(Context context, String url, ImageView src, Callback callback) {
        if (!ValidUtil.isEmpty(url)) {
            Picasso picasso = getPicasso(context);
            RequestCreator requestCreator;
            if (!url.startsWith("http")) {
                requestCreator = picasso.load(new File(url));
            } else {
                requestCreator = picasso.load(url);
            }
            requestCreator
                    .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                    .into(src, callback);
        }
    }

    /**
     * Purpose: resize large image bitmap
     */
    static class BitmapTransform implements Transformation {

        private int maxWidth;
        private int maxHeight;

        BitmapTransform(int maxWidth, int maxHeight) {
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth, targetHeight;
            double aspectRatio;
            if (source.getWidth() > source.getHeight()) {
                targetWidth = maxWidth;
                aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                targetHeight = (int) (targetWidth * aspectRatio);
            } else {
                targetHeight = maxHeight;
                aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                targetWidth = (int) (targetHeight * aspectRatio);
            }
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return maxWidth + "x" + maxHeight;
        }

    }

}
