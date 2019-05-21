package com.example.finalprojectvignette.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.eftimoff.viewpagertransformers.CubeOutTransformer;
import com.eftimoff.viewpagertransformers.TabletTransformer;
import com.example.finalprojectvignette.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ClassFragment extends Fragment {
    FragmentActivity myContext;
    public static String classid, className;
    Toolbar toolbar;
    private int[] layouts = new int[]{
            R.layout.fragment_student_list,
            R.layout.fragment_home,
            R.layout.fragment_activities_grid
    };
    private String[] titles = new String[]{
            "Students", "Posts", "Activities"
    };
    private ViewPager.PageTransformer[] transformers = new ViewPager.PageTransformer[]{
        new CubeOutTransformer(), new AccordionTransformer(),new TabletTransformer()
    };
    public ClassFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class, container, false);
        toolbar = getActivity().findViewById(R.id.teacher_toolbar);
        setHasOptionsMenu(true);
        classid = getArguments().getString("classId");
        ViewPager pager = v.findViewById(R.id.class_viewpager);
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        Random rand = new Random();
        int max = transformers.length-1,min = 0;
        int  n = rand.nextInt((max - min) + 1) + min;
        pager.setPageTransformer(true, transformers[n]);
        TabLayout tab = v.findViewById(R.id.class_tab_titles);
        tab.setupWithViewPager(pager);

        FirebaseDatabase.getInstance().getReference().child("classes").child(classid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toolbar.setTitle(dataSnapshot.child("class_name").getValue().toString());
                //Toast.makeText(getContext(), "Your new class '" + dataSnapshot.child("class_name").getValue().toString() + "'Good", Toast.LENGTH_SHORT).show();
                className = dataSnapshot.child("class_name").getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.empty_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public static ClassFragment newInstance(String ClassId) {
        Bundle args = new Bundle();
        ClassFragment fragment = new ClassFragment();
        args.putString("classId",ClassId);
        fragment.setArguments(args);
        return fragment;
    }
    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public  MyPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ClassStudentList();
                case 1:
                    return new HomeFragment();
                case 2:
                    return new Fragment_ActivitiesGrid();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
