package com.huawei.hiardemo.area.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chinasoft_gyr on 2018/11/8.
 */

public class FileUtils {

    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 写入Sdcard
     *
     * @param bytes 二进制数组
     */
    public static void writeToDisk(String parentPath, String fileName, byte[] bytes) {
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File targetFile = new File(parentFile, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFileFromSDcard(String path) {

    }

    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static boolean isZipFile(File file) {
        if (file.exists() && file.getName().toLowerCase().contains(".zip")) {
            return true;
        }
        return false;

    }

    /**
     * 获取文件名不带后缀名的
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
}
