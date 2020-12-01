package mx.tec.sergioalvaradoparcial2;

import android.os.Handler;
import android.os.Message;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PerritoRequest extends Thread {

    private String url;
    private Handler handler;

    public PerritoRequest(String url, Handler handler){
        this.url = url;
        this.handler = handler;
    }

    public void run(){
        try {
            URL myUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myUrl.openConnection();
            int response = con.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                StringBuilder sb = new StringBuilder();
                String line;
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                while((line = br.readLine()) != null){

                    sb.append(line);
                }

                String received = sb.toString();
                JSONArray ja = new JSONArray(received);

                Message mensaje = new Message();
                mensaje.obj = ja;
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
