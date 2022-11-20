package com.example.ejercicio21_grabarvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ejercicio21_grabarvideos.Transacciones.transacciones;
import com.example.ejercicio21_grabarvideos.tabla.videos;

import java.sql.SQLException;
import java.util.ArrayList;

public class ActivityViewVideo extends AppCompatActivity {




    ListView lista;
    ArrayList<String> listainformacion;
    ArrayList<videos>lista_video;
    SQLiteConexion conexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        conexion = new SQLiteConexion(getApplicationContext(), "DBActual",null,1);
        lista = (ListView) findViewById(R.id.listview);

        try {
            consultarLista();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listainformacion);
        lista.setAdapter(adaptador);
    }

    private void consultarLista() throws SQLException {
        SQLiteDatabase db = conexion.getReadableDatabase();
        videos video = null;
        lista_video = new ArrayList<videos>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + transacciones.video, null);
        while(cursor.moveToNext()){

            video = new videos();

            video.getVideo();
            lista_video.add(video);
        }
        Obtener_Lista();
    }

    private void Obtener_Lista() {
        listainformacion = new ArrayList<String>();
        for (int i = 0; i < lista_video.size(); i++){
            listainformacion.add(String.valueOf(lista_video.get(i).getVideo()));
        }
    }
}