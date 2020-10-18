package mx.tec.sep23;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Request extends Thread {

    private String url;
    private Handler handler;

    public Request(String url, Handler handler){
        this.url = url;
        this.handler = handler;
    }

    public void run(){
        try {

            URL direccion = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) direccion.openConnection();

            int code = connection.getResponseCode();

            if(code == HttpURLConnection.HTTP_OK){

                // STREAM
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuilder builder = new StringBuilder();
                String lineaActual;

                while((lineaActual = br.readLine()) != null){

                    builder.append(lineaActual);
                }

                String json = builder.toString();
                Log.wtf("JSON RECIBIDO", json);

                JSONArray resultado = new JSONArray(json);

                Message mensaje = new Message();
                mensaje.obj = resultado;
                handler.sendMessage(mensaje);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
