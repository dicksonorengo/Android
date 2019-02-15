package com.example.phonebook;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends FragmentPagerAdapter {
    private final List<Fragment> listF = new ArrayList<>();
    private final List<String> listS = new ArrayList<>();
    public ViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return listF.get(position);
    }
    @Override
    public int getCount() {
        return listS.size();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listS.get(position);
    }
    public void AddFragment(Fragment fragment,String string){
        listF.add(fragment);
        listS.add(string);
    }
}
