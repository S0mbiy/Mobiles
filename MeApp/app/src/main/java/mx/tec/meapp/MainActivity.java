package mx.tec.meapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    EditText input;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.nameText);
        b = findViewById(R.id.menuButton);
    }

    public void menu(View v){


        Intent intentito = new Intent(this, MenuActivity.class);

        intentito.putExtra("name", input.getText().toString());
        startActivityForResult(intentito, 0);

    }
}