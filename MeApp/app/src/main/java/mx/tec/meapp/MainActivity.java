package mx.tec.meapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    EditText input;
    Button b;
    Boolean properties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.nameText);
        b = findViewById(R.id.menuButton);
        String name = readFromFile(this);
        input.setText(name);
    }

    public void menu(View v){

        if(!properties){
            writeToFile(input.getText().toString(), this);
        }
        Intent intentito = new Intent(this, MenuActivity.class);

        intentito.putExtra("name", input.getText().toString());
        startActivityForResult(intentito, 0);

    }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput("properties.txt", Context.MODE_PRIVATE));
            out.write(data);
            out.close();
            properties = true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream input = context.openFileInput("properties.txt");

            if ( input != null ) {
                InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                properties = true;
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                input.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            properties = false;
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}