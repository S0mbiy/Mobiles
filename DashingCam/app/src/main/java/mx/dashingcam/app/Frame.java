package mx.dashingcam.app;

import android.graphics.Bitmap;

public class Frame {
    public int id;
    public Bitmap frame;
    public Frame(int id, Bitmap frame){
        this.frame = frame;
        this.id = id;
    }
    public Frame(int id){
        this.id = id;
    }
}
