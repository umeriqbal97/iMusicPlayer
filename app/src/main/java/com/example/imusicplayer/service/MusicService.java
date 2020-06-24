package com.example.imusicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.imusicplayer.model.SongAttr;
import com.example.imusicplayer.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private String path;
    private Uri uri;
    private int currentPos = 0;
    private int totalPos = 0;
    private Binder binder = new MyServiceClass();
    private List<SongAttr> songAttrList=new ArrayList<>();
    private int position;
    private SongAttr songAttr;

    public MusicService() {
    }

    public void setData(List<SongAttr> songAttrList,int position){
        this.songAttrList=songAttrList;
        this.position=position;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SongAttr songAttr = (SongAttr) intent.getSerializableExtra("music");
        mediaPlayerPlay(songAttr);
        return START_NOT_STICKY;
    }

    public class MyServiceClass extends Binder {
        public MusicService getReference() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("Fun", "onUnbind: ");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        setCurrentPos(mediaPlayer.getCurrentPosition());
        setTotalPos(mediaPlayer.getDuration());
        showProgressBroadcast();
    }

    public void playForward(){
        setCurrentPos(0);
        if (position < songAttrList.size() - 1) {
            position++;
            songAttr = songAttrList.get(position);
            mediaPlayerPlay(songAttr);
        } else {
            position = 0;
            songAttr = songAttrList.get(position);
            mediaPlayerPlay(songAttr);
        }
    }

    public void mediaPlayerPlay(SongAttr songAttr) {

        setCurrentPos(0);

        path = songAttr.getPath();
        uri = Uri.parse(path);


        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        showProgressBroadcast();
    }

    public void showProgressBroadcast(){
        Intent intent = new Intent("showProgressBarAction");
        intent.putExtra("name", "show");
        intent.putExtra("pos",position);
        intent.putExtra("songName",songAttrList.get(position).getSongname());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }


    public boolean isPlaying() {
        boolean flag=false;
        try {
            flag=mediaPlayer.isPlaying();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return flag;
    }

    public void play() {
        mediaPlayer.start();
    }


    public void pause() {
        mediaPlayer.pause();

    }

    public int getCurrentPosition() {
        try {
            currentPos = mediaPlayer.getCurrentPosition() / 60;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public void setTotalPos(int totalPos) {
        this.totalPos = totalPos;
    }

    public int getTotalPosition() {
        try {
            totalPos = mediaPlayer.getDuration() / 60;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return totalPos;
    }

}
