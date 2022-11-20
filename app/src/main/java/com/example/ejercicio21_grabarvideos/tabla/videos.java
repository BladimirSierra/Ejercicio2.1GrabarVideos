package com.example.ejercicio21_grabarvideos.tabla;
    import java.sql.Blob;

public class videos {
    private Blob video;


    public videos(byte[] blob) {}

    public videos() {
        this.video= video;
    }

    public Blob getVideo() {
        return video;
    }

    public void setVideo(Blob video) {

        this.video = video;
    }
}
