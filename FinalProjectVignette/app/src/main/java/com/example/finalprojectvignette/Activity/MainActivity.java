package com.example.finalprojectvignette.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.finalprojectvignette.Authentication.Fragment_CreateAccount;
import com.example.finalprojectvignette.Authentication.Fragment_Login;
import com.example.finalprojectvignette.Authentication.PrefManager;
import com.example.finalprojectvignette.R;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefManager pref = new PrefManager(this);
        if(pref.isDataSet()){
            LoginButtonPressed(findViewById(R.id.Button_Login));
        }
    }
    public void CreateAccountClicked(View v)
    {
        fragmentmanager = getSupportFragmentManager();
        Fragment_CreateAccount createFrag = new Fragment_CreateAccount();
        FragmentTransaction trans = fragmentmanager.beginTransaction();
        trans.add(R.id.MyFrame, createFrag);
        trans.addToBackStack(null);
        trans.commit();
    }
    public void LoginButtonPressed(View v)
    {
        fragmentmanager = getSupportFragmentManager();
        Fragment_Login LoginFrag = new Fragment_Login();
        FragmentTransaction trans = fragmentmanager.beginTransaction();
        trans.add(R.id.MyFrame, LoginFrag);
        trans.addToBackStack(null);
        trans.commit();
    }
}

