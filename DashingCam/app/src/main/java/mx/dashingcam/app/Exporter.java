package mx.dashingcam.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Exporter extends Thread{
    private String path;
    private Context context;
    private MainActivity main;
    public Exporter(String path, Context context, MainActivity main){
        this.path= path;
        this.context = context;
        this.main = main;
    }

    public void run(){

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "DashingCam");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, path+".mp4");
        FileChannelWrapper out = null;
        try {
            out = NIOUtils.writableFileChannel(file.getAbsolutePath());
            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(24, 1));
            InputStream inputStream = context.openFileInput(path+".txt");

            if ( inputStream != null ) {
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
                main.setExporting(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            NIOUtils.closeQuietly(out);
        }
    }

}
