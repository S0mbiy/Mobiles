package mx.dashingcam.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private GestureDetectorCompat mGestureDetector;
    private ProgressBar sb;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("Debug", "Selected:"+parent.getItemAtPosition(pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d("Debug", "Nothing Selected");
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            hideSystemUI();
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mGestureDetector = new GestureDetectorCompat(this, new Settings.GestureListener());
        sb = findViewById(R.id.progressBar);

        Spinner spinner = (Spinner) findViewById(R.id.time_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.record_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Log.d("Debug", "Memory "+busyMemory()+"/"+totalMemory());
        sb.setMax((int)totalMemory());
        sb.setProgress((int)busyMemory());

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
    public float totalMemory()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long   total  = (statFs.getBlockCountLong() * statFs.getBlockSizeLong());
        total = total/(1024*1024);
        return total;
    }
    public float busyMemory()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long   total  = (statFs.getBlockCountLong() * statFs.getBlockSizeLong());
        long   free   = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
        long   busy   = total - free;
        busy = busy/(1024*1024);
        return busy;
    }


    public void back(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onSwitchClicked(View view) {
        // Is the button now checked?
        boolean checked = ((Switch) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.switchBack:
                if (checked)
                    Log.d("Debug", "Selected Back:"+view.getId());
                    break;
            case R.id.switchFront:
                if (checked)
                    Log.d("Debug", "Selected Front:"+view.getId());
                    break;
        }
    }


}