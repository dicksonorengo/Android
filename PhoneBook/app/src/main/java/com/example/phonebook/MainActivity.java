package com.example.phonebook;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewAdapter viewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewAdapter = new ViewAdapter(getSupportFragmentManager());

        viewAdapter.AddFragment(new Call(),"Call");
        viewAdapter.AddFragment(new Contact(),"Contact");
        //viewAdapter.AddFragment(new Favourite(),"Favorite");


        viewPager.setAdapter(viewAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.call);
        tabLayout.getTabAt(1).setIcon(R.drawable.contact);
        //tabLayout.getTabAt(2).setIcon(R.drawable.star);

    }






}
