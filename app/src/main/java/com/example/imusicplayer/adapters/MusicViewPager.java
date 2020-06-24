package com.example.imusicplayer.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.imusicplayer.fragments.ArtistListFragment;
import com.example.imusicplayer.fragments.FileFolderFragment;
import com.example.imusicplayer.fragments.SongsListFragment;

public class MusicViewPager extends FragmentPagerAdapter {

    private int nooftabs;
    SongsListFragment songsListFragment;
    public MusicViewPager(@NonNull FragmentManager fm, int behavior,int nooftabs) {
        super(fm, behavior);
        this.nooftabs=nooftabs;
        songsListFragment=new SongsListFragment();
    }

    public SongsListFragment getSongsListFragment(){
        return songsListFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return songsListFragment;
            case 1:
                return new ArtistListFragment();
            case 2:
                return new FileFolderFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Songs";
            case 1:
                return "Artists";
            case 2:
                return "Files";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nooftabs;
    }
}
