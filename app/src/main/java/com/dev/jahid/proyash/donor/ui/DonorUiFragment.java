package com.dev.jahid.proyash.donor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dev.jahid.proyash.databinding.FragmentPostBinding;
import com.dev.jahid.proyash.donor.DonorModel;
import com.dev.jahid.proyash.donor.adapter.ViewpagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DonorUiFragment extends Fragment {

    private FragmentPostBinding binding;
    private ViewpagerAdapter viewpagerAdapter;
    private DataFragment activeFragment;

    private List<DonorModel> allList = new ArrayList<>();
    private List<DonorModel> apList= new ArrayList<>();
    private List<DonorModel> anList= new ArrayList<>();
    private List<DonorModel> bpList= new ArrayList<>();
    private List<DonorModel> bnList= new ArrayList<>();
    private List<DonorModel> opList= new ArrayList<>();
    private List<DonorModel> onList= new ArrayList<>();
    private List<DonorModel> abpList = new ArrayList<>();
    private List<DonorModel> abnList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnDonorAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), DonorAddActivity.class));
            }
        });

        initFragments();
        //search view
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                activeFragment.getSearch(newText);
                return true;
            }
        });
    }

    private void initFragments() {
        ArrayList<DataFragment> fragmentsList = new ArrayList<>();

        DataFragment all = new DataFragment("all");
        DataFragment abp = new DataFragment("abp");
        DataFragment abn = new DataFragment("abn");
        DataFragment ap = new DataFragment("ap");
        DataFragment an = new DataFragment("an");
        DataFragment bp = new DataFragment("bp");
        DataFragment bn = new DataFragment("bn");
        DataFragment op = new DataFragment("op");
        DataFragment on = new DataFragment("on");

        activeFragment = all;

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

        viewpagerAdapter = new ViewpagerAdapter(getActivity().getSupportFragmentManager(), fragmentsList);
        binding.dataFragmentContainer.setAdapter(viewpagerAdapter);

        binding.dataFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
                activeFragment = fragmentsList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.dataFragmentContainer.setCurrentItem(tab.getPosition());
                activeFragment = fragmentsList.get(tab.getPosition());
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