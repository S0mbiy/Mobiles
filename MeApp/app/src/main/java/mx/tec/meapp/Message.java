package mx.tec.meapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Message extends AppCompatActivity {
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        b = findViewById(R.id.Send);

        // 2. if you have a single case logic - use an anonymous class
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Message.this,
                        "Message sent" ,
                        Toast.LENGTH_SHORT)
                        .show();
                setResult(Activity.RESULT_OK, null);
                finish();
            }
        });
    }
}