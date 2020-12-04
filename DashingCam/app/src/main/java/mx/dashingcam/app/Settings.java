package mx.dashingcam.app;

import androidx.activity.OnBackPressedCallback;
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
    private boolean front, back;
    private int duration = 15;
    private int position = 3;

    private void picker(String item){
        switch(item){
            case("Select..."):
            case("15 min"):
            case("15"):
                duration=15;
                position=3;
                break;
            case("5 min"):
            case("5"):
                duration=5;
                position=1;
                break;
            case("10 min"):
            case("10"):
                duration=10;
                position=2;
                break;
            case("20 min"):
            case("20"):
                duration=20;
                position=4;
                break;
            case("30 min"):
            case("30"):
                duration=30;
                position=5;
                break;
            case("45 min"):
            case("45"):
                duration=45;
                position=6;
                break;
            case("60 min"):
            case("60"):
                duration=60;
                position=7;
                break;
            default:
                duration=15;
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String item = (String)parent.getItemAtPosition(pos);
        picker(item);
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

        sb.setMax((int)totalMemory());
        sb.setProgress((int)busyMemory());
        Intent intent = getIntent();
        duration = intent.getIntExtra("duration", 15);
        front = intent.getBooleanExtra("front", true);
        back = intent.getBooleanExtra("back", true);
        Switch frontSwitch = findViewById(R.id.switchFront);
        Switch backSwitch = findViewById(R.id.switchBack);
        frontSwitch.setChecked(front);
        backSwitch.setChecked(back);

        picker(duration+"");
        spinner.setSelection(position);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                back(findViewById(R.id.imageView2));
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
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
        Intent data = new Intent();
        data.putExtra("duration",duration);
        data.putExtra("front",front);
        data.putExtra("back",back);
        setResult(RESULT_OK,data);
        finish();
    }
    public void onSwitchClicked(View view) {
        // Is the button now checked?
        boolean checked = ((Switch) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.switchBack:
                back=checked;
                break;
            case R.id.switchFront:
                front=checked;
                break;
        }
    }


}