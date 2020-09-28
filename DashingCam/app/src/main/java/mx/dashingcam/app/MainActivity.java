package mx.dashingcam.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity{
    WebView front, back;
    private boolean isFrontMain;
    private GestureDetectorCompat mGestureDetector;
    private static final String url = "http://192.168.1.76:80";

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

        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());
        front = findViewById(R.id.forntCamera);
        back = findViewById(R.id.backCamera);

        isFrontMain = true;
        front.clearCache(true);
        front.getSettings().setJavaScriptEnabled(true);
        front.setWebViewClient(new WebViewClient());
        front.loadUrl(url);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(back, "scaleX", 1f, .6f),
                ObjectAnimator.ofFloat(back, "scaleY", 1f, .6f),
                ObjectAnimator.ofFloat(back, "translationX", 260f),
                ObjectAnimator.ofFloat(back, "translationY", 200f)


        );
        set.start();

        front.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    return false;
                }

                if (event.getAction()==MotionEvent.ACTION_UP){
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

                return false;
            }
        });

        back.clearCache(true);
        back.getSettings().setJavaScriptEnabled(true);
        back.setWebViewClient(new WebViewClient());
        back.loadUrl(url);

        back.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    return false;
                }

                if (event.getAction()==MotionEvent.ACTION_UP){
                    if(isFrontMain) {
                        back.bringToFront();
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(front, "scaleX", 1, 0.6f),
                                ObjectAnimator.ofFloat(front, "scaleY", 1, 0.6f),
                                ObjectAnimator.ofFloat(front, "translationX", 1750f),
                                ObjectAnimator.ofFloat(front, "translationY", 480f),
                                ObjectAnimator.ofFloat(back, "scaleX", .6f, 1f),
                                ObjectAnimator.ofFloat(back, "scaleY", .6f, 1f),
                                ObjectAnimator.ofFloat(back, "translationX", -1400f),
                                ObjectAnimator.ofFloat(back, "translationY", -280f)
                        );
                        set.setDuration(500).start();
                        isFrontMain=false;
                    }
                }

                return false;
            }
        });
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
        startActivity(intent);
    }
    public void reload(View v){
        front.loadUrl(url);
        back.loadUrl(url);
    }

}