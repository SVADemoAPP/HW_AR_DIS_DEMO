package com.huawei.hiardemo.area.util;

import java.io.File;

public class FileUtil {
    public static boolean deleteDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            if (null == childs) {
                return false;
            }
            boolean result = true;
            for (File child : childs) {
                result = result && deleteDirs(child.getAbsolutePath());
            }
            try {
                boolean ret = file.delete();
                return result && ret;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } else {
            try {
                boolean ret = file.delete();
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
