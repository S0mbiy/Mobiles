package mx.dashingcam.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class BroadcastRequest{

    private Handler handler, recorderHandler;
    private Recorder recorder;
    private WebSocket ws;
    private boolean streaming, recording;
    private String url;
    private Context context;
    private MainActivity main;


    private OkHttpClient client;

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
        recorder.setRecording(recording);
    }

    public boolean isStreaming() {
        return streaming;
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.d("WebSocket", "onOpen.");
            main.setConnecting(false);
        }  @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            if (streaming){
                byte[] data = bytes.toByteArray();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                Message mensaje = handler.obtainMessage();
                mensaje.obj = bmp;
                handler.sendMessage(mensaje);
            }
            if(recording){
                Message recorderMensaje = recorderHandler.obtainMessage();
                recorderMensaje.obj = bytes.hex();
                recorderHandler.sendMessage(recorderMensaje);
            }

        }  @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.i("WebSocket", "Closing Socket.");
        }  @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.e("WebSocket: ", "Error: "+t.getMessage());
            Message mensaje = handler.obtainMessage();
            mensaje.obj = BitmapFactory.decodeResource(context.getResources(), R.drawable.error);
            handler.sendMessage(mensaje);
            main.setConnecting(false);
        }
    }

    public void close(){
        ws.close(1000, null);
    }

    public BroadcastRequest(String url, Handler handler, Recorder recorder, Context context, MainActivity main){
        this.url = url;
        this.handler = handler;
        this.recorder = recorder;
        this.recorderHandler = recorder.getRecorderHandler();
        this.streaming = true;
        this.recording = false;
        this.context = context;
        this.main = main;
    }

    public void run(){
        Message mensaje = handler.obtainMessage();
        mensaje.obj = BitmapFactory.decodeResource(context.getResources(), R.drawable.conectando);
        handler.sendMessage(mensaje);
        this.client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        this.ws = client.newWebSocket(request, listener);
    }

}
