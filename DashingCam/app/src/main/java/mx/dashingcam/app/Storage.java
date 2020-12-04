package mx.dashingcam.app;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Storage {
    private Context context;
    public Storage(Context context){
        this.context=context;
    }

    public void writeToFile(String data) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput("Settings.txt", Context.MODE_PRIVATE));
            out.write(data);
            out.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public String readFromFile() {

        String ret = "";

        try {
            InputStream input = context.openFileInput("Settings.txt");

            if ( input != null ) {
                InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                String receiveString = bufferedReader.readLine();
                input.close();
                ret = receiveString;
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            String data = "{'duration':15,'front':true,'back':true}";
            writeToFile(data);
            ret = data;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
