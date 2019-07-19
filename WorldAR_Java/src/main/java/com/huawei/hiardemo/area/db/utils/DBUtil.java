package com.huawei.hiardemo.area.db.utils;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.huawei.hiardemo.area.db.table.ARLoctionModel;
import com.huawei.hiardemo.area.db.table.ARLoctionModel_Table;
import com.huawei.hiardemo.area.db.table.AreaMapData;
import com.huawei.hiardemo.area.db.table.AreaMapData_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

/**
 * 数据库操作工具类
 */

public class DBUtil {
    /**
     * 保存地图信息
     *
     * @param mapName 地图名词
     * @param path    地图路径
     * @param x       二维地图初始化x
     * @param y       二维地图初始化y
     * @param angle   二维地图初始化方向
     * @return
     */
    public static boolean saveMapData(String mapName, String path, float x, float y, float angle) {
        try {
            AreaMapData areaMapData = new AreaMapData();
            areaMapData.setMapName(mapName);
            areaMapData.setPath(path);
            areaMapData.setInitX(x);
            areaMapData.setInitY(y);
            areaMapData.setAngle(angle);
            long insert = areaMapData.insert();
            return true;
        } catch (Exception e) {
            e.toString();
            return false;
        }
    }

    /**
     * 本地存储路径
     *
     * @param path
     * @return
     */
    public static void getSearchData(String path, final DBListener dbListener) {
        OperatorGroup op = OperatorGroup.clause().and(AreaMapData_Table.path.eq(path));
        SQLite.select().from(AreaMapData.class).where(op).async().querySingleResultCallback(new QueryTransaction.QueryResultSingleCallback<AreaMapData>() {
            @Override
            public void onSingleQueryResult(QueryTransaction transaction, @Nullable AreaMapData areaMapData) {
                dbListener.asyncQueryMapData(areaMapData);
            }
        }).execute();
        ;
//        SQLite.select().from(AreaMapData.class).where(op).async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<AreaMapData>() {
//            @Override
//            public void onListQueryResult(QueryTransaction transaction, @NonNull List<AreaMapData> tResult) {
//                dbListener.asyncQueryMapData(tResult);
//            }
//        }).execute();
    }

    /**
     * @param buildingName 楼宇名称
     * @param siteName     站点名称
     * @param initPoint    初始化坐标点
     * @return
     */
    public static boolean addARLocation(String buildingName, String siteName, PointF initPoint) {
        try {
            ARLoctionModel arLoctionModel = new ARLoctionModel();
            arLoctionModel.setBuildingName(buildingName);
            arLoctionModel.setSiteName(siteName);
            arLoctionModel.setLocation(initPoint.x + "," + initPoint.y);
            long insert = arLoctionModel.insert();
            return true;
        } catch (Exception e) {
            e.toString();
            return false;
        }

    }


    /**
     * @param id           id
     * @param buildingName 楼宇名称
     * @param siteName     站点名称
     * @param initPoint    初始化坐标点
     * @return
     */
    public static boolean updateRLocation(int id, String buildingName, String siteName, PointF initPoint) {
        try {
            ARLoctionModel arLoctionModel = new ARLoctionModel();
            arLoctionModel.setId(id);
            arLoctionModel.setBuildingName(buildingName);
            arLoctionModel.setSiteName(siteName);
            arLoctionModel.setLocation(initPoint.x + "," + "," + initPoint.y);
            boolean update = arLoctionModel.update();
            return update;
        } catch (Exception e) {
            e.toString();
            return false;
        }

    }

    /***
     * 事务查询（async）
     * @param buildingName 楼宇名称
     * @param siteName     站点名称
     * @return
     */
    public static void asyncQueryARLocation(String buildingName, String siteName, final DBListener dbListener) {
        OperatorGroup op = OperatorGroup.clause().and(ARLoctionModel_Table.buildingName.eq(buildingName)).and(ARLoctionModel_Table.siteName.eq(siteName));  //连接多
        SQLite.select().from(ARLoctionModel.class).where(op).async().queryListResultCallback(new QueryTransaction.QueryResultListCallback<ARLoctionModel>() {
            @Override
            public void onListQueryResult(QueryTransaction transaction, @NonNull List<ARLoctionModel> tResult) {
                dbListener.asyncQueryData(tResult);
            }
        }).execute();
    }

    /**
     * 同步查询
     *
     * @param buildingName
     * @param siteName
     */
    public static List<ARLoctionModel> syncQueryARLocation(String buildingName, String siteName) {
        OperatorGroup op = OperatorGroup.clause().and(ARLoctionModel_Table.buildingName.eq(buildingName)).and(ARLoctionModel_Table.siteName.eq(siteName));  //连接多个查询条件
        List<ARLoctionModel> arLoctionModels = SQLite.select().from(ARLoctionModel.class).where(op).queryList();
        return arLoctionModels;
    }

    /**
     * 删除
     *
     * @param arLoctionModel
     */
    public static void deleteARLocation(ARLoctionModel arLoctionModel) {
        arLoctionModel.delete();
    }

    /**
     * 删除指定id数据
     *
     * @param id
     */
    public static void deleteARLocation(int id) {
        OperatorGroup op = OperatorGroup.clause().and(ARLoctionModel_Table.id.eq(id));  //连接多个查询条件
        ARLoctionModel arLoctionModel = SQLite.select().from(ARLoctionModel.class).where(op).querySingle();
        arLoctionModel.delete();
    }

    public interface DBListener {
        /**
         * 查询数据回调
         *
         * @param data 回调数据
         */
        void asyncQueryData(List<ARLoctionModel> data);

        void asyncQueryMapData(AreaMapData data);
    }
}
