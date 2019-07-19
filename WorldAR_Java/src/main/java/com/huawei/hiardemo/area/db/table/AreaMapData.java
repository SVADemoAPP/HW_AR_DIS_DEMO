package com.huawei.hiardemo.area.db.table;

import com.huawei.hiardemo.area.db.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Create by 91569
 * Time 2019/7/15
 * Describe :
 */
@Table(database = AppDataBase.class)
public class AreaMapData extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String mapName;

    @Column
    private String path;

    @Column
    private float initX;

    @Column
    private float initY;

    @Column
    private float angle;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getInitX() {
        return initX;
    }

    public void setInitX(float initX) {
        this.initX = initX;
    }

    public float getInitY() {
        return initY;
    }

    public void setInitY(float initY) {
        this.initY = initY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
