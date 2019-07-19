package com.huawei.hiardemo.area.util;

import com.huawei.hiar.ARSession;
import com.huawei.hiardemo.area.ShareMapHelper;
import com.huawei.hiardemo.area.db.table.AreaMapData;
import com.huawei.hiardemo.area.db.utils.DBUtil;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * Create by 91569
 * Time 2019/7/12
 * Describe :
 */
public class ARUtils {
    /**
     * 存储AR Plane平面
     *
     * @param arSession
     */
    public static void saveARSeesionPlane(ARSession arSession, float x, float y, float angle) {
        String fileName = "map" + System.currentTimeMillis() + ".data";
        String path = Constant.AR_PATH + File.separator + fileName;
        ShareMapHelper.writeBuffer(Constant.AR_PATH, fileName, arSession.saveSharedData());
        DBUtil.saveMapData(fileName, path, x, y, angle); //保存完成ar地图，再次保存ar地图相关信息如文件名，文件路径，x,y,angle。
        AreaMapData areaMapData = new AreaMapData();
        areaMapData.setPath(path);
        areaMapData.setMapName(fileName);
        areaMapData.setInitX(x);
        areaMapData.setInitY(y);
        areaMapData.setAngle(angle);
        XMlUtils.createXML(areaMapData); //写入xml
//        ByteBuffer byteBuffer = arSession.saveSharedData(); //获取到arSessi
//        byte[] bytes = new byte[byteBuffer.remaining()];
//        byteBuffer.get(bytes); //buffer写入给byte
//        FileUtils.writeToDisk(Constant.AR_PATH, "map.data", bytes);
    }

    /**
     * 获取SDcard存储的Plane
     *
     * @param path 本地的存储路径
     * @return
     */
    public static ByteBuffer loadARSeesionPlane(String path) {
        File planesFile = new File(path);
        byte[] bytes = ShareMapHelper.readBuffer(planesFile);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
        byteBuffer.put(bytes);
        return byteBuffer;
    }

}
