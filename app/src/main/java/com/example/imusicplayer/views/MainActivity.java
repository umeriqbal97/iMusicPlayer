package com.example.imusicplayer.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imusicplayer.R;
import com.example.imusicplayer.adapters.MusicViewPager;
import com.example.imusicplayer.fragments.SongsListFragment;
import com.example.imusicplayer.model.SongAttr;
import com.example.imusicplayer.service.MusicService;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private MusicViewPager musicViewPager;
    private int pos;
    private ProgressBar progressBar;
    private ImageView toggleButton;
    private String TAG = "MyTag";
    private SongAttr songsPlaylistModel;
    private LinearLayout linearLayout;
    private List<SongAttr> songList;
    private ViewPager viewPager;
    private TextView textView;
    private int totalDuration, currentPosition = 0;
    private boolean isBound = false;
    private MusicService myService;


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


        IntentFilter intentFilter = new IntentFilter("showProgressBarAction");
        LocalBroadcastManager.getInstance(getApplicationContext()).
                registerReceiver(broadcastReceiver, intentFilter);

    }

    // Service connection
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("lol", "onServiceConnected: established ");
            isBound = true;
            MusicService.MyServiceClass serviceClass = (MusicService.MyServiceClass) service;
            myService = serviceClass.getReference();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    // Receive message if song is completed
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("name");
            int pos= intent.getIntExtra("pos",0);
            String songname=intent.getStringExtra("songName");
            assert name != null;
            if (name.equals("show")) {
                textView.setText(songname);
                if(myService.isPlaying()){
                    toggleButton.setImageResource(R.drawable.ic_pause_black_24dp);
                } else {
                    toggleButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                linearLayout.setVisibility(View.VISIBLE);
                showProgrerssBar();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).
                unregisterReceiver(broadcastReceiver);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.songname);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        toggleButton = findViewById(R.id.play_pause);
        progressBar = findViewById(R.id.progressbar);
        linearLayout = findViewById(R.id.music_bar);
        musicViewPager = new MusicViewPager(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
        viewPager.setAdapter(musicViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setList(List<SongAttr> songList){
        this.songList = songList;
    }
    public void setPos(final int position) {
        currentPosition=0;
        pos = position;
        songsPlaylistModel = songList.get(pos);
        textView.setText(songList.get(position).getSongname());
        toggleButton.setImageResource(R.drawable.ic_pause_black_24dp);
        myService.setData(songList, position);
    }


    public void nextSong(View view) {
        currentPosition=0;
        if (pos < songList.size() - 1) {
            pos++;
            textView.setText(songList.get(pos).getSongname());
        } else {
            pos = 0;
            textView.setText(songList.get(pos).getSongname());
        }
        myService.playForward();
    }

    public void tooglebutton(View view) {

        if (myService.isPlaying()) {
            toggleButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            myService.pause();
        } else {
            Log.d(TAG, "tooglebutton: ");
            toggleButton.setImageResource(R.drawable.ic_pause_black_24dp);
            myService.play();
            showProgrerssBar();
        }

    }

    public void showProgrerssBar() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    totalDuration = myService.getTotalPosition();
                    Log.d(TAG, "tooglebutton: "+currentPosition+" "+totalDuration);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                progressBar.setMax(totalDuration);


                while (currentPosition < totalDuration) {

                    if (!myService.isPlaying()) {
                        break;
                    } else {
                        progressBar.setProgress(currentPosition);
                    }

                    if (myService.isPlaying()) {
                        try {
                            Thread.sleep(1000);

                            currentPosition = myService.getCurrentPosition();

                            progressBar.setProgress(currentPosition);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (currentPosition == totalDuration) {
                            toggleButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            progressBar.setProgress(0);
                            currentPosition=0;
                            Log.d(TAG, "run: ");
                            break;
                        }

                    }


                }


            }
        }).start();
    }


    public void secondScreen(View view) {
        Intent intent=new Intent(this,SecondScreen.class);
        startActivity(intent);
    }
}
