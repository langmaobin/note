package com.ywvision.wyvisionhelper.generator;

import com.lib.utils.ValidUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

public class ApiErrorGenerator {

    private LinkedHashMap<String, List<ApiErrObj>> apiErrObjMap = new LinkedHashMap<>();

    private final String JAVA_PACKAGE_NAME = "com.ywvision.wyvisionhelper.api";
    private final String JAVA_CLASS_NAME = "ApiError";
    private final String[] JAVA_IMPORT_LIBS = new String[]{
            "android.content.Context",
            "com.ywvision.wyvisionhelper.R",
            "com.lib.utils.ValidUtil"
    };

    private final String JAVA_DIR = "\\wyvisionhelper\\src\\main\\java";
    private final String JAVA_PACKAGE = "\\com\\izyoo\\wyvisionhelper\\api";
    private final String JAVA_FILE = "\\ApiError.java";

    private final String RES_FILE = "\\excel\\api_error_strings.xml";

    private final String ASSETS_DIR = "\\wyvisionhelper\\src\\main\\assets";
    private final String EXCEL_FILE = "\\excel\\api_error_data.xls";

    private ApiErrorGenerator() {
        generateFiles();
    }

    private void generateFiles() {
        try {
            readExcelFile();
            createJavaFile();
            createResFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readExcelFile() throws Exception {
        apiErrObjMap.clear();
        String excelFile = new File("").getAbsolutePath() + ASSETS_DIR + EXCEL_FILE;
        Workbook workbook = Workbook.getWorkbook(new File(excelFile));
        Sheet sheet = workbook.getSheet(0);
        int totalRows = sheet.getRows();
        String url;
        String lastUrl = "";
        List<ApiErrObj> apiErrObjs = new ArrayList<>();
        for (int i = 0; i < totalRows; i++) {
            url = sheet.getCell(0, i).getContents();
            if (ValidUtil.isEmpty(url)) {
                break;
            }
            if (i == 0) {
                lastUrl = sheet.getCell(0, i).getContents();
            } else if (i == totalRows - 1) {
                apiErrObjs.add(new ApiErrObj(
                        sheet.getCell(1, i).getContents(),
                        sheet.getCell(2, i).getContents(),
                        sheet.getCell(3, i).getContents()));
                apiErrObjMap.put(lastUrl, new ArrayList<>(apiErrObjs));
            } else if (!lastUrl.equals(url)) {
                apiErrObjMap.put(lastUrl, new ArrayList<>(apiErrObjs));
                apiErrObjs = new ArrayList<>();
            }
            apiErrObjs.add(new ApiErrObj(
                    sheet.getCell(1, i).getContents(),
                    sheet.getCell(2, i).getContents(),
                    sheet.getCell(3, i).getContents()));
            lastUrl = sheet.getCell(0, i).getContents();
        }
        workbook.close();
    }

    private void createJavaFile() throws Exception {
        String javaFile = new File("").getAbsolutePath() + JAVA_DIR + JAVA_PACKAGE + JAVA_FILE;
        File file = new File(javaFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        PrintWriter writer = new PrintWriter(file);
        writer.println("package " + JAVA_PACKAGE_NAME + ";");
        writer.println();
        for (String importLib : JAVA_IMPORT_LIBS) {
            writer.println("import " + importLib + ";");
        }
        writer.println();
        writer.println("public class " + JAVA_CLASS_NAME + " {");
        writer.println();
        for (Map.Entry entry : apiErrObjMap.entrySet()) {
            List<ApiErrObj> apiErrObjs = (List<ApiErrObj>) entry.getValue();
            for (ApiErrObj apiErrObj : apiErrObjs) {
                writer.println("public static final String " + apiErrObj.name.toUpperCase() + " = \"" + apiErrObj.code + "\";");
            }
        }
        writer.println();
        writer.println("public static String getMessage(Context context, String url, String statusCode) {");
        writer.println("if (!ValidUtil.isEmpty(url) && !ValidUtil.isEmpty(statusCode)) {");
        boolean isFirst = true;
        for (Map.Entry entry : apiErrObjMap.entrySet()) {
            String url = (String) entry.getKey();
            List<ApiErrObj> apiErrObjs = (List<ApiErrObj>) entry.getValue();
            if (!url.equals("API_NONE")) {
                if (isFirst) {
                    writer.println("switch (url) {");
                    isFirst = false;
                }
                writer.println("case ApiMethod." + url + ":");
            }
            writer.println("switch (statusCode) {");
            for (ApiErrObj apiErrObj : apiErrObjs) {
                writer.println("case " + apiErrObj.name.toUpperCase() + ": return context.getString(R.string.__t_" + apiErrObj.name.toLowerCase() + ");");
            }
            writer.println("}");
            if (!isFirst) {
                writer.println("break;");
            }
        }
        writer.println("}");
        writer.println("}");
        writer.println("return context.getString(R.string.__t_api_status_alert_unknown_system_error);");
        writer.println("}");
        writer.println();
        writer.println("}");
        writer.close();
    }

    private void createResFile() throws Exception {
        String resFile = new File("").getAbsolutePath() + ASSETS_DIR + RES_FILE;
        File file = new File(resFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        PrintWriter writer = new PrintWriter(file);
        writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.println("<resources>");
        writer.println("    <!-- Label API Error Message -->");
        for (Map.Entry entry : apiErrObjMap.entrySet()) {
            List<ApiErrObj> apiErrObjs = (List<ApiErrObj>) entry.getValue();
            for (ApiErrObj apiErrObj : apiErrObjs) {
                writer.println("    <string name=\"__t_" + apiErrObj.name.toLowerCase()/*.replace("_", ".")*/ + "\">" + apiErrObj.message + "</string>");
            }
        }
//        writer.println("    <string name=\"err_unknown_system_error\">Unknown system error</string>");
        writer.println("    <!-- End Label API Error Message -->");
        writer.println("</resources>");
        writer.close();
    }

    private class ApiErrObj {

        String name;
        String code;
        String message;

        public ApiErrObj(String name, String code, String message) {
            this.name = name;
            this.code = code;
            this.message = message;
        }

    }

    public static void main(String[] args) {
        new ApiErrorGenerator();
    }

}