package teja.dely.cameraapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DELY on 10/24/2018.
 */

public class TimeDB extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "waktu_eksekusi";
    private static final String COL1 = "No";
    private static final String COL2 = "Nama";
    private static final String COL3 = "Start";
    private static final String COL4 = "End";
    private static final String COL5 = "Delta";
    private static final String COL6 = "Created_at";

    public TimeDB(Context context) {
        super(context, TABLE_NAME, null, DB_VERSION);
    }
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " TEXT, " +
                    COL3 + " TEXT, " +
                    COL4 + " TEXT, " +
                    COL5 + " TEXT, " +
                    COL6 + " TEXT );";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String nama, String start, String end, String delta , String created_at) {

        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, nama);
        contentValues.put(COL3, start);
        contentValues.put(COL4, end);
        contentValues.put(COL5, delta);
        contentValues.put(COL6, created_at);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db1.rawQuery(query, null);
        return data;
    }
    public  boolean updateData(int id , String status, String time) { // sesuaikan parameternya
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues dataku = new ContentValues();
        dataku.put("status",status);
        dataku.put("time",time);
        long status_update = db.update(TABLE_NAME,dataku,"id="+id,null);
        if (status_update == -1) {
            return false;
        } else {
            return true;
        }
    }
}
