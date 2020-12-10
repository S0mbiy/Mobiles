package mx.dashingcam.app;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Exporter extends Thread{
    private int id;
    private String path;
    private Context context;
    private MainActivity main;
    private Handler handler;
    private boolean hasFile = false, activated=true;

    public Exporter(String path, Context context, MainActivity main, Handler handler, boolean activated, int id){
        this.path= path;
        this.context = context;
        this.main = main;
        this.handler = handler;
        this.activated = activated;
        this.id = id;
    }

    public void run(){

        File file = new File(context.getFilesDir(), path+".mp4");
        FileChannelWrapper out = null;
        try {

            InputStream inputStream = context.openFileInput(path+".txt");

            if ( inputStream != null && activated) {
                hasFile = true;
                out = NIOUtils.writableFileChannel(file.getAbsolutePath());
                AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(24, 1));
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String receiveString = "";
                while ((receiveString = br.readLine()) != null) {
                    byte[] data = new byte[receiveString.length() / 2];
                    for (int i = 0; i < data.length; i++) {
                        int index = i * 2;
                        int j = Integer.parseInt(receiveString.substring(index, index + 2), 16);
                        data[i] = (byte) j;
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    encoder.encodeImage(bmp);
                }
                encoder.finish();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            NIOUtils.closeQuietly(out);
        }
        if(hasFile){
            String videoFileName = path + "_" + System.currentTimeMillis() + ".mp4";

            ContentValues valuesvideos = new ContentValues();
            valuesvideos.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "DashingCam");
            valuesvideos.put(MediaStore.Video.Media.TITLE, videoFileName);
            valuesvideos.put(MediaStore.Video.Media.DISPLAY_NAME, videoFileName);
            valuesvideos.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            valuesvideos.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            valuesvideos.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            valuesvideos.put(MediaStore.Video.Media.IS_PENDING, 1);
            ContentResolver resolver = context.getContentResolver();
            Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri uriSavedVideo = resolver.insert(collection, valuesvideos);


            ParcelFileDescriptor pfd;

            try {
                pfd = context.getContentResolver().openFileDescriptor(uriSavedVideo, "w");

                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());

                File imageFile = new File(context.getFilesDir(), path+".mp4");

                FileInputStream in = new FileInputStream(imageFile);

                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) > 0) {

                    fos.write(buf, 0, len);
                }
                fos.close();
                in.close();
                pfd.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
            valuesvideos.clear();
            valuesvideos.put(MediaStore.Video.Media.IS_PENDING, 0);
            context.getContentResolver().update(uriSavedVideo, valuesvideos, null, null);

        }

        Message mensaje = handler.obtainMessage();
        mensaje.obj = new Frame(5);
        handler.sendMessage(mensaje);
        main.setExportingFalse(id);
    }

}
