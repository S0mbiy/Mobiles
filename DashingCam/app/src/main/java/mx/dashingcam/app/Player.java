package mx.dashingcam.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Player extends Thread{
    private String path;
    private Handler handler;
    private Context context;
    private MainActivity main;
    private BroadcastRequest request;
    private boolean exit = false;


    public Player(Handler handler, String path, Context context, MainActivity main){
        this.path = path;
        this.handler = handler;
        this.context= context;
        this.main = main;
    }

    public void setRequest(BroadcastRequest request) {
        this.request = request;
    }

    public void exit(){
        exit = true;
    }

    public void run(){
        try {
            InputStream inputStream = context.openFileInput(path+".txt");

            if ( inputStream != null ) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String receiveString = "";
                while ( (receiveString = br.readLine()) != null && !exit) {
                    byte[] data = new byte[receiveString.length() / 2];
                    for (int i = 0; i < data.length; i++) {
                        int index = i * 2;
                        int j = Integer.parseInt(receiveString.substring(index, index + 2), 16);
                        data[i] = (byte) j;
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Message mensaje = handler.obtainMessage();
                    mensaje.obj = bmp;
                    handler.sendMessage(mensaje);
                    Thread.sleep(40);
                }

                inputStream.close();
                request.setStreaming(true);
                main.setPlaying(false);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
