package com.huawei.hiardemo.area.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.huawei.hiardemo.area.db.AppDataBase;
import com.huawei.hiardemo.area.db.table.AreaMapData;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Create by 91569
 * Time 2019/7/16
 * Describe :
 */
public class XMlUtils {

    public static void saveDataToText(AreaMapData data) {
        File file = new File(Constant.AR_PATH, getFileNameNoEx(data.getMapName()) + ".text");
        FileOutputStream fos = null;
        String line = data.getMapName() + "%" + data.getPath() + "%" + data.getInitX() + "%" + data.getInitY() + "%" + data.getAngle();
        try {
            fos = new FileOutputStream(file);
            fos.write(line.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static AreaMapData getDataFormText(String filePath) {
        AreaMapData areaMapData = new AreaMapData();
        File file = new File(Constant.AR_PATH, filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return areaMapData;
    }


    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static boolean createXML(AreaMapData data) {
        File file = new File(Constant.AR_PATH, getFileNameNoEx(data.getMapName()) + ".xml");
        FileOutputStream fos = null;
        // 获得xml序列化实例
        XmlSerializer serializer = new KXmlSerializer();
        // 文件写入流实例
        try {
            // 根据文件对象创建一个文件的输出流对象
            fos = new FileOutputStream(file);
            // 设置输出的流及编码
            serializer.setOutput(fos, "GBK");
            // 设置文件的开始
            serializer.startDocument("GBK", true);
            // 设置文件开始标签
            serializer.startTag(null, "root");
            //文件名
            serializer.startTag(null, "mapName");
            serializer.text(data.getMapName());
            serializer.endTag(null, "mapName");

            //文件路径
            serializer.startTag(null, "path");
            serializer.text(data.getPath());
            serializer.endTag(null, "path");

            //坐标x
            serializer.startTag(null, "initX");
            serializer.text(data.getInitX() + "");
            serializer.endTag(null, "initX");

            //坐标Y
            serializer.startTag(null, "initY");
            serializer.text(data.getInitY() + "");
            serializer.endTag(null, "initY");

            //角度
            serializer.startTag(null, "angle");
            serializer.text(data.getAngle() + "");
            serializer.endTag(null, "angle");

            // 设置文件结束标签
            serializer.endTag(null, "root");
            // 文件的结束
            serializer.endDocument();

            serializer.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 读入AppData数据
     */
    public static AreaMapData readAreaMapData(String filePath) {
        AreaMapData areaMapData = new AreaMapData();
        try {
            File file = new File(Constant.AR_PATH, filePath);
            FileInputStream fis = new FileInputStream(file);
            // 获得pull解析器对象
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");
            int eventType = parser.getEventType(); // 获得事件类型

            String mapName = null; //地图名称
            String path = null;     //path路径
            String initX = null;    //初始化x
            String initY = null;    //初始化y
            String angle = null;    //初始化角度

            while ((eventType != XmlPullParser.END_TAG) && eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // 获得当前节点的名称
                switch (eventType) {
                    case XmlPullParser.START_TAG: // 当前等于开始节点 <person>
                        if ("mapName".equals(tagName)) { // <mapName>
                            mapName = parser.nextText();
                        } else if ("path".equals(tagName)) { // <path>
                            path = parser.nextText();
                        } else if ("initX".equals(tagName)) { // <initX>
                            initX = parser.nextText();
                        } else if ("initY".equals(tagName)) { // <initY>
                            initY = parser.nextText();
                        } else if ("angle".equals(tagName)) { // <angle>
                            angle = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </persons>

                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // 获得下一个事件类型
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return areaMapData;
    }


    public static AreaMapData parseXml(String filePath) {
        // 声明返回值
        AreaMapData areaMapData = null;
        // 获取解析对象
        XmlPullParser xmlPullParser = new KXmlParser();
        try {
            File file = new File(Constant.AR_PATH, getFileNameNoEx(filePath) + ".xml");
            FileInputStream fis = new FileInputStream(file);
            // 设置输入流的编码
            xmlPullParser.setInput(fis, "GBK");
            // 获取解析的事件类型
            int eventType = xmlPullParser.getEventType();
            String mapName = "";
            String path = "";
            String initX = "";
            String initY = "";
            String angle = "";
            // 判断文件解析的是否完毕
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xmlPullParser.getName();

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        areaMapData = new AreaMapData();
                        break;

                    case XmlPullParser.START_TAG:

                        if ("mapName".equals(tagName)) { // <mapName>
                            mapName = xmlPullParser.nextText();
                        } else if ("path".equals(tagName)) { // <path>
                            path = xmlPullParser.nextText();
                        } else if ("initX".equals(tagName)) { // <initX>
                            initX = xmlPullParser.nextText();
                        } else if ("initY".equals(tagName)) { // <initY>
                            initY = xmlPullParser.nextText();
                        } else if ("angle".equals(tagName)) { // <angle>
                            angle = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        areaMapData.setAngle(Float.parseFloat(angle));
                        areaMapData.setInitX(Float.parseFloat(initX));
                        areaMapData.setInitY(Float.parseFloat(initY));
                        areaMapData.setMapName(mapName);
                        areaMapData.setPath(path);
                        break;
                }
                //读取下一个事件
                eventType = xmlPullParser.next();
            }
            //关闭输入流
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaMapData;

    }

}
