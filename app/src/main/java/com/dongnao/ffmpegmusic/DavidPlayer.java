package com.dongnao.ffmpegmusic;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by david on 2017/9/20.
 */

public class DavidPlayer  implements SurfaceHolder.Callback {
    static{
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avformat-56");
        System.loadLibrary("avutil-54");
        System.loadLibrary("postproc-53");
        System.loadLibrary("swresample-1");
        System.loadLibrary("swscale-3");
        System.loadLibrary("native-lib");
    }

    private boolean isPlaying=false;
    private SurfaceView surfaceView;

    public   void playJava(String path) {
        if (surfaceView == null) {
            return;
        }
        play(path);
    }

    public boolean isPlaying(){
        return  this.isPlaying;
    }

    public void setPlaying(boolean playState){
        this.isPlaying=playState;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        display(surfaceView.getHolder().getSurface());
        surfaceView.getHolder().addCallback(this);

    }

    /**
     * 播放
     * @param path
     */
    public native void play(String path);

    /**
     * 显示屏
     * @param surface
     */
    public native void display(Surface surface);

    /**
     * 停止
     */
    public native void stop();

    /**
     * 释放资源
     */
    public native void  release();


    public native void addWatermark(String inPath,String outPath,String pngPath);

    /**
     * 次方法在c调用
     */
    public void showWaittingDialog(){
        Log.e("show","showWaittingDialog");
        if (listener!=null){
            listener.showProgress(true);
        }
    }

    public  void dismissWaittingDialog(){
        Log.e("dismiss","dismissWaittingialog");
        if (listener!=null){
            listener.showProgress(false);
        }
    }

    private  VideoPrepareListener listener;
    public void setVideoPrepareListener(VideoPrepareListener listener){
        this.listener=listener;
    }

    public interface VideoPrepareListener{
        void showProgress(boolean complete);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e("surfaceCreated=","surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.e("surfaceChanged-format=",format+"========");
        Log.e("surfaceChanged-width=",width+"========");
        Log.e("surfaceChanged-height=",height+"========");
        display(surfaceHolder.getSurface());

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e("surfaceDestroyed=","destroyed");
        stop();
        release();
    }
}
