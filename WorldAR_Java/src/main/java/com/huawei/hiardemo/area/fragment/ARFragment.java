package com.huawei.hiardemo.area.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiar.ARAnchor;
import com.huawei.hiar.ARCamera;
import com.huawei.hiar.ARConfigBase;
import com.huawei.hiar.AREnginesApk;
import com.huawei.hiar.AREnginesSelector;
import com.huawei.hiar.ARFrame;
import com.huawei.hiar.ARHitResult;
import com.huawei.hiar.ARLightEstimate;
import com.huawei.hiar.ARPlane;
import com.huawei.hiar.ARPoint;
import com.huawei.hiar.ARPointCloud;
import com.huawei.hiar.ARPose;
import com.huawei.hiar.ARSession;
import com.huawei.hiar.ARTrackable;
import com.huawei.hiar.ARWorldTrackingConfig;
import com.huawei.hiar.exceptions.ARUnSupportedConfigurationException;
import com.huawei.hiar.exceptions.ARUnavailableClientSdkTooOldException;
import com.huawei.hiar.exceptions.ARUnavailableDeviceNotCompatibleException;
import com.huawei.hiar.exceptions.ARUnavailableEmuiNotCompatibleException;
import com.huawei.hiar.exceptions.ARUnavailableServiceApkTooOldException;
import com.huawei.hiar.exceptions.ARUnavailableServiceNotInstalledException;
import com.huawei.hiar.exceptions.ARUnavailableUserDeclinedInstallationException;
import com.huawei.hiardemo.area.DisplayRotationHelper;
import com.huawei.hiardemo.area.MainActivity;
import com.huawei.hiardemo.area.R;
import com.huawei.hiardemo.area.ShareMapHelper;
import com.huawei.hiardemo.area.UtilsCommon;
import com.huawei.hiardemo.area.activity.FloorMapActivity;
import com.huawei.hiardemo.area.framework.sharef.CameraPermissionHelper;
import com.huawei.hiardemo.area.rendering.BackgroundRenderer;
import com.huawei.hiardemo.area.rendering.PlaneRenderer;
import com.huawei.hiardemo.area.rendering.PointCloudRenderer;
import com.huawei.hiardemo.area.rendering.VirtualObjectRenderer;
import com.huawei.hiardemo.area.util.ARUtils;
import com.huawei.hiardemo.area.util.Constant;
import com.huawei.hiardemo.area.util.LogUtils;
import com.huawei.hiardemo.area.util.network.ApiManager;
import com.huawei.hiardemo.area.util.network.api.CallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ARFragment extends Fragment implements GLSurfaceView.Renderer {


    private ARPose mArPose;  //摄像头位置的实时坐标
    private ArCameraListener mListener;  //监听器
    private long intervalTime = 500;
    private Handler mGetArPoseHandler = new Handler();
    private boolean isFirstFlag = false;
    private Runnable mGetArPosRunnable = new Runnable() {  //定时获取坐标
        @Override
        public void run() {
            mGetArPoseHandler.postDelayed(this, intervalTime);
            if (mListener != null) {
                mListener.getCameraPose(mArPose);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mArX.setText("X:" + mArPose.tz());
                        mArY.setText("Y:" + mArPose.tx());
                    }
                });
            }
        }
    };
    private boolean hasPlane = false;
    private boolean hasplat = false;
    //    private boolean initARFlag = false;
    private View mTvRsrp;
    private View mTvcode;
    private TextView mPrruNeCode;
    private TextView mPrruRsrp;

    private int mWidth;
    private int mHeight;
    /**
     * 传感器参数
     */
    private SensorManager mSensorManager;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    private float azimuthAngle;
    private PointF oldPointF = new PointF(0, 0);

    String mPath = "";//上个页面选择的map.data的路径
    private TextView mArX;
    private TextView mArY;
    private ByteBuffer mByte;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mArView = inflater.inflate(R.layout.activity_main, container, false);
        mTvRsrp = mArView.findViewById(R.id.rsrp);
        mTvcode = mArView.findViewById(R.id.necode);
        mArX = mArView.findViewById(R.id.ar_x);
        mArY = mArView.findViewById(R.id.ar_y);
        mPrruNeCode = mArView.findViewById(R.id.prru_ne_code);
        mPrruRsrp = mArView.findViewById(R.id.prruRsrp);
        mSearchingTextView = mArView.findViewById(R.id.searchingTextView);
        mSurfaceView = mArView.findViewById(R.id.surfaceview);
        mDisplayRotationHelper = new DisplayRotationHelper(getActivity());
        mPath = getActivity().getIntent().getStringExtra("arpath");
        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onSingleTap(e);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        if (mPath != null && !mPath.equals("")) {
            mByte = ARUtils.loadARSeesionPlane(mPath);
        }

        // Set up renderer.
        mSurfaceView.setPreserveEGLContextOnPause(true);
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        mSurfaceView.setRenderer(this);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        installRequested = false;


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        calculateOrientation();
        return mArView;
    }

    private void setView(final float azimuthAngle) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPrruNeCode.setText(azimuthAngle + "");

            }
        });
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private ARSession mSession;
    private GLSurfaceView mSurfaceView;
    private GestureDetector mGestureDetector;
    private DisplayRotationHelper mDisplayRotationHelper;

    private BackgroundRenderer mBackgroundRenderer = new BackgroundRenderer();
    private VirtualObjectRenderer mVirtualObject = new VirtualObjectRenderer();
    private PlaneRenderer mPlaneRenderer = new PlaneRenderer();
    private PointCloudRenderer mPointCloud = new PointCloudRenderer();

    private final float[] mAnchorMatrix = new float[UtilsCommon.MATRIX_NUM];
    private static final float[] DEFAULT_COLOR = new float[]{0f, 0f, 0f, 0f};

    // Anchors created from taps used for object placing with a given color.
