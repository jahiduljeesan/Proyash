package com.dev.jahid.proyash.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dev.jahid.proyash.fragment.DataFragment;
import com.dev.jahid.proyash.fragment.HomeFragment;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<DataFragment> fragmentsList = new ArrayList<>();

    public ViewpagerAdapter(@NonNull FragmentManager fm, ArrayList<DataFragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
