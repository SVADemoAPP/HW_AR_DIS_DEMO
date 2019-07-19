package com.huawei.hiardemo.area.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;

public class GifImageView extends ImageView {

    /**
     * 播放GIF动画的关键类
     */
    private Movie mMovie;

    /**
     * 记录动画开始的时间
     */
    private long mMovieStart;

    /**
     * GIF图片的宽度
     */
    private int mImageWidth;

    /**
     * GIF图片的高度
     */
    private int mImageHeight;

    /**
     * PowerImageView构造函数。
     * 
     * @param context
     */
    public GifImageView(Context context) {
        super(context);
    }

    /**
     * PowerImageView构造函数。
     * 
     * @param context
     */
    public GifImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * PowerImageView构造函数，在这里完成所有必要的初始化操作。
     * 
     * @param context
     */
    public GifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        int resourceId = getResourceId(context, attrs);

        if (resourceId != 0) {
            // 当资源id不等于0时，就去获取该资源的流
            InputStream is = getResources().openRawResource(resourceId);
            // 使用Movie类对流进行解码
            mMovie = Movie.decodeStream(is);
            if (mMovie != null) {
                // 如果返回值不等于null，就说明这是一个GIF图片
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                if(bitmap != null){
                    mImageWidth = bitmap.getWidth();
                    mImageHeight = bitmap.getHeight();

                    bitmap.recycle();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mMovie == null) {
            // mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
            super.onDraw(canvas);
        } else {
            // mMovie不等于null，说明是张GIF图片
            // 调用playMovie()方法播放GIF动画
            if(!playMovie(canvas)){
                invalidate();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
            // 如果是GIF图片则重写设定PowerImageView的大小
            setMeasuredDimension(mImageWidth, mImageHeight);
        }
    }

    /**
     * 开始播放GIF动画，播放完成返回true，未完成返回false。
     * 
     * @param canvas
     * @return 播放完成返回true，未完成返回false。
     */
    private boolean playMovie(Canvas canvas) {

        long now = SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int duration = mMovie.duration();

        if (duration == 0) {
            duration = 1000;
        }

        int relTime = (int) ((now - mMovieStart) % duration);

        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, 0);

        if ((now - mMovieStart) >= duration) {
            //播放完成，显示最后一帧
            mMovie.setTime(duration);
            mMovie.draw(canvas, 0, 0);
            return true;
        }

        return false;
    }

    /**
     * 获取到src指定图片资源所对应的id。
     * 
     * @param context
     * @param attrs
     * @return 返回布局文件中指定图片资源所对应的id，没有指定任何图片资源就返回0。
     */
    private int getResourceId(Context context, AttributeSet attrs) {

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if (attrs.getAttributeName(i).equals("src")) {
                return attrs.getAttributeResourceValue(i, 0);
            }
        }
        return 0;
    }

}
