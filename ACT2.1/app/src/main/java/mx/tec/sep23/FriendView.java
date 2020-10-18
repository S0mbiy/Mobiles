package mx.tec.sep23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FriendView extends AppCompatActivity {
    TextView nombre, hobby, age, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);
        nombre = findViewById(R.id.textNombre);
        hobby = findViewById(R.id.textHobby);
        age = findViewById(R.id.textAge);
        phone = findViewById(R.id.textPhone);
        address = findViewById(R.id.textAddress);
        Intent intent = getIntent();
        nombre.setText(intent.getStringExtra("nombre"));
        hobby.setText(intent.getStringExtra("hobby"));
        age.setText(intent.getStringExtra("age"));
        phone.setText(intent.getStringExtra("phone"));
        address.setText(intent.getStringExtra("address"));
    }
}