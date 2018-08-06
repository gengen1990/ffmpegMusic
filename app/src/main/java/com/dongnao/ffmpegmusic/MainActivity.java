package com.dongnao.ffmpegmusic;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;


public class  MainActivity<T> extends AppCompatActivity {

   private DavidPlayer davidPlayer;
    private  SurfaceView surfaceView;


    private View playBtn,stopBtn;


    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = (SurfaceView) findViewById(R.id.surface);
        davidPlayer = new DavidPlayer();
        surfaceView.setKeepScreenOn(true);

        playBtn=findViewById(R.id.play);
        playBtn.setOnClickListener((View view)->{
            player(view);
        });
        stopBtn=findViewById(R.id.stop);
        stopBtn.setOnClickListener((View view)->{
            stop(view);
        });

        DisplayMetrics metrics=new DisplayMetrics();
        Display display=getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);


        Log.e(" metrics.heightPixels=", metrics.heightPixels+"");
        Log.e(" metrics.widthPixels=", metrics.widthPixels+"");
        Log.e(" metrics.density=", metrics.density+"");
        Log.e(" metrics.scaledDensity=", metrics.scaledDensity+"");


        surfaceView.getLayoutParams().height=(int)(metrics.heightPixels / metrics.scaledDensity);
        surfaceView.getLayoutParams().width=metrics.widthPixels;
        surfaceView.requestLayout();

        davidPlayer.setSurfaceView(surfaceView);


       // progressDialog=ProgressDialog.show(this,"","请稍后");

      /*  davidPlayer.setVideoPrepareListener((boolean ok)->{
            if (ok){
                if (progressDialog==null){
                    progressDialog=ProgressDialog.show(MainActivity.this,"","请稍后");
                }
            }else{
                if (progressDialog!=null){
                    progressDialog.dismiss();
                    progressDialog=null;
                }

            }
        });*/


      findViewById(R.id.all_screen).setOnClickListener((View view)->{
          doScreenChange();
      });

      new Handler(Looper.getMainLooper()).postDelayed(()->{
          player(playBtn);
      },1000);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){//横屏
             Log.e("onConfigurationChanged=","横屏");
            absrequestLayout(true);
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){//竖屏
            Log.e("onConfigurationChanged＝","竖屏");
            absrequestLayout(false);
        }
    }

    private void absrequestLayout(boolean landscape){
        DisplayMetrics metrics=new DisplayMetrics();
        Display display=getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);

        Log.e(" metrics.heightPixels=", metrics.heightPixels+"");
        Log.e(" metrics.widthPixels=", metrics.widthPixels+"");
        Log.e(" metrics.density=", metrics.density+"");
        Log.e(" metrics.scaledDensity=", metrics.scaledDensity+"");

        if (landscape){
            surfaceView.getLayoutParams().height=metrics.heightPixels;
            surfaceView.getLayoutParams().width=metrics.widthPixels;
        }else{
            surfaceView.getLayoutParams().height=(int)(metrics.heightPixels/metrics.scaledDensity);
            surfaceView.getLayoutParams().width=metrics.widthPixels;
        }

    }

    private void doScreenChange(){
        //获得当前屏幕方向
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //变成竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //变成横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

    public void player(View view) {
        if (!davidPlayer.isPlaying()){
            play();
            davidPlayer.setPlaying(true);
        }
        view.setEnabled(false);
        if (stopBtn!=null){
            stopBtn.setEnabled(true);
        }
    }


    private void play(){

//        String path= Environment.getExternalStorageDirectory().getAbsoluteFile() +
//                File.separator + "DCIM" + File.separator + "input.mp4";
//        if (!new File(path).exists()){
//            Log.e("哈哈","文件不存在");
//            return;
//        }

        //davidPlayer.playJava(path);//播放手机本地视频

        davidPlayer.playJava("rtmp://live.hkstv.hk.lxdns.com/live/hks");//看直播的－香港卫视
    }

    public void stop(View view) {
        if (davidPlayer.isPlaying()){
            davidPlayer.stop();
            davidPlayer.setPlaying(false);
        }
        view.setEnabled(false);
        if (playBtn!=null){
            playBtn.setEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (davidPlayer.isPlaying()){
            davidPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        davidPlayer.release();

    }
}
