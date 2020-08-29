package mx.tec.meapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MyHobbies extends AppCompatActivity {
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hobbies);
        input = findViewById(R.id.hobbyInput);
    }
    public void returnToMenu(View v){

        Intent intent = new Intent();
        intent.putExtra("hobby", input.getText().toString());

        // how to actually set information to be "sent" back
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}