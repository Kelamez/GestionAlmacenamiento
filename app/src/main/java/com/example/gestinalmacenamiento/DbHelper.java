package com.example.gestinalmacenamiento;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "Almacen";
    public static final String TABLE_ALMACEN = "t_almacen";
    public static final String TABLE_ITEM = "t_item";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ALMACEN + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "tabla INTEGER AUTOINCREMENT");

        //ELIMINAR UNA TABLA
        //sqLiteDatabase.execSQL("DROP TABLE " + TABLE_ITEM);

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ITEM + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "tabla INTEGER NOT NULL," +
                "cantidad INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
