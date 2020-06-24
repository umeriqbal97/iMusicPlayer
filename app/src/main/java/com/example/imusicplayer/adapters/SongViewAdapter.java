package com.example.imusicplayer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imusicplayer.R;
import com.example.imusicplayer.model.SongAttr;

import java.util.List;

public class SongViewAdapter extends RecyclerView.Adapter<SongViewAdapter.SongViewHolder> {

    private Context context;
    private List<SongAttr> songsPlaylistModelList;
    private String TAG="MyTag1";
    private ImageView imageView;
    private OnRecyclerListener onRecyclerListener;

    public SongViewAdapter(Context context, List<SongAttr> songsPlaylistModelList, OnRecyclerListener onRecyclerListener) {
        this.context = context;
        this.songsPlaylistModelList = songsPlaylistModelList;
        this.onRecyclerListener=onRecyclerListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_view_ui_songs,null);
        SongViewHolder songViewHolder= new SongViewHolder(view,onRecyclerListener);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder recyclerViewHolder, int i) {
        SongAttr songsPlaylistModel=songsPlaylistModelList.get(i);
        recyclerViewHolder.sname.setText(songsPlaylistModel.getSongname());
        recyclerViewHolder.aname.setText(songsPlaylistModel.getArtistname());
        Log.d(TAG,songsPlaylistModel.getArtistname()+" "+songsPlaylistModel.getSongname());

    }

    @Override
    public int getItemCount() {
        return songsPlaylistModelList.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView sname,aname;
        OnRecyclerListener onRecyclerListener;
        public SongViewHolder(@NonNull View itemView,OnRecyclerListener onRecyclerListener) {
            super(itemView);
            this.onRecyclerListener=onRecyclerListener;
            sname=itemView.findViewById(R.id.lsongname);
            aname=itemView.findViewById(R.id.lartistname);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            //imageView.setImageResource(R.drawable.ic_volume_up_black_24dp);
            onRecyclerListener.onRecyclerClick(getAdapterPosition());
        }
    }
    public interface OnRecyclerListener{
        void onRecyclerClick(int position);
    }
}
