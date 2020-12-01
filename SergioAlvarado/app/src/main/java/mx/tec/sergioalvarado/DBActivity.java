package mx.tec.sergioalvarado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DBActivity extends AppCompatActivity {
    private int pos;
    Button cleanBtn, saveBtn, searchBtn, previousBtn, nextBtn, backBtn;
    EditText greetingText, idText, ageText, weightText;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b);
        pos = 0;
        cleanBtn = findViewById(R.id.cleanBtn);
        saveBtn = findViewById(R.id.saveBtn);
        searchBtn = findViewById(R.id.searchBtn);
        previousBtn = findViewById(R.id.previousBtn);
        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);
        greetingText = findViewById(R.id.greetingText);
        idText = findViewById(R.id.idText);
        ageText= findViewById(R.id.ageText);
        weightText = findViewById(R.id.weightText);
        db = new DBHelper(this);

        this.fillTexts(pos);

        Intent intent = getIntent();
        greetingText.setText(intent.getStringExtra("greeting"));
    }

    public void fillTexts(int pos){
        String[] vals = db.get(pos);
        idText.setText(vals[0]);
        ageText.setText(vals[1]);
        weightText.setText(vals[2]);
    }
    public void next(View v){
        pos++;
        fillTexts(pos);
    }
    public void previous(View v){
        pos--;
        fillTexts(pos);
    }
    public void clean(View v){
        idText.setText("");
        ageText.setText("");
        weightText.setText("");
    }
    public void save(View v){
        try {
            db.save(Integer.parseInt(ageText.getText().toString()), Float.parseFloat(weightText.getText().toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public void search(View v){
        try {
            String[] vals = db.search(Integer.parseInt(idText.getText().toString()));
            ageText.setText(vals[0]);
            weightText.setText(vals[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public void back(View v){
        Intent intent = new Intent();
        intent.putExtra("id", idText.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}