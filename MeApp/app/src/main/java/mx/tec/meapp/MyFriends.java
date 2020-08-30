package mx.tec.meapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyFriends extends AppCompatActivity {
    Button save, find, delete;
    EditText name, hobby;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        save = findViewById(R.id.saveButton);
        find = findViewById(R.id.findButton);
        delete = findViewById(R.id.deleteButton);
        name = findViewById(R.id.PersonName);
        hobby = findViewById(R.id.PersonHobby);
        db = new DBHelper(this);
    }
    public void saveDB(View v){

        db.save(name.getText().toString(), hobby.getText().toString());
        Toast.makeText(this, "SAVED!", Toast.LENGTH_SHORT).show();
    }

    public void deleteDB(View v){

        int rows = db.delete(name.getText().toString());
        Toast.makeText(this, rows + " ROWS AFFECTED.", Toast.LENGTH_SHORT).show();
    }

    public void findDB(View v){

        String h = db.find(name.getText().toString());
        hobby.setText(h);
    }
}