package com.ywvision.wyvisionhelper.generator;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

public class MalaysiaStatesGenerator {

    private HashMap<String, Integer> statesMap = new HashMap<>();
    private LinkedHashMap<String, String> statesReadMap = new LinkedHashMap<>();

    private final String RES_FILE = "\\excel\\malaysia_states_format.json";

    private final String ASSETS_DIR = "\\wyvisionhelper\\src\\main\\assets";
    private final String EXCEL_FILE = "\\excel\\malaysia_states_format.xls";

    private MalaysiaStatesGenerator() {
        generateFiles();
    }

    private void initStatesMap() {
        statesMap.put("NA", -1);
        statesMap.put("Johor", 0);
        statesMap.put("Kedah", 1);
        statesMap.put("Kelantan", 2);
        statesMap.put("Kuala Lumpur", 3);
        statesMap.put("Labuan", 4);
        statesMap.put("Melaka", 5);
        statesMap.put("Negeri Sembilan", 6);
        statesMap.put("Pahang", 7);
        statesMap.put("Penang", 8);
        statesMap.put("Perak", 9);
        statesMap.put("Perlis", 10);
        statesMap.put("Putrajaya", 11);
        statesMap.put("Sabah", 12);
        statesMap.put("Sarawak", 13);
        statesMap.put("Selangor", 14);
        statesMap.put("Terengganu", 15);
    }

    private void generateFiles() {
        try {
            initStatesMap();
            readExcelFile();
            createResFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readExcelFile() throws Exception {
        statesReadMap.clear();
        String excelFile = new File("").getAbsolutePath() + ASSETS_DIR + EXCEL_FILE;
        Workbook workbook = Workbook.getWorkbook(new File(excelFile));
        Sheet sheet = workbook.getSheet(0);
        int totalRows = sheet.getRows();
        for (int i = 0; i < totalRows; i++) {
            statesReadMap.put(sheet.getCell(0, i).getContents(), sheet.getCell(1, i).getContents());
        }
        workbook.close();
    }

    private void createResFile() throws Exception {
        String resFile = new File("").getAbsolutePath() + ASSETS_DIR + RES_FILE;
        File file = new File(resFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        PrintWriter writer = new PrintWriter(file);
        writer.println("{");
        int count = 0;
        int statesReadMapSize = statesReadMap.size();
        for (Map.Entry entry : statesReadMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            String endOfLine = (count == statesReadMapSize - 1) ? "" : ",";
            writer.println("  \"" + key+ "\": " + statesMap.get(value) + endOfLine);
            count++;
        }
        writer.println("}");
        writer.close();
    }

    public static void main(String[] args) {
        new MalaysiaStatesGenerator();
    }

}
