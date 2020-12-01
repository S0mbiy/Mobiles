package mx.tec.sergioalvarado;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    EditText greeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        greeting = findViewById(R.id.greeting);
        bt1 = findViewById(R.id.button1);
    }

    public void dbActivity(View v){
        Intent intent = new Intent(this, DBActivity.class);

        intent.putExtra("greeting", greeting.getText().toString());
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            String id = data.getStringExtra("id");
            Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();
        }
    }

}