package com.vikisoft.londrishops.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.vikisoft.londrishops.fragments.ViewAdultClothsFragment;
import com.vikisoft.londrishops.fragments.ViewHouseholdClothsFragment;
import com.vikisoft.londrishops.fragments.ViewKidsClothsFragment;

import java.util.ArrayList;
import java.util.List;

public class ClothsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private String paymentId;

    public ClothsPagerAdapter(@NonNull FragmentManager fm, String paymentId) {
        super(fm);
        mFragmentList.add(new ViewAdultClothsFragment(paymentId));
        mFragmentList.add(new ViewKidsClothsFragment(paymentId));
        mFragmentList.add(new ViewHouseholdClothsFragment(paymentId));
        this.paymentId = paymentId;
    }

    /*@NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }*/


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ViewAdultClothsFragment(paymentId);
        } else if (position == 1) {
            return new ViewKidsClothsFragment(paymentId);
        } else if (position == 2) {
            return new ViewHouseholdClothsFragment(paymentId);
        }

        return new ViewAdultClothsFragment(paymentId);
    }


    @Override
    public int getCount() {
        return 3;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
