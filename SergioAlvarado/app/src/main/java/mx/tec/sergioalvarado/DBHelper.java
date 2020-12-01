package mx.tec.sergioalvarado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_FILE = "MyDatabase.db";
    private static final String TABLE = "Dogs";
    private static final String FIELD_ID = "id";
    private static final String FIELD_AGE = "age";
    private static final String FIELD_WEIGHT = "weight";

    public DBHelper(Context context) {
        super(context, DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE + "(" +
                FIELD_ID + " INTEGER PRIMARY KEY, " +
                FIELD_AGE + " INTEGER, " +
                FIELD_WEIGHT + " FLOAT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS ?";
        String[] params = {TABLE};
        sqLiteDatabase.execSQL(query, params);
        onCreate(sqLiteDatabase);
    }


    public void save(int age, float weight){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_AGE, age);
        values.put(FIELD_WEIGHT, weight);
        db.insert(TABLE, null, values);
    }

    public String[] search(int id){
        SQLiteDatabase db = getReadableDatabase();
        String clause = FIELD_ID + " = ?";
        String[] args = {id + ""};

        Cursor c = db.query(TABLE, null, clause, args, null, null, null);
        String age = "";
        String weight = "";

        if(c.moveToFirst()){
            age = c.getInt(1)+"";
            weight = c.getFloat(2)+"";
        }
        String[] res = {age, weight};
        return res;
    }

    public String[] get(int pos){
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE, null, null, null, null, null, null);
        String id = "";
        String age = "";
        String weight = "";
        int i = 0;
        while(c.moveToNext()){
            if(i==pos){
                id = c.getInt(0)+"";
                age = c.getInt(1)+"";
                weight = c.getFloat(2)+"";
                break;
            }
            i++;
        }
        String[] res = {id, age, weight};
        return res;
    }
}
