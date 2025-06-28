package com.example.examen3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ciudades.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CIUDADES = "ciudades";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_LATITUD = "latitud";
    private static final String COLUMN_LONGITUD = "longitud";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CIUDADES = "CREATE TABLE " + TABLE_CIUDADES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_LATITUD + " REAL,"
                + COLUMN_LONGITUD + " REAL" + ")";
        db.execSQL(CREATE_TABLE_CIUDADES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CIUDADES);
        onCreate(db);
    }

    public long agregarCiudad(String nombre, double latitud, double longitud) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_LATITUD, latitud);
        values.put(COLUMN_LONGITUD, longitud);
        long id = db.insert(TABLE_CIUDADES, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Ciudad> obtenerTodasLasCiudades() {
        List<Ciudad> ciudades = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CIUDADES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Ciudad ciudad = new Ciudad();
                ciudad.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                ciudad.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE)));
                ciudad.setLatitud(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUD)));
                ciudad.setLongitud(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUD)));
                ciudades.add(ciudad);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ciudades;
    }
}