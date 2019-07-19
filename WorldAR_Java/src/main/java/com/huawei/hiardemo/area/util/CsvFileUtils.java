package com.huawei.hiardemo.area.util;

import com.huawei.hiardemo.area.bean.SaveData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvFileUtils {

    // 写入 .csv 文件
    public static void writeCsv(String filePath, List<SaveData> saveData) {
        try {
            File file = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));  // 防止出现乱码
            // 添加头部
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("timestamp", "rsrp", "x", "y", "z"));
            // 添加内容
            for (SaveData data : saveData) {
                csvPrinter.printRecord(
                        data.getTime(), data.getRsRp(), data.getX(), data.getY(), data.getZ());
            }
            csvPrinter.printRecord();
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}