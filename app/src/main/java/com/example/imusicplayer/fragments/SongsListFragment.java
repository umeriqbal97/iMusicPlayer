package com.example.imusicplayer.fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.imusicplayer.R;
import com.example.imusicplayer.adapters.SongViewAdapter;
import com.example.imusicplayer.model.SongAttr;
import com.example.imusicplayer.service.MusicService;
import com.example.imusicplayer.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsListFragment extends Fragment implements SongViewAdapter.OnRecyclerListener{
    private View view;
    private List<SongAttr> songList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SongViewAdapter recyclerViewAdapter;
    private String TAG = "MyTag";
    private boolean getdata=true;
    private MainActivity mainActivity;

    public SongsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songs_list, container, false);
        recyclerView = view.findViewById(R.id.recyclersong);
        mainActivity= (MainActivity) getActivity();
        if(getdata) {
            getSongsFromMedia();
            getdata=false;
        }
        else{
            putIntoRecyclerView();
        }

        return view;
    }

    public void getSongsFromMedia() {
        ContentResolver musicResolver = getContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int dataColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            int albumart= musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);


            //add songs to list
            SongAttr songsPlaylistModel;
            do {
                songsPlaylistModel = new SongAttr();
                songsPlaylistModel.setSongname(musicCursor.getString(titleColumn));
                songsPlaylistModel.setArtistname(musicCursor.getString(artistColumn));
                songsPlaylistModel.setPath(musicCursor.getString(dataColumn));
                songsPlaylistModel.setAlbumart(musicCursor.getString(albumart));
                songList.add(songsPlaylistModel);
                Log.d(TAG, songsPlaylistModel.getArtistname() + " " + songsPlaylistModel.getSongname() + " " + songsPlaylistModel.getPath()+" " + songsPlaylistModel.getAlbumart());

            }
            while (musicCursor.moveToNext());
        }
        mainActivity.setList(songList);
        putIntoRecyclerView();
    }

    void putIntoRecyclerView() {
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerViewAdapter = new SongViewAdapter(getContext(), songList, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onRecyclerClick(int position) {
        if(mainActivity!=null){
            mainActivity.setPos(position);

            Intent intent=new Intent(getContext(),MusicService.class);
            intent.putExtra("music",songList.get(position));
            getContext().startService(intent);

        }
    }

}
