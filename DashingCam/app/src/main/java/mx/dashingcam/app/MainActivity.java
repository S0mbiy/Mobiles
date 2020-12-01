package mx.dashingcam.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements Handler.Callback{
    ImageView front, back;
    private boolean isFrontMain, recording = false, exporting=false, playing=false, connecting=false;
    private static final int requestCode = 1;
    private GestureDetectorCompat mGestureDetector;
    private static final String url = "ws://192.168.4.1:8888";
    private Handler handler;
    private Recorder recorder;
    private Button record, play;
    private BroadcastRequest request;
    private Player player = null;



    @Override
    public boolean handleMessage(@NonNull Message message) {
        Bitmap data = (Bitmap) message.obj;
        front.setImageBitmap(Bitmap.createScaledBitmap(data, 800,600, false));
        return false;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            hideSystemUI();
            return true;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        askPermission();

        record = findViewById(R.id.record);
        play = findViewById(R.id.play);
        handler = new Handler(Looper.getMainLooper(), this);
        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());
        front = findViewById(R.id.FRONT);
        back = findViewById(R.id.BACK);

        isFrontMain = true;

        recorder = new Recorder("videoFront", this);
        recorder.waitUntilReady();

        request = new BroadcastRequest(url, handler, recorder, this, this);
        connecting = true;
        request.run();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(back, "scaleX", 1f, .6f),
                ObjectAnimator.ofFloat(back, "scaleY", 1f, .6f),
                ObjectAnimator.ofFloat(back, "translationX", 260f),
                ObjectAnimator.ofFloat(back, "translationY", 200f)
        );
        set.start();
    }
//    public void askPermission(){
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            String[] permisos = {Manifest.permission.MANAGE_EXTERNAL_STORAGE};
//            requestPermissions(permisos, REQUEST_PERMISO);
//            Log.wtf("","preg");
//        }
//    }
//    public void onRequestPermissionsResult(int requestCode, String[] p, int[] r){
//        if(r.length==1){
//            if(requestCode == REQUEST_PERMISO && r[0]!= PackageManager.PERMISSION_GRANTED){
//                askPermission();
//                Log.wtf("","perm res");
//            }
//        }
//    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setExporting(boolean exporting) {
        this.exporting = exporting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public void frontClick(View v){
        if(!isFrontMain) {
            front.bringToFront();
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(front, "translationX", 0f),
                    ObjectAnimator.ofFloat(front, "translationY", 0f),
                    ObjectAnimator.ofFloat(front, "scaleX", .6f, 1f),
                    ObjectAnimator.ofFloat(front, "scaleY", .6f, 1f),
                    ObjectAnimator.ofFloat(back, "translationX", 260f),
                    ObjectAnimator.ofFloat(back, "translationY", 200f),
                    ObjectAnimator.ofFloat(back, "scaleX", 1f, .6f),
                    ObjectAnimator.ofFloat(back, "scaleY", 1f, .6f)

            );
            set.setDuration(500).start();
            isFrontMain=true;
        }
    }
    public void backClick(View v){
        if(isFrontMain) {
            back.bringToFront();
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(front, "scaleX", 1, 0.6f),
                    ObjectAnimator.ofFloat(front, "scaleY", 1, 0.6f),
                    ObjectAnimator.ofFloat(front, "translationX", 1800f),
                    ObjectAnimator.ofFloat(front, "translationY", 600),
                    ObjectAnimator.ofFloat(back, "scaleX", .6f, 1f),
                    ObjectAnimator.ofFloat(back, "scaleY", .6f, 1f),
                    ObjectAnimator.ofFloat(back, "translationX", -1540f),
                    ObjectAnimator.ofFloat(back, "translationY", -390f)
            );
            set.setDuration(500).start();
            isFrontMain=false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        Log.d("UI", "System UI hidden");
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    public void settings(View v){
        Intent intent = new Intent(this, Settings.class);
        startActivityForResult(intent,requestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode) {
            if (resultCode == RESULT_OK) {
//                String street = data.getStringExtra("streetkey");
            }
        }
    }
    public void reload(View v){
        if(!connecting) {
            request.close();
            connecting = true;
            request.run();
            this.recording = false;
            request.setRecording(this.recording);
            record.setBackgroundResource(R.drawable.record);
        }
    }

    public void record(View v){
        this.recording = !this.recording;
        request.setRecording(this.recording);
        if(this.recording){
            record.setBackgroundResource(R.drawable.stop);
        }else{
            record.setBackgroundResource(R.drawable.record);
        }
    }

    public void play(View v){
        if(!this.playing) {
            play.setBackgroundResource(R.drawable.stop);
            Toast.makeText(this, "Playing video", Toast.LENGTH_SHORT).show();
            this.playing = true;
            request.setStreaming(false);
            this.recording = false;
            request.setRecording(this.recording);
            player = new Player(handler, "videoFront", this, this);
            player.setRequest(request);
            player.start();
        }else{
            if(player != null){
                play.setBackgroundResource(R.drawable.play);
                player.exit();
                Toast.makeText(this, "Video stopped", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void exportVideo(View v){
        if(this.recording){
            Toast.makeText(this, "Must stop recording before exporting video", Toast.LENGTH_SHORT).show();
        }else {
            if (!exporting) {
                Toast.makeText(this, "Exporting video", Toast.LENGTH_SHORT).show();
                exporting = true;
                Exporter exporter = new Exporter("videoFront", this, this);
                exporter.start();
            }
        }
    }

}