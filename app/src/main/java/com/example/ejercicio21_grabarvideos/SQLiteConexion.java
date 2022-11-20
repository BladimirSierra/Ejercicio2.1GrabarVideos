package com.example.ejercicio21_grabarvideos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ejercicio21_grabarvideos.Transacciones.transacciones;

public class SQLiteConexion extends SQLiteOpenHelper
{
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(transacciones.CreateTablaVideos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(transacciones.DropTablaVideos);
        onCreate(db);
    }
}
