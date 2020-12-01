package mx.dashingcam.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class Recorder extends Thread implements Handler.Callback{
    private boolean recording = false;
    private Handler recorderHandler;
    private OutputStreamWriter outputStreamWriter = null;
    private String path;
    private Context context;


    public Recorder(String path, Context context){
        this.path = path;
        this.context= context;
    }

    public synchronized void waitUntilReady() {
        recorderHandler = new Handler(Looper.myLooper(), this);
    }

    public Handler getRecorderHandler() {
        return recorderHandler;
    }

    public void setRecording(boolean recording) {
        if(recording){
            try {
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(path+".txt", Context.MODE_PRIVATE),  "UTF-8");
            } catch (IOException e) {
                Log.e("Exception", "File open failed: " + e.toString());

            }
        }else{
            try {
                if(outputStreamWriter!= null)
                    outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("Exception", "File close failed: " + e.toString());
            }
        }
        this.recording = recording;
    }


    @Override
    public boolean handleMessage(@NonNull Message message) {
        if(recording){
            try {
                outputStreamWriter.write((String) message.obj);
                outputStreamWriter.write("\n");
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        return false;
    }


}
