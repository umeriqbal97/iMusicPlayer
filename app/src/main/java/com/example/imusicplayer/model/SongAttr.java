package com.example.imusicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SongAttr implements Serializable {
    private String songname;
    private String artistname;
    private String path;
    private String albumart;

    public String getAlbumart() {
        return albumart;
    }

    public void setAlbumart(String albumart) {
        this.albumart = albumart;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SongAttr() {

    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public SongAttr(String songname, String artistname) {
        this.songname = songname;
        this.artistname = artistname;
    }

    protected SongAttr(Parcel in) {
        this.songname = in.readString();
        this.artistname = in.readString();
        this.path = in.readString();
    }


}