//    private static class ColoredARAnchor {
//        public final ARAnchor anchor;
//        public final float[] color;
//
//        public ColoredARAnchor(ARAnchor a, float[] color4f) {
//            this.anchor = a;
//            this.color = color4f;
//        }
//    }

    private ArrayBlockingQueue<MotionEvent> mQueuedSingleTaps = new ArrayBlockingQueue<>(2);
    private List<ARAnchor> mAnchors = new ArrayList<>();
    private ARFrame mFrame;

    private float mScaleFactor = 0.15f;
    private boolean installRequested;
    private float updateInterval = 0.5f;
    private long lastInterval;
    private int frames = 0;
    private float fps;
    private TextView mFpsTextView;
    private TextView mSearchingTextView;
    private boolean initArFlag = false;

    public ARPose getArPose() {
        return mArPose;
    }

    @Override
    public void onResume() {
        super.onResume();
        createSession();
        mSession.resume();
        mSurfaceView.onResume();
        mDisplayRotationHelper.onResume();
        lastInterval = System.currentTimeMillis();

        // 注册监听
        mSensorManager.registerListener(new MySensorEventListener(),
                accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(new MySensorEventListener(), magnetic,
                Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void createSession() {
        Exception exception = null;
        String message = null;

        if (null == mSession) {
            try {
                //If you do not want to switch engines, AREnginesSelector is useless.
                // You just need to use AREnginesApk.requestInstall() and the default engine
                // is Huawei AR Engine.
                AREnginesSelector.AREnginesAvaliblity enginesAvaliblity = AREnginesSelector.checkAllAvailableEngines(getActivity());
                if ((enginesAvaliblity.ordinal() &
                        AREnginesSelector.AREnginesAvaliblity.HWAR_ENGINE_SUPPORTED.ordinal()) != 0) {

                    AREnginesSelector.setAREngine(AREnginesSelector.AREnginesType.HWAR_ENGINE);

                    switch (AREnginesApk.requestInstall(getActivity(), !installRequested)) {
                        case INSTALL_REQUESTED:
                            installRequested = true;
                            return;
                        case INSTALLED:
                            break;
                    }

                    if (!CameraPermissionHelper.hasPermission(getActivity())) {
                        CameraPermissionHelper.requestPermission(getActivity());
                        return;
                    }

                    mSession = new ARSession(/*context=*/getActivity());
                    ARConfigBase config = new ARWorldTrackingConfig(mSession);
                    mSession.configure(config);
                }


            } catch (ARUnavailableServiceNotInstalledException e) {
                message = "Please install HuaweiARService.apk";
                exception = e;
            } catch (ARUnavailableServiceApkTooOldException e) {
                message = "Please update HuaweiARService.apk";
                exception = e;
            } catch (ARUnavailableClientSdkTooOldException e) {
                message = "Please update this app";
                exception = e;
            } catch (ARUnavailableDeviceNotCompatibleException e) {
                message = "This device does not support Huawei AR Engine ";
                exception = e;
            } catch (ARUnavailableEmuiNotCompatibleException e) {
                message = "Please update EMUI version";
                exception = e;
            } catch (ARUnavailableUserDeclinedInstallationException e) {
                message = "Please agree to install!";
                exception = e;
            } catch (ARUnSupportedConfigurationException e) {
                message = "The configuration is not supported by the device!";
                exception = e;
            } catch (Exception e) {
                message = "exception throwed";
                exception = e;
            }
            if (message != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Creating sesson", exception);
                if (mSession != null) {
                    mSession.stop();
                    mSession = null;
                }
                return;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSession != null) {
            mDisplayRotationHelper.onPause();
            mSurfaceView.onPause();
            mSession.pause();
        }
        mSensorManager.unregisterListener(new MySensorEventListener());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!CameraPermissionHelper.hasPermission(getActivity())) {
            Toast.makeText(getActivity(),
                    "This application needs camera permission.", Toast.LENGTH_LONG).show();

            getActivity().finish();
        }
    }


    private void onSingleTap(MotionEvent e) {
        mQueuedSingleTaps.offer(e);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        mBackgroundRenderer.createOnGlThread(/*context=*/getActivity());

        try {
            mVirtualObject.createOnGlThread(/*context=*/getActivity(), "AR_logo.obj", "AR_logo.png");
            mVirtualObject.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to read plane texture");
        }
        try {
            mPlaneRenderer.createOnGlThread(/*context=*/getActivity(), "U5.png");
        } catch (IOException e) {
            Log.e(TAG, "Failed to read plane texture");
        }

        mPointCloud.createOnGlThread(/*context=*/getActivity());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        GLES20.glViewport(0, 0, width, height);
        mDisplayRotationHelper.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        setView(azimuthAngle);//设置角度
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (null == mSession) {
            return;
        }
        mDisplayRotationHelper.updateSessionIfNeeded(mSession);

        try {
            mSession.setCameraTextureName(mBackgroundRenderer.getTextureId());
            mFrame = mSession.update();
            if (!initArFlag) {
                initArFlag = true;
                if (mPath != null && !mPath.equals("")) {
                    File file = new File(mPath);
                    if (file.exists()) {
//                        loadSdCardMap(mPath);
                        mSession.loadSharedData(mByte);
                        setARAnchors(new  File(Constant.AR_PATH + File.separator + "ar.data"));
                    }
                }
            }
            ARCamera camera = mFrame.getCamera();
            mArPose = camera.getDisplayOrientedPose();  //获取摄像头位置（即为定位位置）
            if (!isFirstFlag) {  //判断第一次进入
                if (hasPlane) { //开始定位的标志
                    isFirstFlag = true;
                    Log.e("XHF", "定位开始");
                    ((FloorMapActivity) getActivity()).startCollectionData();
                    mGetArPoseHandler.post(mGetArPosRunnable);
                }
            }
            mBackgroundRenderer.draw(mFrame);

            handleTap(mFrame, camera);

            if (camera.getTrackingState() == ARTrackable.TrackingState.PAUSED) {
                return;
            }

            float[] projmtx = new float[UtilsCommon.MATRIX_NUM];
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

            float[] viewmtx = new float[UtilsCommon.MATRIX_NUM];
            camera.getViewMatrix(viewmtx, 0);

            ARLightEstimate le = mFrame.getLightEstimate();
            float lightIntensity = 1;
            if (le.getState() != ARLightEstimate.State.NOT_VALID) {
                lightIntensity = le.getPixelIntensity();
            }
            ARPointCloud arPointCloud = mFrame.acquirePointCloud();
            mPointCloud.update(arPointCloud);
            mPointCloud.draw(viewmtx, projmtx);
            arPointCloud.release();

            if (mSearchingTextView != null) {
                for (ARPlane plane : mSession.getAllTrackables(ARPlane.class)) {
                    if (plane.getType() != ARPlane.PlaneType.UNKNOWN_FACING &&
                            plane.getTrackingState() == ARTrackable.TrackingState.TRACKING) {
                        hasplat = true;
                        hideLoadingMessage();
                        break;
                    } else {
                        hasplat = false;
                    }
                }
            }
            mPlaneRenderer.drawPlanes(mSession.getAllTrackables(ARPlane.class), camera.getDisplayOrientedPose(), projmtx);

            Iterator<ARAnchor> ite = mAnchors.iterator();
            StringBuffer stringBuffer = new StringBuffer();
            while (ite.hasNext()) {
                ARAnchor coloredAnchor = ite.next();
                stringBuffer.append("--");
                stringBuffer.append(coloredAnchor.getTrackingState());
                stringBuffer.append("--");
                stringBuffer.append((coloredAnchor.getTrackingState() == ARTrackable.TrackingState.STOPPED));
                stringBuffer.append("--");
                if (coloredAnchor.getTrackingState() == ARTrackable.TrackingState.STOPPED) {
                    ite.remove();
                } else{
                    coloredAnchor.getPose().toMatrix(mAnchorMatrix, 0);
                    mVirtualObject.updateModelMatrix(mAnchorMatrix, mScaleFactor);
                    mVirtualObject.draw(viewmtx, projmtx, lightIntensity, DEFAULT_COLOR);
                }
            }
            LogUtils.d("TrackingState",stringBuffer.toString());

        } catch (Throwable t) {
            Log.e(TAG, "Exception on the OpenGL thread", t);
        }

    }


    private void hideLoadingMessage() {
        hasPlane = true;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSearchingTextView != null) {
                    mSearchingTextView.setVisibility(View.GONE);
                    mSearchingTextView = null;
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        if (mSession != null) {
            mSession.stop();
            mSession = null;
        }
        super.onDestroy();
    }

    float FPSCalculate() {
        ++frames;
        long timeNow = System.currentTimeMillis();
        if (((timeNow - lastInterval) / 1000) > updateInterval) {
            fps = (frames / ((timeNow - lastInterval) / 1000.0f));
            frames = 0;
            lastInterval = timeNow;
        }
        return fps;
    }

    public void setArCameraListener(ArCameraListener arCameraListener) {
        mListener = arCameraListener;
    }

    public interface ArCameraListener {
        void getCameraPose(ARPose arPose);
    }

    public void setCameraPose() {
        mAnchors.add(mSession.createAnchor(mArPose));
    }

    public ByteBuffer getMapData() {
        return mSession.saveSharedData();
    }

    public Collection<ARAnchor> getARAnchors() {
        return Collections.unmodifiableList(mAnchors);
    }

    public ARSession getARSession() {
        return mSession;
    }

    public void destroy() {
        mSession.stop();
        mGetArPoseHandler.removeCallbacks(mGetArPosRunnable);
    }

    public void setMapData(byte[] map) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(map.length);
        byteBuffer.put(map);
        mSession.loadSharedData(byteBuffer);
    }

    public void setARAnchors(final File arData) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int count = 0;
                while (true) {
                    ARFrame.AlignState state = mFrame.getAlignState();
                    Log.e("ARFrame.AlignState", state.toString());
                    if (state == ARFrame.AlignState.SUCCESS) {
                        mAnchors = ShareMapHelper.readAnchorFromFile(arData, getARSession());
                        /*getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvRsrp.setVisibility(View.VISIBLE);
                                mTvcode.setVisibility(View.VISIBLE);
                                mPrruNeCode.setVisibility(View.VISIBLE);
                                mPrruNeCode.setVisibility(View.VISIBLE);
                            }
                        });*/
                        break;
                    } else if (state == ARFrame.AlignState.PROCESSING && count == 0) {
                        count++;
                    }
                }
            }
        }).start();
    }

    /**
     * 加载SD卡地图 xhf
     */
    public void loadSdCardMap(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int count = 0;
                while (true) {
                    if (mFrame != null) {
                        ARFrame.AlignState state = mFrame.getAlignState();
                        LogUtils.e("AR_LOG_STATE", state.name());
//                        if (state == ARFrame.AlignState.SUCCESS) {
                        LogUtils.e("AR_LOG", path);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "AR平面加载完成", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
//                        } else if (state == ARFrame.AlignState.PROCESSING && count == 0) {
//                            count++;
//                        }
                    }
                }
            }
        }).start();
        //加载AR Plane
    }

    /**
     * 保存AR Plane
     */
    public void saveARPlanes() {
        final float angle = ((FloorMapActivity) getActivity()).getAngle();
        final PointF selectPoint = ((FloorMapActivity) getActivity()).getSelectPoint();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ARUtils.saveARSeesionPlane(mSession, selectPoint.x, selectPoint.y, angle);
                Collection<ARAnchor> arAnchors = getARAnchors();
                if (arAnchors.size() > 0) {
                    ShareMapHelper.svaAnchorToFile(Constant.AR_PATH + File.separator + "ar.data", arAnchors);
                }

            }
        }).start();
    }

    public void setAnchor() {
        mAnchors.add(mSession.createAnchor(mArPose));
    }

    private void handleTap(ARFrame frame, ARCamera camera) {
        MotionEvent tap = mQueuedSingleTaps.poll();

        if (tap != null && camera.getTrackingState() == ARTrackable.TrackingState.TRACKING) {
            ARHitResult hitResult = null;
            ARTrackable trackable = null;
            boolean hasHitFlag = false;

            List<ARHitResult> hitTestResult = frame.hitTest(tap);
            for (int i = 0; i < hitTestResult.size(); i++) {
                // Check if any plane was hit, and if it was hit inside the plane polygon
                ARHitResult hitResultTemp = hitTestResult.get(i);
                trackable = hitResultTemp.getTrackable();
                if ((trackable instanceof ARPlane
                        && ((ARPlane) trackable).isPoseInPolygon(hitResultTemp.getHitPose())
                        && (PlaneRenderer.calculateDistanceToPlane(hitResultTemp.getHitPose(), camera.getPose()) > 0))
                        || (trackable instanceof ARPoint
                        && ((ARPoint) trackable).getOrientationMode() == ARPoint.OrientationMode.ESTIMATED_SURFACE_NORMAL)) {
                    hasHitFlag = true;
                    hitResult = hitResultTemp;
                }

                if (trackable instanceof ARPlane) {
                    break;
                }
            }

            //if hit both Plane and Point,take Plane at the first priority.
            if (hasHitFlag != true) {
                return;
            }

            // Hits are sorted by depth. Consider only closest hit on a plane or oriented point.
            // Cap the number of objects created. This avoids overloading both the
            // rendering system and ARCore.
/*            if (mARAnchors.size() >= UtilsCommon.MAX_TRACKING_ANCHOR_NUM) {
                mARAnchors.get(0).anchor.detach();
                mARAnchors.remove(0);
            }*/

            // Adding an Anchor tells ARCore that it should track this position in
            // space. This anchor is created on the Plane to place the 3D model
            // in the correct position relative both to the world and to the plane.
            mAnchors.add(hitResult.createAnchor());
        }
    }


    class MySensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }
            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        azimuthAngle = (float) Math.toDegrees(values[0]);
    }

    public void judgeARAnchor(){
        if(hasplat){
            LogUtils.d("分割线","------------------");
            StringBuffer stringBuffer = new StringBuffer();
            for(ARAnchor arAnchor : mAnchors){
                stringBuffer.append("x:" + arAnchor.getPose().tx());
                stringBuffer.append("--" );
                stringBuffer.append("y:" + arAnchor.getPose().tz());
                stringBuffer.append("--" );
                stringBuffer.append("z:" + arAnchor.getPose().ty());
                stringBuffer.append("\\r\\n");
                double d =  Math.sqrt(Math.pow(mArPose.tx() - arAnchor.getPose().tx(), 2) + Math.pow(mArPose.tz() - arAnchor.getPose().tz(), 2));
                LogUtils.d("distance",d+"");
                if(d < 0.2){
                    arAnchor.detach();
                }
                LogUtils.d("state",arAnchor.getTrackingState().toString());
            }
            LogUtils.d("坐标",stringBuffer.toString());
        }
    }


    private void saveBitmap(Bitmap bitmap) {
        String path = Constant.FilePath + File.separator + "pRRU";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path2 = path + File.separator + System.currentTimeMillis() + ".jpg";
        File save = new File(path2);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(save);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
//            //发送广播通知系统扫描文件
//            Uri uri = Uri.fromFile(file);
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传图片
        PointF nowStation = ((FloorMapActivity) getActivity()).getNowStation();
        ApiManager.upLoadImage(Constant.MAP_ID, nowStation.x, nowStation.y, new File(path2), new CallBack() {
            @Override
            public void onSucCallBack(Object data) {
                LogUtils.e("SVA", "MainActivity_上传图片成功");
            }

            @Override
            public void onFailCallBack() {
                LogUtils.e("SVA", "MainActivity_上传图片失败");
            }
        });

    }

    /**
     * 判断是否需要记录照片
     *
     * @return
     */
    private boolean judge() {
        PointF nowStation = ((FloorMapActivity) getActivity()).getNowStation();
        double sqrt = Math.sqrt((nowStation.x - oldPointF.x) * (nowStation.x - oldPointF.x) + (nowStation.y - oldPointF.y) * (nowStation.y - oldPointF.y));
        if (sqrt > 5) {
            oldPointF = nowStation;
            return true;
        } else {
            return false;
        }
    }


}