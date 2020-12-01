package mx.tec.sergioalvaradoparcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    private Handler handler;
    private JSONArray data;
    private static final String FRAGMENT_TAG = "tag";
    private PerritoFragment perrito;
    private GatitoFragment gatito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(Looper.getMainLooper(), this);
        gatito = new GatitoFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.contenedor, gatito, FRAGMENT_TAG);
        ft.commit();

    }
    @Override
    public boolean handleMessage(@NonNull Message message) {
        data = (JSONArray) message.obj;
        perrito = PerritoFragment.newInstance(data.toString());
        cambiarFragmento(perrito);
        return false;
    }


    public void perritoButton(View v){
        PerritoRequest pr = new PerritoRequest("https://bitbucket.org/itesmguillermorivas/partial2/raw/ed1cb65b708b2d85dc1b777ea1278481301d9efe/doggos.json", handler);
        pr.start();
    }
    public void gatitoButton(View v){
        cambiarFragmento(gatito);
    }
    private void cambiarFragmento(Fragment nf){
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(FRAGMENT_TAG);

        if(f != null && f != nf){
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(f);
            ft.add(R.id.contenedor, nf, FRAGMENT_TAG);
            ft.commit();
        }
    }
}