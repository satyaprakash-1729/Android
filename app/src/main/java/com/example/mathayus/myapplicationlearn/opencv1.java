package com.example.mathayus.myapplicationlearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import static android.R.attr.width;
import static com.example.mathayus.myapplicationlearn.R.attr.height;

public class opencv1 extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private static final String TAG = "OpenCVActivity";
    private JavaCameraView javaCameraView;
    Mat mRgba, mGray, mCanny, mResult, mTemp;
    static {
        System.loadLibrary("MyLib");
    }

    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        javaCameraView = (JavaCameraView)findViewById(R.id.javaCameraView1);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.i(TAG,"OpenCV Loaded");
            mLoaderCallBack.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }else{
            Log.i(TAG,"OpenCV Failed to Load");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9,this,mLoaderCallBack);
        }
    }
    @Override
    public void onCameraViewStarted(int width,int height){
        mRgba = new Mat(height,width, CvType.CV_8UC4);
        mGray = new Mat();
        mCanny = new Mat();
        mResult = new Mat();
        mTemp = new Mat();
    }
    @Override
    public void onCameraViewStopped(){
        mRgba.release();
        mGray.release();
        mCanny.release();
        mResult.release();
        mTemp.release();
    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mTemp = inputFrame.rgba();
        Core.transpose(mTemp,mRgba);
        Imgproc.resize(mRgba,mTemp,mTemp.size(),0,0,0);
        Core.flip(mTemp,mRgba,-1);
        //Imgproc.cvtColor(mRgba,mGray,Imgproc.COLOR_BGR2GRAY);
        //Imgproc.Canny(mGray,mResult,50,150);
        int a = opncvNative.convertGray(mRgba.getNativeObjAddr());
        Log.d(TAG,"--->"+a);
        return mRgba;
    }
}
