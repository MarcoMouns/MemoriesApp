package dude.memories.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dude.memories.Models.MemoryModel;


public class DB_Helper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MemoryManager.db";
    private static final String TABLE_MEMORY = "memory";
    private static final String COLUMN_MEMORY_ID = "memory_id";
    private static final String COLUMN_MEMORY_TITLE = "memory_title";
    private static final String COLUMN_MEMORY_DESCRIPTION = "memory_description";
    private static final String COLUMN_MEMORY_LNG = "memory_lng";
    private static final String COLUMN_MEMORY_LAT = "memory_lat";
    private static final String COLUMN_MEMORY_PICTURE = "picture";
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_MEMORY + "("
            + COLUMN_MEMORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MEMORY_TITLE + " TEXT, "
            + COLUMN_MEMORY_DESCRIPTION + " TEXT, "
            + COLUMN_MEMORY_LNG + " TEXT, "
            + COLUMN_MEMORY_LAT + " TEXT, "
            + COLUMN_MEMORY_PICTURE + " blol " + ")";


    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_MEMORY;

    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void addUser(MemoryModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MEMORY_TITLE, model.getTitle());
            values.put(COLUMN_MEMORY_DESCRIPTION, model.getDescription());
            values.put(COLUMN_MEMORY_LNG, model.getLng());
            values.put(COLUMN_MEMORY_LAT, model.getLat());
            values.put(COLUMN_MEMORY_PICTURE, model.getPicture());
            db.insertOrThrow(TABLE_MEMORY, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("add", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public List<MemoryModel> getAllMemories() {
        List<MemoryModel> memoryList = new ArrayList<MemoryModel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEMORY, null);
        try {
            if (cursor.moveToFirst()) {

                do {
                    MemoryModel memoryModel = new MemoryModel();
                    memoryModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MEMORY_ID))));
                    memoryModel.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_MEMORY_TITLE)));
                    memoryModel.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_MEMORY_DESCRIPTION)));
                    memoryModel.setLng(cursor.getString(cursor.getColumnIndex(COLUMN_MEMORY_LNG)));
                    memoryModel.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_MEMORY_LAT)));
                    memoryModel.setPicture(cursor.getBlob(cursor.getColumnIndex(COLUMN_MEMORY_PICTURE)));
                    memoryList.add(memoryModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("getall", e.getMessage());
        } finally {

            cursor.close();
        }

        return memoryList;
    }
}
