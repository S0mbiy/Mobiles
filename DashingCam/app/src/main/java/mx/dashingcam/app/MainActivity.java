package mx.dashingcam.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


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
    private Storage storage;
    private JSONObject setting;
    int duration;
    boolean fVal, bVal;
    private int width, height;

    @Override
    public boolean handleMessage(@NonNull Message message) {
        Frame frame = (Frame) message.obj;
        if(frame.id == 1){
            Bitmap data = frame.frame;
            front.setImageBitmap(Bitmap.createScaledBitmap(data, 800,600, false));
        }else if(frame.id == 2){
            //todo back view
        }else if(frame.id == 3){
            play.setBackgroundResource(R.drawable.play);
        }else if(frame.id == 4){
            record.setBackgroundResource(R.drawable.record);
        }else{
            Log.wtf("Handler","Unrecognized id found");
        }
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

        askPermission();

        storage = new Storage(this);
        try {
            String val = storage.readFromFile();
            setting = new JSONObject(val);
            duration = setting.getInt("duration");
            fVal = setting.getBoolean("front");
            bVal = setting.getBoolean("back");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        record = findViewById(R.id.record);
        play = findViewById(R.id.play);
        handler = new Handler(Looper.getMainLooper(), this);
        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());
        front = findViewById(R.id.FRONT);
        back = findViewById(R.id.BACK);

        isFrontMain = true;

        recorder = new Recorder("videoFront", this);
        recorder.waitUntilReady();

        request = new BroadcastRequest(url, handler, recorder, this, this, 1);
        connecting = true;
        request.setDuration(duration);
        request.setActivated(fVal);
        request.start();

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(back, "scaleX", 1f, .6f),
                ObjectAnimator.ofFloat(back, "scaleY", 1f, .6f),
                ObjectAnimator.ofFloat(back, "translationX", width*16/32),
                ObjectAnimator.ofFloat(back, "translationY", height*10/31)
        );
        set.start();
    }
    public void askPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            String[] permisos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permisos, 1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            String[] permisos = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permisos, 1);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] p, int[] r){
        if(r.length==1){
            if(requestCode == 1 && r[0]!= PackageManager.PERMISSION_GRANTED){
                Log.wtf("","perm res");
                askPermission();
            }
        }
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setExporting(boolean exporting) {
        this.exporting = exporting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
        request.setRecording(this.recording);
    }

    public void frontClick(View v){
        if(!isFrontMain) {
            front.bringToFront();
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(front, "translationX", 0),
                    ObjectAnimator.ofFloat(front, "translationY", 0),
                    ObjectAnimator.ofFloat(front, "scaleX", .6f, 1f),
                    ObjectAnimator.ofFloat(front, "scaleY", .6f, 1f),
                    ObjectAnimator.ofFloat(back, "translationX", width*16/32),
                    ObjectAnimator.ofFloat(back, "translationY", height*10/31),
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
                    ObjectAnimator.ofFloat(front, "translationX", width*16/32),
                    ObjectAnimator.ofFloat(front, "translationY", height*10/31),
                    ObjectAnimator.ofFloat(back, "scaleX", .6f, 1f),
                    ObjectAnimator.ofFloat(back, "scaleY", .6f, 1f),
                    ObjectAnimator.ofFloat(back, "translationX", 0),
                    ObjectAnimator.ofFloat(back, "translationY", 0)
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
        intent.putExtra("duration", duration);
        intent.putExtra("front", fVal);
        intent.putExtra("back", bVal);
        startActivityForResult(intent,requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode) {
            if (resultCode == RESULT_OK) {
                duration = data.getIntExtra("duration", 15);
                fVal = data.getBooleanExtra("front",true);
                bVal = data.getBooleanExtra("back",true);
                storage.writeToFile("{'duration':"+duration+",'front':"+fVal+",'back':"+bVal+"}");
            }
        }
    }
    public void reload(View v){
        if(!connecting) {
            request.close();
            request = new BroadcastRequest(url, handler, recorder, this, this, 1);
            connecting = true;
            request.setDuration(duration);
            request.setActivated(fVal);
            request.start();
            setRecording(false);
            record.setBackgroundResource(R.drawable.record);
        }
    }

    public void record(View v){
        if(this.playing){
            play(findViewById(R.id.play));
        }
        setRecording(!this.recording);
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
            setRecording(false);
            record.setBackgroundResource(R.drawable.record);
            player = new Player(handler, "videoFront", this, this, 1);
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