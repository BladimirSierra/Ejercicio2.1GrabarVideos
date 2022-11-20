package com.example.ejercicio21_grabarvideos;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ejercicio21_grabarvideos.Transacciones.transacciones;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int PETICION_ACCESO_CAM = 100;
    private static final int GALLERY_INTENT = 1;
    VideoView lienzo_pantalla;
    private Button btn_guardar, btn_grabar, btn_lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_guardar = (Button)findViewById(R.id.btnguardar);
        btn_grabar = (Button)findViewById(R.id.btngrabar);
        btn_lista = (Button)findViewById(R.id.btnlista);
        lienzo_pantalla = (VideoView)findViewById(R.id.pantalla_video);

        btn_grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Otorgar_Permisos();
            }
        });

        btn_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lista = new Intent(getApplicationContext(), ActivityViewVideo.class);
                startActivity(lista);
            }
        });
    }

    private void Tomar_video() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void Otorgar_Permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED  &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PETICION_ACCESO_CAM);
        }
        else
        {
            Tomar_video();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Tomar_video();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso a la camara", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Proceder a grabar el video
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            lienzo_pantalla.setMediaController((new MediaController(this)));
            lienzo_pantalla.setVideoURI(videoUri);
            lienzo_pantalla.requestFocus();
            lienzo_pantalla.start();
            Toast.makeText(getApplicationContext(), "Guardado en Galeria", Toast.LENGTH_LONG).show();
        }

        SQLiteConexion conexion = new SQLiteConexion(this, transacciones.NameDataBase,  null, 1);

        //Guardar Video en la base de datos
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    SQLiteDatabase db = conexion.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put(transacciones.tablavideo, transacciones.video.equals(uri.getLastPathSegment()));

                    Long resultado = db.insert(transacciones.tablavideo,transacciones.video, valores);
                    Toast.makeText(getApplicationContext(), "Guardado en SQL" +" "+ "Registro #: "+resultado, Toast.LENGTH_LONG).show();
                    db.close();
                }
            }
        });


    }

}