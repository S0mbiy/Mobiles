package mx.tec.sep23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback {

    private RecyclerView recyclerView;
    private Handler handler;
    private JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

//        FriendAdapter adapter = new FriendAdapter("[]", this);

        // recycler view (GUI)
        // layout manager - LinearLayoutManager, GridLayoutManager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
//        recyclerView.setAdapter(adapter);

        // get handler so we can actually exchange messages
        handler = new Handler(Looper.getMainLooper(), this);
    }

    @Override
    public void onClick(View view) {
        try {
            int pos = recyclerView.getChildLayoutPosition(view);
            JSONObject friend = data.getJSONObject(pos);
            Intent intent = new Intent(this, FriendView.class);
            intent.putExtra("nombre", friend.getString("nombre"));
            intent.putExtra("hobby", friend.getString("hobby"));
            intent.putExtra("age", friend.getString("age"));
            intent.putExtra("phone", friend.getString("phone"));
            intent.putExtra("address", friend.getString("address"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadRequest(View v) {
        Request request = new Request("https://S0mbiy.github.io/DevelopAPPsACT2.1/friends.json", handler);
        request.start();

    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
//            String prueba = "[{'nombre' : 'Diego', 'hobby' : 'videojuegos', 'age' : '10', 'phone': '12345678', 'address': 'Calle siempre viva 1234'}," +
//                    "{'nombre' : 'Luis Angel', 'hobby' : 'ver series', 'age' : '15', 'phone': '12345678', 'address': 'Calle siempre viva 1234'}," +
//                    "{'nombre' : 'Hugo', 'hobby' : 'ver peliculas', 'age' : '20', 'phone': '12345678', 'address': 'Calle siempre viva 1234'}]";
//            data = new JSONArray(prueba);
        data = (JSONArray) message.obj;
        // translator - Adapter
        FriendAdapter adapter = new FriendAdapter(data.toString(), this);
        recyclerView.setAdapter(adapter);

        return false;
    }
}