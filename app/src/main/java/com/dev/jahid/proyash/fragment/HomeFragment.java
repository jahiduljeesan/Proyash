package com.dev.jahid.proyash.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.jahid.proyash.adapter.ViewpagerAdapter;
import com.dev.jahid.proyash.database.AppData;
import com.dev.jahid.proyash.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AppData appData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appData = AppData.getAppData();
        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();

                if (Math.abs(verticalOffset) == totalScrollRange) {
                    //Collapsed
                    binding.toolbar.setVisibility(View.VISIBLE);
                }
                else if (verticalOffset == 0) {
                    //Fully expended
                    binding.toolbar.setVisibility(View.INVISIBLE);
                }
                else {
                    //Somewhere in between
                    binding.toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });

        initFragments();
    }

    private void initFragments() {
        ArrayList<DataFragment> fragmentsList = new ArrayList<>();

        DataFragment all = new DataFragment(appData.allList);
        DataFragment abp = new DataFragment(appData.abpList);
        DataFragment abn = new DataFragment(appData.abnList);
        DataFragment ap = new DataFragment(appData.apList);
        DataFragment an = new DataFragment(appData.anList);
        DataFragment bp = new DataFragment(appData.bpList);
        DataFragment bn = new DataFragment(appData.bnList);
        DataFragment op = new DataFragment(appData.opList);
        DataFragment on = new DataFragment(appData.onList);

        //adding objets to the fragments list;
        fragmentsList.add(all);
        fragmentsList.add(ap);
        fragmentsList.add(an);
        fragmentsList.add(bp);
        fragmentsList.add(bn);
        fragmentsList.add(op);
        fragmentsList.add(on);
        fragmentsList.add(abp);
        fragmentsList.add(abn);

        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(requireActivity().getSupportFragmentManager(),fragmentsList);
        binding.dataFragmentContainer.setAdapter(viewpagerAdapter);

        binding.dataFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.dataFragmentContainer.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}