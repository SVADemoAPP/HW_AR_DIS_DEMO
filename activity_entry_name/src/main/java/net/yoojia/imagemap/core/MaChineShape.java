package net.yoojia.imagemap.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.View;

import net.yoojia.imagemap.R;
import net.yoojia.imagemap.util.MatrixConverHelper;

public class MaChineShape extends PrruInfoShape {

    private PointF center = new PointF();
    private Bitmap bmp_show;
    private float radius = 20.0f;

    public MaChineShape(Object tag, int coverColor, Context context) {
        super(tag, coverColor, context);
        View view = View.inflate(context, R.layout.shape_machine_room, null);
        this.bmp_show = convertViewToBitmap(view);
        setRadius((float) ((view.getWidth() / 2)));
    }

    @Override
    public void setValues(float... coords) {
        float centerX = coords[0];
        float centerY = coords[1];
        this.center.set(centerX, centerY);
    }

    /**
     * 设置尺寸半径
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void onScale(float scale) {
        PointF pointF = this.center;
        PointF pointF2 = this.center;
        float f = pointF2.x * scale;
        pointF2.x = f;
        pointF2 = this.center;
        float f2 = pointF2.y * scale;
        pointF2.y = f2;
        pointF.set(f, f2);
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        PointF f = MatrixConverHelper.mapMatrixPoint(mOverMatrix, center.x, center.y); //转换坐标
        canvas.drawBitmap(this.bmp_show, f.x - ((float) (this.bmp_show.getWidth() / 2)), f.y - ((float) (this.bmp_show.getHeight() / 2)), this.drawPaint);
        canvas.restore();
    }

    @Override
    public boolean bubbleTag() {
        return false;
    }

    @Override
    public void scaleBy(float scale, float centerX, float centerY) {

    }

    @Override
    public void translate(float deltaX, float deltaY) {
        PointF pointF = this.center;
        pointF.x += deltaX;
        pointF = this.center;
        pointF.y += deltaY;
    }


    @Override
    public boolean inArea(float x, float y) {
        float r = this.radius;
        if (null != null) {
            return false;
        }
        PointF p = this.center;
        float dx = p.x - x;
        float dy = p.y - y;
        float sqrt = (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
        if (sqrt < r/mScale) {
            return true;
        }
        return false;
    }

    @Override
    public PointF getCenterPoint() {
        return MatrixConverHelper.mapMatrixPoint(mOverMatrix, center.x, center.y);
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getPictureUrl() {
        return null;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public float getCenterX() {
        return center.x;
    }

    @Override
    public float getCenterY() {
        return center.y;
    }
}
