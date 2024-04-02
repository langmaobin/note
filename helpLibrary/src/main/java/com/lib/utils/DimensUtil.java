package com.lib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class DimensUtil {

    public static float convertPxToDp(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertDpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * dimens.xml generator
     */

    private final String RES_DIR = "\\lib\\src\\main\\res";
    private final String DIMENS_W320_DIR = "\\values-w320dp";
    private final String DIMENS_W600_DIR = "\\values-w600dp";
    private final String DIMENS_W820_DIR = "\\values-w820dp";
    private final String DIMENS_FILE = "\\dimens.xml";

    private DimensUtil() {
        generateDimensFiles();
    }

    private void generateDimensFiles() {
        try {
            String resPath = new File("").getAbsolutePath() + RES_DIR;
            createDimensFile(resPath + DIMENS_W320_DIR + DIMENS_FILE, 0.8);
            createDimensFile(resPath + DIMENS_W600_DIR + DIMENS_FILE, 1.6);
            createDimensFile(resPath + DIMENS_W820_DIR + DIMENS_FILE, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDimensFile(String filepath, double ratio) throws IOException {
        File dimensFile = new File(filepath);
        if (!dimensFile.getParentFile().exists()) {
            dimensFile.getParentFile().mkdirs();
        }
        dimensFile.createNewFile();
        createDimensContent(new PrintWriter(dimensFile), ratio);
    }

    private void createDimensContent(PrintWriter writer, double ratio) {
        writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.println("<resources>");
        writer.println();
        writer.println("    <!-- Common Size Dimens -->");
        writer.println();
        for (int i = 2; i < 1000; i++) {
            writer.println("    <dimen name=\"size_" + i + "dp\">" + Math.round(i * ratio) + "dp</dimen>");
        }
        writer.println();
        writer.println("    <!-- Common Text Size Dimens -->");
        writer.println();
        for (int i = 2; i < 1000; i++) {
            writer.println("    <dimen name=\"size_" + i + "sp\">" + Math.round(i * ratio) + "sp</dimen>");
        }
        writer.println();
        writer.println("</resources>");
        writer.close();
    }

    public static void main(String[] args) {
        new DimensUtil();
    }

}
